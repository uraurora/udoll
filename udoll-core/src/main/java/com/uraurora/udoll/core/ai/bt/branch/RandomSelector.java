package com.uraurora.udoll.core.ai.bt.branch;


import com.uraurora.udoll.core.ai.bt.INode;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-22 20:50
 * @description :
 */
public class RandomSelector<E> extends Selector<E> {
    /** Creates a {@code RandomSelector} branch with no children. */
    public RandomSelector () {
        super();
    }

    /** Creates a {@code RandomSelector} branch with the given children.
     *
     * @param nodes the children of this task */
    @SafeVarargs
    public RandomSelector(INode<E>... nodes) {
        super(nodes);
    }

    /** Creates a {@code RandomSelector} branch with the given children.
     *
     * @param nodes the children of this task */
    public RandomSelector(List<INode<E>> nodes) {
        super(nodes);
    }

    /**
     * 创建随机子节点数组，用来处理随机顺序
     */
    @Override
    public void start () {
        super.start();
        if (randomChildren == null) {
            randomChildren = createRandomChildren();
        }
    }
}
