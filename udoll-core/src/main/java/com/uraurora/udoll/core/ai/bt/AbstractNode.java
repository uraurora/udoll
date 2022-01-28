package com.uraurora.udoll.core.ai.bt;

import com.uraurora.udoll.core.ai.bt.annotation.NodeConstraint;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-07 20:23
 * @description : 行为树节点的抽象类，主要包含状态更新、设置子节点等逻辑
 */
@NodeConstraint
public abstract class AbstractNode<E> implements INode<E> {

    // public static TaskCloner TASK_CLONER = null;

    /** 该节点的状态 */
    protected BTStatus status = BTStatus.INVALID;

    /** 该节点的父级节点 */
    protected INode<E> control;

    /** 该节点所属的行为树 */
    protected BehaviourTree<E> tree;

    /** The guard of this task */
    protected INode<E> guard;

    /**
     * 添加一个子节点到该节点的子节点列表中，并通知行为树
     * @param child 需要添加的子节点
     * @return 这个子节点添加的索引
     * @throws IllegalStateException 如果添加不成功的原因 */
    @Override
    public final int addChild(INode<E> child) {
        int index = addChildToTask(child);
        if (tree != null && tree.listeners != null) {
            tree.notifyChildAdded(this, index);
        }
        return index;
    }

    /**
     * 添加一个子节点到该节点的子节点列表中，实际执行的抽象方法
     * @param child 需要添加的孩子节点
     * @return 这个子节点添加的索引
     * @throws IllegalStateException 如果添加不成功的原因 */
    protected abstract int addChildToTask(INode<E> child);

    /**
     *  返回该节点的子节点的个数
     * @return 返回该节点的子节点的个数 */
    @Override
    public abstract int getChildCount ();

    /** 返回子节点列表中指定索引位置的节点 */
    @Override
    public abstract INode<E> getChild (int i);

    /** 返回这个节点属于的黑板对象
     * @throws IllegalStateException 如果该节点从未运行 */
    @Override
    public E getObject() {
        if (tree == null) {
            throw new IllegalStateException("This task has never run");
        }
        return tree.getObject();
    }

    /** 返回这个节点的守护节点 */
    @Override
    public INode<E> getGuard() {
        return guard;
    }

    /** 设置守护节点
     * @param guard 守护节点 */
    @Override
    public void setGuard(INode<E> guard) {
        this.guard = guard;
    }

    /** 返回这个节点的状态 */
    @Override
    public final BTStatus getStatus() {
        return status;
    }

    @Override
    public BehaviourTree<E> getTree() {
        return tree;
    }

    /**
     * 设置该任节点的父级节点，并将父节点的行为树置为该节点所属的行为树
     * @param control 父节点 */
    @Override
    public final void setControl(INode<E> control) {
        this.control = control;
        this.tree = control.getTree();
    }

    /**
     * 检查守护节点
     * @param control 父节点
     * @return 如果守护节点校验返回SUCCEEDED或者没有守护进程，返回 {@code true} ，否则返回 {@code false} .
     * @throws IllegalStateException 如果守护节点返回除了 {@link BTStatus#SUCCEEDED} 和
     *            {@link BTStatus#FAILED} 的其他状态则抛出 */
    @Override
    public boolean checkGuard(INode<E> control) {
        // 没有设置守护节点
        if (guard == null) {
            return true;
        }

        // 递归检测守护节点
        if (!guard.checkGuard(control)) {
            return false;
        }

        // 使用行为树的 guardEvaluator 来检查这个节点的守护节点
        guard.setControl(control.getTree().guardEvaluator);
        guard.start();
        guard.run();
        switch (guard.getStatus()) {
            case SUCCEEDED:
                return true;
            case FAILED:
                return false;
            default:
                throw new IllegalStateException("Illegal guard status '" + guard.getStatus() + "'. Guards must either succeed or fail in one step.");
        }
    }

    /** 在该节点第一次开始运行前调用该方法一次 */
    @Override
    public void start() {
    }

    /** 这个方法会被以下方法调用 {@link #success()}, {@link #fail()} 或 {@link #cancel()}, 意味着该节点的状态已经各自被设置成了
     * 以下几种状态 {@link BTStatus#SUCCEEDED}, {@link BTStatus#FAILED} 或 {@link BTStatus#CANCELLED} */
    @Override
    public void end() {
    }

    /**
     * 更新节点逻辑的方法，实际实现的方法由 {@link #running()} ,{@link #success()} 或 {@link #fail()} 的某个实现
     */
    @Override
    public abstract void run();

