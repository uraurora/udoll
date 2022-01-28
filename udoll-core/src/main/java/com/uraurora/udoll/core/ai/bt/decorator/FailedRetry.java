package com.uraurora.udoll.core.ai.bt.decorator;

import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.LoopDecoratorNode;
import com.uraurora.udoll.core.ai.bt.annotation.NodeAttribute;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-25 14:16
 * @description : {@link FailedRetry}装饰节点是一个对重试机制封装的节点
 */
public class FailedRetry<E> extends LoopDecoratorNode<E> {

    @NodeAttribute(name = "limitTime")
    private final long limitTime;

    @NodeAttribute(name = "timeUnit")
    private final TimeUnit timeUnit;

    @NodeAttribute(name = "retryTimes")
    private final int retryTimes;

    private final AtomicInteger count;

    public FailedRetry() {
        super();
        retryTimes = 1;
        limitTime = -1;
        timeUnit = TimeUnit.SECONDS;
        count = new AtomicInteger(1);
    }

    public FailedRetry(INode<E> child, int retryTimes, long limitTime, TimeUnit timeUnit) {
        super(child);
        this.retryTimes = retryTimes;
        this.limitTime = limitTime;
        this.timeUnit = timeUnit;
        count = new AtomicInteger(retryTimes);
    }

    public FailedRetry(INode<E> child, int retryTimes){
        this(child, retryTimes, -1, TimeUnit.SECONDS);
    }

    @Override
    public boolean condition() {
        return loop && child.isNotSuccess() && count.get() > 0;
    }

    public int getRetryTimes(){
        return retryTimes - count.get();
    }

    @Override
    public void childFail(NodeContext<E> context) {
        count.decrementAndGet();
    }

    @Override
    public void childRunning(NodeContext<E> context, INode<E> reporter) {
        super.childRunning(context, reporter);
    }

    @Override
    public void resetTask() {
        super.resetTask();
        count.getAndSet(retryTimes);
    }
}
