package com.uraurora.udoll.core.ai.bt.leaf.action;

import com.google.common.collect.Queues;
import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.LeafNode;

import java.util.Deque;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-27 11:43
 * @description :
 */
public class PushToStack<E> extends LeafNode<E> {

    private final Deque<E> deque = Queues.newLinkedBlockingDeque();

    @Override
    public BTStatus execute() {
        return null;
    }

    public boolean push(E element){
        try {
            deque.push(element);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
