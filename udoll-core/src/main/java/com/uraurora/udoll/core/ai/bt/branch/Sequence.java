package com.uraurora.udoll.core.ai.bt.branch;

import com.google.common.collect.Lists;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.SingleRunningChildBranch;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-09 19:48
 * @description :
 */
public class Sequence<E> extends SingleRunningChildBranch<E> {

    /**
     * Creates a {@code Sequence} branch with no children.
     */
    public Sequence() {
        super();
    }

    /**
     * Creates a {@code Sequence} branch with the given children.
     *
     * @param nodes the children of this task
     */
    public Sequence(List<INode<E>> nodes) {
        super(nodes);
    }

    /**
     * Creates a {@code Sequence} branch with the given children.
     *
     * @param nodes the children of this task
     */
    @SafeVarargs
    public Sequence(INode<E>... nodes) {
        super(Lists.newArrayList(nodes));
    }

    /**
     * 序列节点，只有当所有节点都成功运行，这个节点才返回成功
     *
     * @param context 成功的子节点
     */
    @Override
    public void childSuccess(NodeContext<E> context) {
        super.childSuccess(context);
        if (++currentChildIndex < children.size()) {
            // Run next child
            run();
        } else {
            // All children processed, return success status
            success();
        }
    }

    @Override
    public void childFail(NodeContext<E> context) {
        super.childFail(context);
        // Return failure status when a child says it failed
        fail();
    }

}
