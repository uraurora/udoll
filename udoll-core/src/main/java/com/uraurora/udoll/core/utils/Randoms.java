package com.uraurora.udoll.core.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-25 15:33
 * @description :
 */
public abstract class Randoms {

    public static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    /**
     * 返回[lower, upper)范围内的随机数
     * @param lower 下界
     * @param upper 上界
     * @return 范围内的随机int
     */
    public static int nextInt(int lower, int upper){
        return lower + RANDOM.nextInt(upper - lower);
    }


    /**
     * 返回[lower, upper)范围内的随机数
     * @param lower 下界
     * @param upper 上界
     * @return 范围内的随机long
     */
    public static long nextLong(long lower, long upper){
        return lower + RANDOM.nextLong(upper - lower);
    }

}
