package com.uraurora.udoll.core.ai.bt.decorator;

import com.google.common.collect.Queues;
import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.DecoratorNode;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

import java.util.Deque;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-28 20:33
 * @description :
 */
public class StatusMemoize<E> extends DecoratorNode<E> {
    /**
     * 保存该节点装饰的节点历史执行状态的栈
     */
    private final Deque<BTStatus> stack = Queues.newArrayDeque();

    public StatusMemoize() {
    }

    public StatusMemoize(INode<E> child) {
        super(child);
    }

    public Deque<BTStatus> getStack(){
        return stack;
    }

    @Override
    public void childRunning(NodeContext<E> context, INode<E> reporter) {
        stack.addLast(BTStatus.RUNNING);
        running();
    }

    @Override
    public void childFail(NodeContext<E> context) {
        stack.addLast(BTStatus.FAILED);
        fail();
    }

    @Override
    public void childSuccess(NodeContext<E> context) {
        stack.addLast(BTStatus.SUCCEEDED);
        success();
    }

    public static class ExecuteRecord{

    }


}
