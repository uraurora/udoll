package com.uraurora.udoll.core.ai.bt.decorator;


import com.uraurora.udoll.core.ai.bt.DecoratorNode;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-22 20:10
 * @description : 除了运行状态，子节点返回成功或者失败，该节点总是成功
 */
public class AlwaysSucceed<E> extends DecoratorNode<E> {
    /**
     * 创建一个{@code AlwaysFail}，无子节点的装饰节点decorator
     */
    public AlwaysSucceed() {
    }

    /** 创建一个{@code AlwaysFail}，给定子节点的装饰节点decorator
     *
     * @param task 需要装饰的子节点
     */
    public AlwaysSucceed(INode<E> task) {
        super(task);
    }

    @Override
    public void childFail(NodeContext<E> context) {
        childSuccess(context);
    }
}
