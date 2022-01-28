package com.uraurora.udoll.core.ai.bt.decorator;

import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.LoopDecoratorNode;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-23 15:11
 * @description : 一个{@link UtilFail} 装饰节点从不会进入{@link BTStatus#FAILED} 失败状态
 *  装饰节点会持续运行子节点直到其子节点处于失败状态为止，当子节点失败时，该节点则返回成功
 *  注意：需要注意的是当一个总是成功的节点 如 {@link AlwaysSucceed}被该节点装饰时，会进入无限循环，导致无法预知的情况
 */
public class UtilFail<E> extends LoopDecoratorNode<E> {

    public UtilFail() {}

    public UtilFail(INode<E> child) {
        super(child);
    }

    @Override
    public void childFail(NodeContext<E> context) {
        success();
        loop = false;
    }

    @Override
    public void childSuccess(NodeContext<E> context) {
        loop = true;
    }
}
