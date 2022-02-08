package com.uraurora.udoll.core.ai.bt.decorator;

import com.uraurora.udoll.core.ai.bt.DecoratorNode;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.annotation.NodeAttribute;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author : gaoxiaodong04
 * @program : udoll
 * @date : 2022-02-08 19:55
 * @description : filter node
 */
public class Filter<E> extends DecoratorNode<E> {

    @NodeAttribute
    private final Function<E, Boolean> condition;

    public Filter(Function<E, Boolean> condition) {
        this.condition = condition;
    }

    public Filter(INode<E> child, Function<E, Boolean> condition) {
        super(child);
        this.condition = condition;
    }

    @Override
    public void run() {
        final Boolean flag = condition.apply(getObject());
        if (flag != null && flag) {
            if (child.isRunning()) {
                child.run();
            } else {
                child.setControl(this);
                child.start();
                if (child.checkGuard(this)) {
                    child.run();
                } else {
                    child.fail();
                }
            }
        } else {
            // equals child.fail()
            fail();
        }
    }
}
