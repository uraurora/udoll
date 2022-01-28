package com.uraurora.udoll.core.ai.bt.util;

import org.joor.Reflect;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static org.joor.Reflect.*;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-11-16 17:29
 * @description :
 */
public abstract class Reflects {

    public static <T> Map<String, Object> toMap(T obj){
        return Reflect.on(obj).fields().entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().get(),
                (oldVal, newVal) -> newVal,
                HashMap::new
        ));
    }

    public static <T> Map<String, Object> toMap(Class<? extends T> clazz){
        return Reflect.onClass(clazz).fields().entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                e -> e.getValue().get(),
                (oldVal, newVal) -> newVal,
                HashMap::new
        ));
    }

}
