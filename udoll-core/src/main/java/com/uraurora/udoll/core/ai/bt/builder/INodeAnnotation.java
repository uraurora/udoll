package com.uraurora.udoll.core.ai.bt.builder;


import com.uraurora.udoll.core.ai.bt.INode;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-30 15:49
 * @description :
 */
public interface INodeAnnotation<E> {

    /**
     * 为当前节点添加子节点
     * @param node 要添加的子节点
     * @return
     */
    INodeAnnotation<E> withChild(INode<E> node);

    /**
     * 为当前节点添加子节点集合
     * 需要注意的是这些子节点添加时都是没有自己的子节点的
     * @param nodes 子节点集合
     * @return
     */
    INodeAnnotation<E> withChildren(List<INode<E>> nodes);

    /**
     * next指的是下一个同级的节点，指从当前节点创建自己的兄弟节点（统一父节点的不同节点）
     * @param node 兄弟节点
     * @return
     */
    INodeAnnotation<E> next(INode<E> node);

    /**
     * 退回到当前节点的父节点处
     * @return
     */
    INodeAnnotation<E> back();

    /**
     * 获取当前节点的index位置的子节点，如果超过索引会报出{@link IndexOutOfBoundsException}异常
     * @return
     */
    INodeAnnotation<E> childAt(int index) throws IndexOutOfBoundsException;

}
