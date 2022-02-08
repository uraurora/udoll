package com.uraurora.udoll.core.ai.bt.branch;


import com.google.common.collect.Lists;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.SingleRunningChildBranch;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-09 19:45
 * @description :
 */
public class Selector<E> extends SingleRunningChildBranch<E> {

    /**
     * Creates a {@code Selector} branch with no children.
     */
    public Selector() {
        super();
    }

    /**
     * Creates a {@code Selector} branch with the given children.
     *
     * @param nodes the children of this task
     */
    @SafeVarargs
    public Selector(INode<E>... nodes) {
        super(Lists.newArrayList(nodes));
    }

    /**
     * Creates a {@code Selector} branch with the given children.
     *
     * @param nodes the children of this task
     */
    public Selector(List<INode<E>> nodes) {
        super(nodes);
    }

    /**
     * 选择节点，只有当尝试所有节点运行都失败时，才返回失败
     *
     * @param context 运行失败的子节点
     */
    @Override
    public void childFail(NodeContext<E> context) {
        super.childFail(context);
        if (++currentChildIndex < children.size()) {
            run(); // Run next child
        } else {
            fail(); // All children processed, return failure status
        }
    }

    /**
     * 当一个子节点返回成功时，则该选择节点运行成功
     *
     * @param context 成功的子节点
     */
    @Override
    public void childSuccess(NodeContext<E> context) {
        super.childSuccess(context);
        // Return success status when a child says it succeeded
        success();
    }
}
