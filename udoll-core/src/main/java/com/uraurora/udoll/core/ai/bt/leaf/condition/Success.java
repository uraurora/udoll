package com.uraurora.udoll.core.ai.bt.leaf.condition;


import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.LeafNode;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-08-09 15:09
 * @description :
 */
public class Success<E> extends LeafNode<E> {

    /** Creates a {@code Success} task. */
    public Success() {
    }

    /** Executes this {@code Success} task.
     * @return {@link BTStatus#SUCCEEDED}. */
    @Override
    public BTStatus execute() {
        return BTStatus.SUCCEEDED;
    }

//    @Override
//    protected Task<E> copyTo (Task<E> task) {
//        return task;
//    }

}
