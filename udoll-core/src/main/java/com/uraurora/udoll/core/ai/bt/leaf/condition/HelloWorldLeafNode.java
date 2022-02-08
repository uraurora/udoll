package com.uraurora.udoll.core.ai.bt.leaf.condition;

import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.LeafNode;

/**
 * @author : gaoxiaodong04
 * @program : udoll
 * @date : 2022-02-08 15:44
 * @description :
 */
public class HelloWorldLeafNode extends LeafNode<String> {
    @Override
    public BTStatus execute() {
        try {
            System.out.println("hello world!");
            return BTStatus.SUCCEEDED;
        } catch (Exception e) {
            return BTStatus.FAILED;
        }
    }
}
