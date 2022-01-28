package com.uraurora.udoll.core.ai.bt.leaf.condition;

import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.LeafNode;

import java.time.Instant;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-23 11:43
 * @description : 在一段时间内返回成功，超个这个时间则返回失败，延时时长由timeout设置，单位为毫秒
 */
public class Wait<E> extends LeafNode<E> {

    private final float timeout;

    private Instant startTime;

    public Wait(float timeout) {
        this.timeout = timeout;
    }

    @Override
    public BTStatus execute() {
        return Instant.now().toEpochMilli() - startTime.toEpochMilli() < timeout?
                BTStatus.RUNNING : BTStatus.SUCCEEDED;
    }

    @Override
    public void start() {
        this.startTime = Instant.now();
    }

}
