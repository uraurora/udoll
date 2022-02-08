package com.uraurora.udoll.core.ai.bt.builder;


import com.uraurora.udoll.core.ai.bt.BehaviourTree;
import com.uraurora.udoll.core.ai.bt.INode;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-30 15:32
 * @description :
 */
public class BehaviourTreeBuilder<E> {

    private NodeBuilder<E> tree;

    private BehaviourTree<E> bt;

    private BehaviourTreeBuilder(){}

    public static <E> BehaviourTreeBuilder<E> builder(){
        return new BehaviourTreeBuilder<>();
    }

    public BehaviourTree<E> build(){
        return bt;
    }

    public INodeAnnotation<E> root(INode<E> root){
        tree = new NodeBuilder<>(root, this);
        return tree;
    }

}
