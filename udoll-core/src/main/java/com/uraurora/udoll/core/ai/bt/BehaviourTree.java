package com.uraurora.udoll.core.ai.bt;

import com.google.common.collect.Lists;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

import java.util.List;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-07 20:29
 * @description : 行为树
 */
public class BehaviourTree<E> extends AbstractNode<E> {
    /**
     * 根节点
     */
    private INode<E> root;
    /**
     * 包装的对象
     */
    private E object;
    /**
     * 守护任务校验器
     */
    GuardEvaluator<E> guardEvaluator;
    /**
     * 监听者
     */
    public List<Listener<E>> listeners;

    /** Creates a {@code BehaviorTree} with no root task and no blackboard object. Both the root task and the blackboard object must
     * be set before running this behavior tree, see {@link #addChild(INode) addChild()} and {@link #setObject(Object) setObject()}
     * respectively. */
    public BehaviourTree () {
        this(null, null);
    }

    /** Creates a behavior tree with a root task and no blackboard object. Both the root task and the blackboard object must be set
     * before running this behavior tree, see {@link #addChild(INode) addChild()} and {@link #setObject(Object) setObject()}
     * respectively.
     *
     * @param root the root task of this tree. It can be {@code null}. */
    public BehaviourTree (INode<E> root) {
        this(root, null);
    }

    /** Creates a behavior tree with a root task and a blackboard object. Both the root task and the blackboard object must be set
     * before running this behavior tree, see {@link #addChild(INode) addChild()} and {@link #setObject(Object) setObject()}
     * respectively.
     *
     * @param root the root task of this tree. It can be {@code null}.
     * @param object the blackboard. It can be {@code null}. */
    public BehaviourTree (INode<E> root, E object) {
        this.root = root;
        this.object = object;
        this.tree = this;
        this.guardEvaluator = new GuardEvaluator<E>(this);
    }

    /** Returns the blackboard object of this behavior tree. */
    @Override
    public E getObject () {
        return object;
    }

    /** Sets the blackboard object of this behavior tree.
     *
     * @param object the new blackboard */
    public void setObject (E object) {
        this.object = object;
    }

    /** This method will add a child, namely the root, to this behavior tree.
     *
     * @param child the root task to add
     * @return the index where the root task has been added (always 0).
     * @throws IllegalStateException if the root task is already set. */
    @Override
    protected int internalAddChild(INode<E> child) {
        if (this.root != null) {
            throw new IllegalStateException("A behavior tree cannot have more than one root task");
        }
        this.root = child;
        return 0;
    }

    @Override
    public int getChildCount () {
        return root == null ? 0 : 1;
    }

    @Override
    public INode<E> getChild (int i) {
        if (i == 0 && root != null) {
            return root;
        }
        throw new IndexOutOfBoundsException("index can't be >= size: " + i + " >= " + getChildCount());
    }

    @Override
    public void childRunning (NodeContext<E> context, INode<E> reporter) {
        running();
    }

    @Override
    public void childFail (NodeContext<E> context) {
        fail();
    }

    @Override
    public void childSuccess (NodeContext<E> context) {
        success();
    }

    /** This method should be called when game entity needs to make decisions: call this in game loop or after a fixed time slice if
     * the game is real-time, or on entity's turn if the game is turn-based */
    public void step () {
        if (root.isRunning()) {
            root.run();
        } else {
            root.setControl(this);
            root.start();
            if (root.checkGuard(this)) {
                root.run();
            } else {
                root.fail();
            }
        }
    }

    @Override
    public void run () {
    }

    @Override
    public void resetTask () {
        super.resetTask();
        tree = this;
    }

//    @Override
//    protected ITask<E> copyTo (ITask<E> task) {
//        BehaviourTree<E> tree = (BehaviourTree<E>)task;
//        tree.rootTask = rootTask.cloneTask();
//
//        return task;
//    }

    public void addListener (Listener<E> listener) {
        if (listeners == null) {
            listeners = Lists.newArrayList();
        }
        listeners.add(listener);
    }

    public void removeListener (Listener<E> listener) {
        if (listeners != null) {
            listeners.remove(listener);
        }
    }

    public void clearListeners() {
        if (listeners != null) {
            listeners.clear();
        }
    }

    public void notifyStatusUpdated (INode<E> task, BTStatus previousStatus) {
        for (Listener<E> listener : listeners) {
            listener.statusUpdated(task, previousStatus);
        }
    }

    public void notifyChildAdded (INode<E> task, int index) {
        for (Listener<E> listener : listeners) {
            listener.childAdded(task, index);
        }
    }

    @Override
    public void reset() {
        clearListeners();
        this.root = null;
        this.object = null;
        this.listeners = null;
        super.reset();
    }

    private static final class GuardEvaluator<E> extends AbstractNode<E> {

        // No argument constructor useful for Kryo serialization
        @SuppressWarnings("unused")
        public GuardEvaluator () {
        }

        public GuardEvaluator (BehaviourTree<E> tree) {
            this.tree = tree;
        }


        @Override
        protected int internalAddChild(INode<E> child) {
            return 0;
        }

        @Override
        public int getChildCount () {
            return 0;
        }

        @Override
        public INode<E> getChild (int i) {
            return null;
        }

        @Override
        public void run () {
        }

        @Override
        public void childSuccess (NodeContext<E> context) {
        }

        @Override
        public void childFail (NodeContext<E> context) {
        }

        @Override
        public void childRunning (NodeContext<E> context, INode<E> reporter) {
        }

//        @Override
//        protected ITask<E> copyTo (ITask<E> task) {
//            return null;
//        }

    }

    /** The listener interface for receiving task events. The class that is interested in processing a task event implements this
     * interface, and the object created with that class is registered with a behavior tree, using the
     * {@link BehaviourTree#addListener(Listener)} method. When a task event occurs, the corresponding method is invoked.
     *
     * @param <E> type of the blackboard object that tasks use to read or modify game state
     *
     * @author davebaol */
    public interface Listener<E> {

        /** This method is invoked when the task status is set. This does not necessarily mean that the status has changed.
         * @param task the task whose status has been set
         * @param previousStatus the task's status before the update */
        void statusUpdated(INode<E> task, BTStatus previousStatus);

        /** This method is invoked when a child task is added to the children of a parent task.
         * @param task the parent task of the newly added child
         * @param index the index where the child has been added */
        void childAdded(INode<E> task, int index);
    }
}
