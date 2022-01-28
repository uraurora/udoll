package com.uraurora.udoll.core.ai.bt.decorator;


import com.uraurora.udoll.core.ai.bt.DecoratorNode;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-10 21:06
 * @description : 除了运行状态，子节点返回成功或者失败，该节点总是失败
 */
public class AlwaysFail<E> extends DecoratorNode<E> {
    /**
     * 创建一个{@code AlwaysFail}，无子节点的装饰节点decorator
     */
    public AlwaysFail() {}

    /** 创建一个{@code AlwaysFail}，给定子节点的装饰节点decorator
     *
     * @param task 需要装饰的子节点
     */
    public AlwaysFail(INode<E> task) {
        super(task);
    }

    @Override
    public void childSuccess(NodeContext<E> context) {
        childFail(context);
    }
}
