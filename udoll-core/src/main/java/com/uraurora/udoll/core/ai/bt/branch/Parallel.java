package com.uraurora.udoll.core.ai.bt.branch;

import com.google.common.collect.Lists;
import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.BranchNode;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.annotation.NodeAttribute;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

import java.util.List;

import static com.uraurora.udoll.core.ai.bt.BTStatus.*;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-30 15:16
 * @description :
 */
public class Parallel<E> extends BranchNode<E> {

    /**
     * Optional node attribute specifying the parallel policy (defaults to {@link Policy#Sequence})
     */
    @NodeAttribute
    public Policy policy;
    /**
     * Optional node attribute specifying the execution policy (defaults to {@link Orchestrator#Resume})
     */
    @NodeAttribute
    public Orchestrator orchestrator;

    private boolean noRunningNodes;
    private Boolean lastResult;
    private int currentChildIndex;

    /**
     * Creates a parallel node with sequence policy, resume orchestrator and no children
     */
    public Parallel() {
        this(Lists.<INode<E>>newArrayList());
    }

    public Parallel(INode<E> node) {
        this(Lists.<INode<E>>newArrayList(node));
    }

    public Parallel(INode<E> node1, INode<E> node2) {
        this(Lists.<INode<E>>newArrayList(node1, node2));
    }

    public Parallel(INode<E> node1, INode<E> node2, INode<E> node3) {
        this(Lists.<INode<E>>newArrayList(node1, node2, node3));
    }

    /**
     * Creates a parallel node with sequence policy, resume orchestrator and the given children
     *
     * @param nodes the children
     */
    @SafeVarargs
    public Parallel(INode<E>... nodes) {
        this(Lists.<INode<E>>newArrayList(nodes));
    }

    /**
     * Creates a parallel node with sequence policy, resume orchestrator and the given children
     *
     * @param nodes the children
     */
    public Parallel(List<INode<E>> nodes) {
        this(Policy.Sequence, nodes);
    }

    /**
     * Creates a parallel node with the given policy, resume orchestrator and no children
     *
     * @param policy the policy
     */
    public Parallel(Policy policy) {
        this(policy, Lists.<INode<E>>newArrayList());
    }

    /**
     * Creates a parallel node with the given policy, resume orchestrator and the given children
     *
     * @param policy the policy
     * @param nodes  the children
     */
    @SafeVarargs
    public Parallel(Policy policy, INode<E>... nodes) {
        this(policy, Lists.<INode<E>>newArrayList(nodes));
    }

    /**
     * Creates a parallel node with the given policy, resume orchestrator and the given children
     *
     * @param policy the policy
     * @param nodes  the children
     */
    public Parallel(Policy policy, List<INode<E>> nodes) {
        this(policy, Orchestrator.Resume, nodes);
    }

    /**
     * Creates a parallel node with the given orchestrator, sequence policy and the given children
     *
     * @param orchestrator the orchestrator
     * @param nodes        the children
     */
    public Parallel(Orchestrator orchestrator, List<INode<E>> nodes) {
        this(Policy.Sequence, orchestrator, nodes);
    }

    /**
     * Creates a parallel node with the given orchestrator, sequence policy and the given children
     *
     * @param orchestrator the orchestrator
     * @param nodes        the children
     */
    @SafeVarargs
    public Parallel(Orchestrator orchestrator, INode<E>... nodes) {
        this(Policy.Sequence, orchestrator, Lists.<INode<E>>newArrayList(nodes));
    }

    /**
     * Creates a parallel node with the given orchestrator, policy and children
     *
     * @param policy       the policy
     * @param orchestrator the orchestrator
     * @param nodes        the children
     */
    public Parallel(Policy policy, Orchestrator orchestrator, List<INode<E>> nodes) {
        super(nodes);
        this.policy = policy;
        this.orchestrator = orchestrator;
        noRunningNodes = true;
    }

    @Override
    public void run() {
        orchestrator.execute(this);
    }

    @Override
    public void childSuccess(NodeContext<E> context) {
        lastResult = policy.onChildSuccess(this);
    }

    @Override
    public void childFail(NodeContext<E> context) {
        lastResult = policy.onChildFail(this);
    }

    @Override
    public void childRunning(NodeContext<E> context, INode<E> reporter) {
        noRunningNodes = false;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        noRunningNodes = true;
    }

//    @Override
//    protected INode<E> copyTo (INode<E> node) {
//        Parallel<E> parallel = (Parallel<E>)task;
//        parallel.policy = policy; // no need to clone since it is immutable
//        parallel.orchestrator = orchestrator; // no need to clone since it is immutable
//        return super.copyTo(task);
//    }

    public void resetAllChildren() {
        for (int i = 0, n = getChildCount(); i < n; i++) {
            INode<E> child = getChild(i);
            child.resetTask();
        }
    }

