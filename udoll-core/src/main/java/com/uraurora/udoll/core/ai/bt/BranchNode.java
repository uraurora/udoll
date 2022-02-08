package com.uraurora.udoll.core.ai.bt;

import com.google.common.collect.Lists;
import com.uraurora.udoll.core.ai.bt.annotation.NodeConstraint;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-08 20:56
 * @description : 枝干类，是行为树上枝干的抽象，主要职责是维护子分支和叶子节点的启动和运行逻辑
 * @param <E> 维护的黑板对象类型
 */
@NodeConstraint(minChildren = 1)
public abstract class BranchNode<E> extends AbstractNode<E> {

    /** 分支包含的子任务列表 */
    protected List<INode<E>> children;

    /** 创建无子任务的枝干 */
    protected BranchNode() {
        this(Lists.newArrayList());
    }

    /**
     * 创建包含给定一个子任务的枝干
     * @param tasks 子任务列表，可以为空列表 */
    protected BranchNode(List<INode<E>> tasks) {
        this.children = tasks;
    }

    @Override
    protected int internalAddChild(INode<E> child) {
        children.add(child);
        return children.size() - 1;
    }

    @Override
    public int getChildCount () {
        return children.size();
    }

    @Override
    public INode<E> getChild (int i) {
        return children.get(i);
    }

//    @Override
//    protected ITask<E> copyTo (ITask<E> task) {
//        BranchTask<E> branch = (BranchTask<E>)task;
//        if (children != null) {
//            for (int i = 0; i < children.size(); i++) {
//                branch.children.add(children.get(i).cloneTask());
//            }
//        }
//
//        return task;
//    }

    @Override
    public void reset() {
        children.clear();
        super.reset();
    }

    public List<INode<E>> getChildren() {
        return children;
    }
}
