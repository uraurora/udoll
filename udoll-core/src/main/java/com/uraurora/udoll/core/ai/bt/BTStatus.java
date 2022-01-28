package com.uraurora.udoll.core.ai.bt;

/**
 * 节点状态枚举
 * @author gaoxiaodong
 */
public enum BTStatus {
    /** 意味着节点可能从未启动或者被运行 */
    INVALID,
    /** 节点需要被重新运行一次 */
    RUNNING,
    /** 节点运行，返回运行失败 */
    FAILED,
    /** 节点运行，返回运行成功 */
    SUCCEEDED,
    /** 节点的运行任务被来自祖先的某个节点取消 */
    CANCELLED,

    ;
}