    /**
     * The enumeration of the child orchestrators supported by the {@link Parallel} node
     */
    public enum Orchestrator {
        /**
         * The default orchestrator - starts or resumes all children every single step
         */
        Resume() {
            @Override
            @SuppressWarnings({"rawtypes", "unchecked"})
            public void execute(Parallel<?> parallel) {
                parallel.noRunningNodes = true;
                parallel.lastResult = null;
                for (parallel.currentChildIndex = 0; parallel.getCurrentChildIndex() < parallel.getChildren().size(); parallel.currentChildIndex++) {
                    INode child = parallel.getChildren().get(parallel.getCurrentChildIndex());
                    if (child.getStatus() == BTStatus.RUNNING) {
                        child.run();
                    } else {
                        child.setControl(parallel);
                        child.start();
                        if (child.checkGuard(parallel)) {
                            child.run();
                        } else {
                            child.fail();
                        }
                    }

                    // Current child has finished either with success or fail
                    if (parallel.getLastResult() != null) {
                        parallel.cancelRunningChildren(parallel.isNoRunningNodes() ? parallel.getCurrentChildIndex() + 1 : 0);
                        if (parallel.getLastResult()) {
                            parallel.success();
                        } else {
                            parallel.fail();
                        }
                        return;
                    }
                }
                parallel.running();
            }
        },
        /**
         * Children execute until they succeed or fail but will not re-run until the parallel node has succeeded or failed
         */
        Join() {
            @Override
            @SuppressWarnings({"rawtypes", "unchecked"})
            public void execute(Parallel<?> parallel) {
                parallel.noRunningNodes = true;
                parallel.lastResult = null;
                for (parallel.currentChildIndex = 0; parallel.getCurrentChildIndex() < parallel.getChildren().size(); parallel.currentChildIndex++) {
                    INode child = parallel.getChildren().get(parallel.getCurrentChildIndex());

                    switch (child.getStatus()) {
                        case RUNNING:
                            child.run();
                            break;
                        case SUCCEEDED:
                        case FAILED:
                            break;
                        default:
                            child.setControl(parallel);
                            child.start();
                            if (child.checkGuard(parallel)) {
                                child.run();
                            } else {
                                child.fail();
                            }
                            break;
                    }

                    // Current child has finished either with success or fail
                    if (parallel.getLastResult() != null) {
                        parallel.cancelRunningChildren(parallel.isNoRunningNodes() ? parallel.getCurrentChildIndex() + 1 : 0);
                        parallel.resetAllChildren();
                        if (parallel.getLastResult()) {
                            parallel.success();
                        } else {
                            parallel.fail();
                        }
                        return;
                    }
                }
                parallel.running();
            }
        };

        /**
         * Called by parallel node each run
         *
         * @param parallel The {@link Parallel} node
         */
        public abstract void execute(Parallel<?> parallel);
    }

    @Override
    public void reset() {
        policy = Policy.Sequence;
        orchestrator = Orchestrator.Resume;
        noRunningNodes = true;
        lastResult = null;
        currentChildIndex = 0;
        super.reset();
    }

    /**
     * The enumeration of the policies supported by the {@link Parallel} node.
     */
    public enum Policy {
        /**
         * The sequence policy makes the {@link Parallel} node fail as soon as one child fails; if all children succeed, then the
         * parallel node succeeds. This is the default policy.
         */
        Sequence() {
            @Override
            public Boolean onChildSuccess(Parallel<?> parallel) {
                switch (parallel.orchestrator) {
                    case Join:
                        return parallel.isNoRunningNodes() && parallel.getChildren().get(parallel.getChildren().size() - 1).getStatus() == SUCCEEDED ? Boolean.TRUE : null;
                    case Resume:
                    default:
                        return parallel.isNoRunningNodes() && parallel.getCurrentChildIndex() == parallel.getChildren().size() - 1 ? Boolean.TRUE : null;
                }
            }

            @Override
            public Boolean onChildFail(Parallel<?> parallel) {
                return Boolean.FALSE;
            }
        },
        /**
         * The selector policy makes the {@link Parallel} node succeed as soon as one child succeeds; if all children fail, then the
         * parallel node fails.
         */
        Selector() {
            @Override
            public Boolean onChildSuccess(Parallel<?> parallel) {
                return Boolean.TRUE;
            }

            @Override
            public Boolean onChildFail(Parallel<?> parallel) {
                return parallel.isNoRunningNodes() && parallel.getCurrentChildIndex() == parallel.getChildren().size() - 1 ? Boolean.FALSE : null;
            }
        };

        /**
         * Called by parallel node each time one of its children succeeds.
         *
         * @param parallel the parallel node
         * @return {@code Boolean.TRUE} if parallel must succeed, {@code Boolean.FALSE} if parallel must fail and {@code null} if
         * parallel must keep on running.
         */
        public abstract Boolean onChildSuccess(Parallel<?> parallel);

        /**
         * Called by parallel node each time one of its children fails.
         *
         * @param parallel the parallel node
         * @return {@code Boolean.TRUE} if parallel must succeed, {@code Boolean.FALSE} if parallel must fail and {@code null} if
         * parallel must keep on running.
         */
        public abstract Boolean onChildFail(Parallel<?> parallel);

    }

    public boolean isNoRunningNodes() {
        return noRunningNodes;
    }

    public Boolean getLastResult() {
        return lastResult;
    }

    public int getCurrentChildIndex() {
        return currentChildIndex;
    }
}
