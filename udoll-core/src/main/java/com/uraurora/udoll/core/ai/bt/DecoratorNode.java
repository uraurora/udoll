package com.uraurora.udoll.core.ai.bt;


import com.uraurora.udoll.core.ai.bt.annotation.NodeConstraint;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-09 15:39
 * @description :
 */
@NodeConstraint(minChildren = 1, maxChildren = 1)
public abstract class DecoratorNode<E> extends AbstractNode<E> {
    /** The child task wrapped by this decorator */
    protected INode<E> child;

    /** Creates a decorator with no child task. */
    public DecoratorNode() {}

    /** Creates a decorator that wraps the given task.
     *
     * @param child the task that will be wrapped */
    public DecoratorNode(INode<E> child) {
        this.child = child;
    }

    @Override
    protected int addChildToTask (INode<E> child) {
        if (this.child != null) {
            throw new IllegalStateException("A decorator task cannot have more than one child");
        }
        this.child = child;
        return 0;
    }

    @Override
    public int getChildCount () {
        return child == null ? 0 : 1;
    }

    @Override
    public INode<E> getChild (int i) {
        if (i == 0 && child != null) {
            return child;
        }
        throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + getChildCount());
    }

    @Override
    public void run () {
        if (child.isRunning()) {
            child.run();
        } else {
            child.setControl(this);
            child.start();
            if (child.checkGuard(this)) {
                child.run();
            } else {
                child.fail();
            }
        }
    }

    @Override
    public void childRunning(NodeContext<E> context, INode<E> reporter) {
        running();
    }

    @Override
    public void childFail(NodeContext<E> context) {
        fail();
    }

    @Override
    public void childSuccess(NodeContext<E> context) {
        success();
    }

//    @Override
//    protected ITask<E> copyTo (ITask<E> task) {
//        if (this.child != null) {
//            DecoratorTask<E> decorator = (DecoratorTask<E>)task;
//            decorator.child = this.child.cloneTask();
//        }
//
//        return task;
//    }

    @Override
    public void reset() {
        child = null;
        super.reset();
    }
}
