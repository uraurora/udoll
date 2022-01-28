package com.uraurora.udoll.core.ai.bt.decorator;


import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.LoopDecoratorNode;
import com.uraurora.udoll.core.ai.bt.annotation.NodeAttribute;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-23 16:10
 * @description :
 */
public class Repeat<E> extends LoopDecoratorNode<E> {
    @NodeAttribute(name = "times")
    private int times;

    private int count;

    public Repeat(int times) {
        this.times = times;
    }

    public Repeat(INode<E> child, int times) {
        super(child);
        this.times = times;
    }

    public int getTimes(){
        return times;
    }

    @Override
    public void start () {
        count = times;
    }

    @Override
    public boolean condition () {
        return loop && count != 0;
    }

    @Override
    public void childSuccess (NodeContext<E> context) {
        if (count > 0) {
            count--;
        }
        if (count == 0) {
            super.childSuccess(context);
            loop = false;
        } else {
            loop = true;
        }
    }

    @Override
    public void childFail (NodeContext<E> context) {
        childSuccess(context);
    }

//    @Override
//    protected INode<E> copyTo (INode<E> task) {
//        Repeat<E> repeat = (Repeat<E>)task;
//        repeat.times = times; // no need to clone since it is immutable
//
//        return super.copyTo(task);
//    }

    @Override
    public void reset() {
        count = 0;
        times = 0;
        super.reset();
    }

}
