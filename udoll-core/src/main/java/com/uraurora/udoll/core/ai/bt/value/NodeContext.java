package com.uraurora.udoll.core.ai.bt.value;


import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.INode;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-30 14:20
 * @description : 节点的上下文信息
 */
public class NodeContext<E> {
    private final BTStatus status;

    private final INode<E> node;

    private final long timestamp;

    private NodeContext(NodeContextBuilder<E> builder){
        status = builder.status;
        node = builder.node;
        final LocalDateTime now = LocalDateTime.now();
        timestamp = now.toEpochSecond(ZoneOffset.of("+08:00"));
    }

    public static final class NodeContextBuilder<E> {
        private BTStatus status;
        private INode<E> node;

        private NodeContextBuilder() {
        }

        public NodeContextBuilder<E> withStatus(BTStatus status) {
            this.status = status;
            return this;
        }

        public NodeContextBuilder<E> withNode(INode<E> node) {
            this.node = node;
            return this;
        }

        public NodeContext<E> build() {
            return new NodeContext<>(this);
        }
    }

    public static <E> NodeContextBuilder<E> builder() {
        return new NodeContextBuilder<>();
    }

    public BTStatus getStatus() {
        return status;
    }

    public INode<E> getNode() {
        return node;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getLocalDateTime(){
        return LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.of("+08:00"));
    }
}

