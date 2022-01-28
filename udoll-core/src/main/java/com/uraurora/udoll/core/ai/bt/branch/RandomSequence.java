package com.uraurora.udoll.core.ai.bt.branch;

import com.uraurora.udoll.core.ai.bt.INode;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-23 10:34
 * @description :/** {@link RandomSequence} 随机序列器会以一个随机的顺序运行其子节点
 *  {@link Sequence} 序列器适用于那些要求子节点顺序执行的场景，比如通话过程，连接->通话->挂断
 *  而随机序列器适用于那些无关顺序的逻辑，比如点燃柴火的前置动作，获取木柴和获取火苗这两个动作顺序无关紧要
 */
public class RandomSequence<E> extends Sequence<E> {
    /** 创建一个没有子节点的随机序列器节点 */
    public RandomSequence () {
        super();
    }

    /**
     * 创建一个具有指定子节点列表的随机序列器节点
     */
    public RandomSequence(List<INode<E>> nodes) {
        super(nodes);
    }

    /**
     * 创建一个具有指定子节点列表的随机序列器节点
     */
    @SafeVarargs
    public RandomSequence(INode<E>... nodes) {
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
