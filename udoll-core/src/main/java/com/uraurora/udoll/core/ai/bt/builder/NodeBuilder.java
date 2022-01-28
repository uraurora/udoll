package com.uraurora.udoll.core.ai.bt.builder;


import com.uraurora.udoll.core.ai.bt.BehaviourTree;
import com.uraurora.udoll.core.ai.bt.INode;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-30 17:06
 * @description :
 */
public class NodeBuilder<E> implements INodeAnnotation<E>, ITreeBuildAnnotation<BehaviourTree<E>> {

    private final Tree<INode<E>> tree;

    private final BehaviourTreeBuilder<E> behaviourTreeBuilder;

    public NodeBuilder(INode<E> root, BehaviourTreeBuilder<E> behaviourTreeBuilder){
        this.tree = new Tree<>(root);
        this.behaviourTreeBuilder = behaviourTreeBuilder;
    }

    @Override
    public INodeAnnotation<E> withChild(INode<E> node) {
        tree.addChild(node);
        return this;
    }

    @Override
    public INodeAnnotation<E> withChildren(List<INode<E>> nodes) {
        tree.addChildren(nodes);
        return this;
    }

    @Override
    public INodeAnnotation<E> next(INode<E> node) {
        tree.addNext(node);
        return this;
    }

    @Override
    public INodeAnnotation<E> back() {
        tree.back();
        return this;
    }

    @Override
    public INodeAnnotation<E> childAt(int index) throws IndexOutOfBoundsException {
        tree.childAt(index);
        return this;
    }

    @Override
    public BehaviourTree<E> build() {
        return behaviourTreeBuilder.build();
    }
}
