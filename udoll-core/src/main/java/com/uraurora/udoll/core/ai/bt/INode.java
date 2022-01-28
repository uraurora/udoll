package com.uraurora.udoll.core.ai.bt;

import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author gaoxiaodong
 */
public interface INode<E> {

    //<editor-fold desc="属性设置">
    /** 返回这个任务属于的黑板对象
     * @throws IllegalStateException 如果该任务从未运行 */
    E getObject();

    /** 返回这个任务的守护任务 */
    INode<E> getGuard();

    /** 设置守护任务
     * @param guard 守护任务 */
    void setGuard(INode<E> guard);

    /** 返回这个任务的状态 */
    BTStatus getStatus();

    /**
     * 获取该任务所属的行为树 {@link BehaviourTree}
     * @return 行为树
     */
    BehaviourTree<E> getTree();

    /**
     * 设置该任任务的父级任务，并将父任务的行为树置为该任务所属的行为树
     * @param control 父任务 */
    void setControl(INode<E> control);
    //</editor-fold>

    //<editor-fold desc="子任务相关">
    /**
     * 添加一个子任务到该任务的子任务列表中，并通知行为树
     * @param child 需要添加的子任务
     * @return 这个子任务添加的索引
     * @throws IllegalStateException 如果添加不成功的原因 */
    int addChild(INode<E> child);
    /**
     *  返回该任务的子任务的个数
     * @return 返回该任务的子任务的个数 */
    int getChildCount();

    /** 返回子任务列表中指定索引位置的任务 */
    INode<E> getChild(int i);
    //</editor-fold>

    /**
     * 检查守护任务
     * @param control 父任务
     * @return 如果守护任务校验返回SUCCEEDED或者没有守护进程，返回 {@code true} ，否则返回 {@code false} .
     * @throws IllegalStateException 如果守护任务返回除了 {@link BTStatus#SUCCEEDED} 和
     *            {@link BTStatus#FAILED} 的其他状态则抛出 */
    boolean checkGuard(INode<E> control);

    /** 在该任务第一次开始运行前调用该方法一次 */
    void start();

    /** 这个方法会被以下方法调用 {@link #success()}, {@link #fail()} 或 {@link #cancel()}, 意味着该任务的状态已经各自被设置成了
     * 以下几种状态 {@link BTStatus#SUCCEEDED}, {@link BTStatus#FAILED} 或 {@link BTStatus#CANCELLED} */
    void end();

    //<editor-fold desc="节点运行逻辑相关">
    /**
     * 更新任务逻辑的方法，实际实现的方法由 {@link #running()} ,{@link #success()} 或 {@link #fail()} 的某个实现
     */
    void run();

    /**
     * 在 {@link #run()} 中调用，通知父任务该任务需要被再次运行，并且状态已经置为 {@link BTStatus#RUNNING}
     */
    void running();

    /**
     * 在 {@link #run()} 中调用，通知父任务该任务运行完成并且状态已经置为 {@link BTStatus#SUCCEEDED}
     */
    void success();

    /** 在 {@link #run()} 中调用，通知父任务该任务运行完成并且状态已经置为 {@link BTStatus#FAILED} */
    void fail();

    /**
     * 当一个子任务运行成功时会调用该方法
     * @param context 运行返回成功的那个任务上下文 */
    void childSuccess(NodeContext<E> context);

    /**
     * 当一个子任务运行失败时会调用该方法
     * @param context 运行返回失败的那个任务上下文 */
    void childFail(NodeContext<E> context);

    /**
     * 当该任务的祖先需要再次运行的时候，这个方法被调用
     * @param context 需要再次运行的祖先节点上下文
     * @param reporter the task that reports, usually one of this task's children */
    void childRunning(NodeContext<E> context, INode<E> reporter);

    /**
     * 取消该任务和该任务下的所有运行的子任务，这个任务只有在该任务运行状态 {@link BTStatus#RUNNING} 时才可以被调用
     */
    void cancel();
    //</editor-fold>

    /** 重置该任务和子任务的状态为 */
    void resetTask();

//    ITask<E> cloneTask ();

//     ITask<E> copyTo (ITask<E> task);

    /**
     * 判断节点的状态是否为成功
     * @return 是否成功
     */
    default boolean isSuccess(){
        return getStatus().equals(BTStatus.SUCCEEDED);
    }

    /**
     * 节点是否处于成功以外的状态
     * @return 是否处于成功以外的状态
     */
    default boolean isNotSuccess(){
        return !isSuccess();
    }

    /**
     * 节点是否是失败
     * @return 是否失败
     */
    default boolean isFailed(){
        return getStatus().equals(BTStatus.FAILED);
    }

    default boolean isNotFailed(){
        return !isFailed();
    }

    /**
     * 节点是否运行
     * @return 是否运行
     */
    default boolean isRunning(){
        return getStatus().equals(BTStatus.RUNNING);
    }

    default boolean isNotRunning(){
        return !isRunning();
    }

    /**
     * 节点是否取消
     * @return 是否取消
     */
    default boolean isCancelled(){
        return getStatus().equals(BTStatus.CANCELLED);
    }

    default boolean isNotCancelled(){
        return !isCancelled();
    }

    /**
     * 节点是否无效
     * @return 是否无效
     */
    default boolean isInvalid(){
        return getStatus().equals(BTStatus.INVALID);
    }

    /**
     * 节点是否是处有效状态以外的状态
     * @return 是否有效
     */
    default boolean isNotInvalid(){
        return !isInvalid();
    }

}
