package com.uraurora.udoll.core.ai.bt.builder;

import com.google.common.collect.Lists;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-30 16:17
 * @description :
 */
public class Tree<E> {
    
    private final TreeNode<E> root;

    private TreeNode<E> currentNode;

    public Tree(E root){
        this.root = new TreeNode<>();
        this.root.setFather(this.root);
        this.currentNode = this.root;
    }

    public TreeNode<E> addChild(E child){
        TreeNode<E> c = new TreeNode<>(child, currentNode);
        currentNode.addChildNode(c);
        currentNode = c;
        return currentNode;
    }

    public TreeNode<E> addChildren(List<E> children){
        currentNode.addChildren(children);
        return currentNode;
    }

    public TreeNode<E> addNext(E node){
        final TreeNode<E> father = this.currentNode.getFather();
        TreeNode<E> c = new TreeNode<>(node, father);
        father.addChildNode(c);
        currentNode = c;
        return currentNode;
    }

    public TreeNode<E> back(){
        return currentNode.father;
    }

    public Tree<E> childAt(int index) throws IndexOutOfBoundsException{
        currentNode = currentNode.children.get(index);
        return this;
    }

    public TreeNode<E> root(){
        return root;
    }

    public void backToRootWithAction(Consumer<? super E> action){
        while(currentNode.father != root){
            action.accept(currentNode.data);
            currentNode = currentNode.father;
        }
    }


    public static class TreeNode<E>{

        private E data;

        private List<TreeNode<E>> children = Lists.newArrayList();
        
        private TreeNode<E> father;

        public TreeNode(){}

        public TreeNode(E data, TreeNode<E> father) {
            this.data = data;
            this.father = father;
        }

        public TreeNode(E data, TreeNode<E> father, List<TreeNode<E>> children) {
            this.data = data;
            this.children.addAll(children);
            this.father = father;
        }

        public TreeNode<E> setChildren(List<TreeNode<E>> children) {
            this.children = children;
            return this;
        }

        public TreeNode<E> setFather(TreeNode<E> father) {
            this.father = father;
            return this;
        }

        public List<TreeNode<E>> getChildren() {
            return children;
        }

        public TreeNode<E> getFather() {
            return father;
        }

        public E getData() {
            return data;
        }

        public TreeNode<E> addChildNode(TreeNode<E> child){
            this.children.add(child);
            return this;
        }

        public TreeNode<E> addChild(E child){
            final TreeNode<E> node = new TreeNode<>(child, this);
            children.add(node);
            return this;
        }

        public TreeNode<E> addChildrenNodes(List<TreeNode<E>> children){
            for (TreeNode<E> child : children) {
                addChildNode(child);
            }
            return this;
        }

        public TreeNode<E> addChildren(List<E> children){
            for (E child : children) {
                addChild(child);
            }
            return this;
        }
    }

}
