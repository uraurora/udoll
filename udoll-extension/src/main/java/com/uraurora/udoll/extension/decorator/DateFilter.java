package com.uraurora.udoll.extension.decorator;

import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.annotation.NodeAttribute;
import com.uraurora.udoll.core.ai.bt.decorator.Filter;

import java.time.LocalDate;

/**
 * @author : gaoxiaodong04
 * @program : udoll
 * @date : 2022-02-09 17:10
 * @description : 日期过滤器，当今日时间等于某个时间时返回成功继续执行子节点
 */
public class DateFilter<E> extends Filter<E> {

    @NodeAttribute
    private final LocalDate date;

    public DateFilter(LocalDate date) {
        super(o -> date.isEqual(LocalDate.now()));
        this.date = date;
    }

    public DateFilter(INode<E> child, LocalDate date) {
        super(child, o -> date.isEqual(LocalDate.now()));
        this.date = date;
    }


}