    /**
     * 在 {@link #run()} 中调用，通知父节点该节点需要被再次运行，并且状态已经置为 {@link BTStatus#RUNNING}
     */
    @Override
    public final void running() {
        BTStatus previousStatus = status;
        status = BTStatus.RUNNING;
        if (tree.listeners != null && tree.listeners.size() > 0) {
            tree.notifyStatusUpdated(this, previousStatus);
        }
        if (control != null) {
            final NodeContext<E> context = NodeContext.<E>builder()
                    .withNode(this)
                    .withStatus(status)
                    .build();
            control.childRunning(context, this);
        }
    }

    /**
     * 在 {@link #run()} 中调用，通知父节点该节点运行完成并且状态已经置为 {@link BTStatus#SUCCEEDED}
     */
    @Override
    public final void success() {
        BTStatus previousStatus = status;
        status = BTStatus.SUCCEEDED;
        if (tree.listeners != null && tree.listeners.size() > 0) {
            tree.notifyStatusUpdated(this, previousStatus);
        }
        end();
        if (control != null) {
            final NodeContext<E> context = NodeContext.<E>builder()
                    .withNode(this)
                    .withStatus(status)
                    .build();
            control.childSuccess(context);
        }
    }

    /** 在 {@link #run()} 中调用，通知父节点该节点运行完成并且状态已经置为 {@link BTStatus#FAILED} */
    @Override
    public final void fail() {
        BTStatus previousStatus = status;
        status = BTStatus.FAILED;
        if (tree.listeners != null && tree.listeners.size() > 0) {
            tree.notifyStatusUpdated(this, previousStatus);
        }
        end();
        if (control != null) {
            final NodeContext<E> context = NodeContext.<E>builder()
                    .withNode(this)
                    .withStatus(status)
                    .build();
            control.childFail(context);
        }
    }

    /**
     * 当一个子节点运行成功时会调用该方法
     * @param context 运行返回成功的那个节点 */
    @Override
    public abstract void childSuccess(NodeContext<E> context);

    /**
     * 当一个子节点运行失败时会调用该方法
     * @param context 运行返回失败的那个节点 */
    @Override
    public abstract void childFail(NodeContext<E> context);

    /** This method will be called when one of the ancestors of this task needs to run again
     * 当该节点的祖先需要再次运行的时候，这个方法被调用
     * @param context 需要再次运行的祖先节点
     * @param reporter the task that reports, usually one of this task's children */
    @Override
    public abstract void childRunning(NodeContext<E> context, INode<E> reporter);

    /**
     * 取消该节点和该节点下的所有运行的子节点，这个节点只有在该节点运行状态 {@link BTStatus#RUNNING} 时才可以被调用
     */
    @Override
    public final void cancel() {
        if (isRunning()) {
            cancelRunningChildren(0);
            BTStatus previousStatus = status;
            status = BTStatus.CANCELLED;
            if (tree.listeners != null && tree.listeners.size() > 0) {
                tree.notifyStatusUpdated(this, previousStatus);
            }
            end();
        }
    }

    /** 从指定的索引开始，取消运行的子节点
     * @param startIndex 指定的索引 */
    protected void cancelRunningChildren(int startIndex) {
        for (int i = startIndex, n = getChildCount(); i < n; i++) {
            getChild(i).cancel();
        }
    }

    /** 重置该节点和子节点的状态为 {@link BTStatus#INVALID} 并将父节点和所属行为树置空 */
    @Override
    public void resetTask() {
        cancel();
        for (int i = 0, n = getChildCount(); i < n; i++) {
            getChild(i).resetTask();
        }
        status = BTStatus.INVALID;
        tree = null;
        control = null;
    }

//    @SuppressWarnings("unchecked")
//    public ITask<E> cloneTask () {
//        if (TASK_CLONER != null) {
//            try {
//                return TASK_CLONER.cloneTask(this);
//            } catch (Throwable t) {
//                throw new TaskCloneException(t);
//            }
//        }
//        try {
//            ITask<E> clone = copyTo(ClassReflection.newInstance(this.getClass()));
//            clone.guard = guard == null ? null : guard.cloneTask();
//            return clone;
//        } catch (ReflectionException e) {
//            throw new TaskCloneException(e);
//        }
//    }

    // protected abstract ITask<E> copyTo (ITask<E> task);

    public void reset() {
        control = null;
        guard = null;
        status = BTStatus.INVALID;
        tree = null;
    }

}

