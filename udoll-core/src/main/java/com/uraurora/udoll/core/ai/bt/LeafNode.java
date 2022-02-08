package com.uraurora.udoll.core.ai.bt;


import com.uraurora.udoll.core.ai.bt.annotation.NodeConstraint;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-08 20:56
 * @description : 叶子节点类，行为树上叶子的抽象，主要职责是执行具体动作和返回条件，不可以包含任何子节点
 */
@NodeConstraint(minChildren = 0, maxChildren = 0)
public abstract class LeafNode<E> extends AbstractNode<E> {

    public LeafNode() {
    }

    /** This method contains the update logic of this leaf task. The actual implementation MUST return one of {@link BTStatus#RUNNING}
     * , {@link BTStatus#SUCCEEDED} or {@link BTStatus#FAILED}. Other return values will cause an {@code IllegalStateException}.
     * @return the status of this leaf task */
    public abstract BTStatus execute ();

    /** This method contains the update logic of this task. The implementation delegates the {@link #execute()} method. */
    @Override
    public void run () {
        BTStatus result = execute();
        if (result == null) {
            throw new IllegalStateException("Invalid status 'null' returned by the execute method");
        }
        switch (result) {
            case SUCCEEDED:
                success();
                return;
            case FAILED:
                fail();
                return;
            case RUNNING:
                running();
                return;
            default:
                throw new IllegalStateException("Invalid status '" + result.name() + "' returned by the execute method");
        }
    }

    /** 调用以下方法总是会抛出异常 {@code IllegalStateException} ，因为叶节点没有子节点 */
    @Override
    protected int internalAddChild(INode<E> child) {
        throw new IllegalStateException("A leaf task cannot have any children");
    }

    @Override
    public int getChildCount () {
        return 0;
    }

    @Override
    public INode<E> getChild (int i) {
        throw new IndexOutOfBoundsException("A leaf task can not have any child");
    }

    //<editor-fold desc="叶子无子节点">
    
    @Override
    public final void childRunning (NodeContext<E> context, INode<E> reporter) {
        throw new IllegalStateException("A leaf task cannot have any children");
    }

    @Override
    public final void childFail (NodeContext<E> context) {
        throw new IllegalStateException("A leaf task cannot have any children");
    }

    @Override
    public final void childSuccess (NodeContext<E> context) {
        throw new IllegalStateException("A leaf task cannot have any children");
    }
    //</editor-fold>
}
