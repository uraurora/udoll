package com.uraurora.udoll.core.ai.bt.leaf.condition;

import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.LeafNode;
import com.uraurora.udoll.core.ai.bt.util.Randoms;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-25 15:32
 * @description : {@link RandomSuccess} 是一个有一定概率返回成功的叶子节点，
 * 它返回成功的概率通过probability设定，则返回失败的概率为1 - probability，此节点不会返回其他状态
 */
public class RandomSuccess<E> extends LeafNode<E> {

    private final double probability;

    private double score;

    public RandomSuccess(double probability) {
        this.probability = probability;
    }

    @Override
    public BTStatus execute() {
        score = Randoms.RANDOM.nextDouble();
        return score <= probability? BTStatus.SUCCEEDED : BTStatus.FAILED;
    }

    public double getScore(){
        return score;
    }
}
