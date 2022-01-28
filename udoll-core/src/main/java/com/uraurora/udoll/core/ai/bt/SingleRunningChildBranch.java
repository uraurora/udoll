package com.uraurora.udoll.core.ai.bt;

import com.uraurora.udoll.core.ai.bt.value.NodeContext;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-09 19:35
 * @description : {@code SingleRunningChildBranch} 单运行子节点枝干类，是一个同一时刻只有一个运行子节点的枝干，但是本身子节点可以有很多
 * @param <E> 黑板对象类型
 */
public abstract class SingleRunningChildBranch<E> extends BranchNode<E> {
    /** 所包含的唯一一个处于运行状态 {@link BTStatus#RUNNING} 的子节点，如果没有子节点在运行则置为 {@code null}  */
    protected INode<E> runningChild;

    /** 正处于运行状态 {@link BTStatus#RUNNING} 的子节点的索引 */
    protected int currentChildIndex;

    /** 随机子节点数组. 如果其为 {@code null} 则该节点为确定性的，否则为随机选择一个合法子节点运行 */
    protected INode<E>[] randomChildren;

    /** 创建无子节点的 {@code SingleRunningChildBranch}  */
    protected SingleRunningChildBranch() {
        super();
    }

    /**
     * 创建指定子节点列表的 {@code SingleRunningChildBranch}
     * @param tasks 可为空的子节点列表 */
    protected SingleRunningChildBranch(List<INode<E>> tasks) {
        super(tasks);
    }

    /**
     * 因为约束为同一时刻只有一个运行的子节点，则子节点处于运行中时，本节点处于运行状态
     * @param context 需要再次运行的祖先节点
     * @param reporter the task that reports, usually one of this task's children
     */
    @Override
    public void childRunning(NodeContext<E> context, INode<E> reporter) {
        runningChild = context.getNode();
        // 当子节点运行时该节点则为运行状态
        running();
    }

    @Override
    public void childSuccess(NodeContext<E> context) {
        this.runningChild = null;
    }

    @Override
    public void childFail(NodeContext<E> context) {
        this.runningChild = null;
    }

    @Override
    public void run() {
        if (runningChild != null) {
            runningChild.run();
        } else {
            if (currentChildIndex < children.size()) {
                if (randomChildren != null) {
                    int last = children.size() - 1;
                    if (currentChildIndex < last) {
                        // Random swap
                        int otherChildIndex = currentChildIndex + ThreadLocalRandom.current().nextInt(last - currentChildIndex + 1);
                        INode<E> tmp = randomChildren[currentChildIndex];
                        randomChildren[currentChildIndex] = randomChildren[otherChildIndex];
                        randomChildren[otherChildIndex] = tmp;
                    }
                    runningChild = randomChildren[currentChildIndex];
                } else {
                    runningChild = children.get(currentChildIndex);
                }
                runningChild.setControl(this);
                runningChild.start();
                if (!runningChild.checkGuard(this)) {
                    runningChild.fail();
                } else {
                    run();
                }
            } else {
                // Should never happen; this case must be handled by subclasses in childXXX methods
            }
        }
    }

    @Override
    public void start() {
        this.currentChildIndex = 0;
        runningChild = null;
    }

    @Override
    protected void cancelRunningChildren(int startIndex) {
        super.cancelRunningChildren(startIndex);
        runningChild = null;
    }

    @Override
    public void resetTask() {
        super.resetTask();
        this.currentChildIndex = 0;
        this.runningChild = null;
        this.randomChildren = null;
    }

//    @Override
//    protected ITask<E> copyTo (ITask<E> task) {
//        SingleRunningChildBranch<E> branch = (SingleRunningChildBranch<E>)task;
//        branch.randomChildren = null;
//
//        return super.copyTo(task);
//    }

    @SuppressWarnings("unchecked")
    protected INode<E>[] createRandomChildren () {
        INode<E>[] rndChildren = new INode[children.size()];
        System.arraycopy(children.toArray(), 0, rndChildren, 0, children.size());
        return rndChildren;
    }

    @Override
    public void reset(){
        this.currentChildIndex = 0;
        this.runningChild = null;
        this.randomChildren = null;
        super.reset();
    }
}
