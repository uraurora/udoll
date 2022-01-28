package com.uraurora.udoll.core.ai.bt.decorator;

import com.uraurora.udoll.core.ai.bt.DecoratorNode;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-12 20:54
 * @description : 取反操作
 */
public class Invert<E> extends DecoratorNode<E> {

    /** Creates an {@code Invert} decorator with no child. */
    public Invert() {
    }

    /** Creates an {@code Invert} decorator with the given child.
     *
     * @param node the child node to wrap */
    public Invert(INode<E> node) {
        super(node);
    }

    @Override
    public void childSuccess(NodeContext<E> context) {
        super.childFail(context);
    }

    @Override
    public void childFail(NodeContext<E> context) {
        super.childSuccess(context);
    }

}
