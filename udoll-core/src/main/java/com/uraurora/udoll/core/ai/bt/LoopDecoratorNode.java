package com.uraurora.udoll.core.ai.bt;


import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-12 16:06
 * @description :
 */
public abstract class LoopDecoratorNode<E> extends DecoratorNode<E> {

    /** Whether the {@link #run()} method must keep looping or not. */
    protected boolean loop;

    /** Creates a loop decorator with no child task. */
    public LoopDecoratorNode() {
    }

    /** Creates a loop decorator that wraps the given task.
     *
     * @param child the task that will be wrapped */
    public LoopDecoratorNode(INode<E> child) {
        super(child);
    }

    /** Whether the {@link #run()} method must keep looping or not.
     * @return {@code true} if it must keep looping; {@code false} otherwise. */
    public boolean condition () {
        return loop;
    }

    @Override
    public void run () {
        loop = true;
        while (condition()) {
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
    }

    @Override
    public void childRunning(NodeContext<E> context, INode<E> reporter) {
        super.childRunning(context, reporter);
        loop = false;
    }

    @Override
    public void reset() {
        loop = false;
        super.reset();
    }

}
