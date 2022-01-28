package com.uraurora.udoll.core.ai.bt.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @description 属性级别注解，表示注释的属性是一个行为树节点属性
 * @author gaoxiaodong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NodeAttribute {

    /** Specifies the attribute's name; if empty the name of the field is used instead.
     * @return the attribute's name or an empty string if the name of the field must be used. */
    String name() default "";

    /** Specifies whether the attribute is required or not.
     * @return {@code true} if the attribute is required; {@code false} if it is optional. */
    boolean required() default false;
}
