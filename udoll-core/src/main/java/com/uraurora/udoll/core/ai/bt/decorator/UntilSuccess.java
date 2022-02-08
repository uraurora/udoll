package com.uraurora.udoll.core.ai.bt.decorator;


import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.LoopDecoratorNode;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-22 20:18
 * @description :一个 {@link UntilSuccess} 装饰节点从不会进入{@link BTStatus#FAILED} 失败状态
 * 该节点会持续运行子节点直到其子节点处于成功状态为止，当子节点成功时，该节点则返回成功
 * 注意：需要注意的是当一个总是失败的节点 如 {@link AlwaysFail} 被该节点装饰时，会进入无限循环，导致无法预知的情况
 */
public class UntilSuccess<E> extends LoopDecoratorNode<E> {
    public UntilSuccess() {}

    /** Creates an {@code UntilSuccess} decorator with the given child.
     *
     * @param node the child node to wrap */
    public UntilSuccess(INode<E> node) {
        super(node);
    }

    @Override
    public void childSuccess(NodeContext<E> context) {
        success();
        loop = false;
    }

    @Override
    public void childFail (NodeContext<E> context) {
        loop = true;
    }
}
