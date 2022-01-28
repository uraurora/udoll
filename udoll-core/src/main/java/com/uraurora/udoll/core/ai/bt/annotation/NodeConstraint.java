package com.uraurora.udoll.core.ai.bt.annotation;

import java.lang.annotation.*;

/**
 * @author gaoxiaodong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
@Documented
public @interface NodeConstraint {

    /**
     * 返回装饰的该节点的子节点数目最小值约束，默认值为0
     * @return 子节点最小值约束 */
    int minChildren() default 0;

    /**
     * 返回装饰的该节点的子节点数目最大值约束，默认值为 {@code Integer.MAX_VALUE}
     * @return 子节点最大值约束 */
    int maxChildren() default Integer.MAX_VALUE;
}
