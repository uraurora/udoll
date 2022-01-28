package com.uraurora.udoll.core.ai.bt.leaf.condition;


import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.LeafNode;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-09 15:08
 * @description :
 */
public class Failure<E> extends LeafNode<E> {
    /** Creates a {@code Failure} task. */
    public Failure() {
    }

    /** Executes this {@code Failure} task.
     * @return {@link BTStatus#FAILED}. */
    @Override
    public BTStatus execute() {
        return BTStatus.FAILED;
    }

//    @Override
//    protected AbstractTask<E> copyTo (AbstractTask<E> task) {
//        return task;
//    }
}
