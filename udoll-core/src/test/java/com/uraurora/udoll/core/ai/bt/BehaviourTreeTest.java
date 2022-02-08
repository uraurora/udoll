package com.uraurora.udoll.core.ai.bt;

import com.uraurora.udoll.core.ai.bt.branch.Parallel;
import com.uraurora.udoll.core.ai.bt.branch.Sequence;
import com.uraurora.udoll.core.ai.bt.builder.BehaviourTreeBuilder;
import com.uraurora.udoll.core.ai.bt.decorator.FailedRetry;
import com.uraurora.udoll.core.ai.bt.decorator.UntilSuccess;
import com.uraurora.udoll.core.ai.bt.leaf.condition.RandomSuccess;
import lombok.val;
import org.junit.Test;

import java.util.Random;

import static com.uraurora.udoll.core.ai.bt.BTStatus.FAILED;
import static com.uraurora.udoll.core.ai.bt.BTStatus.SUCCEEDED;
import static org.junit.Assert.*;

public class BehaviourTreeTest {

    @Test
    public void run() {

        DogBT dog = new DogBT("tom");
        DogBT.Bark bark = new DogBT.Bark(3);
        DogBT.Rest rest = new DogBT.Rest();
        final DogBT.Mark mark = new DogBT.Mark();

        final Parallel<DogBT> sequence = new Parallel<>(
                bark,
                new FailedRetry<>(rest, 10),
                new UntilSuccess<>(mark)
        );
        final BehaviourTree<DogBT> tree = new BehaviourTree<>(sequence, dog);

        tree.start();
        System.out.println(tree.getStatus());
        tree.step();
        System.out.println(tree.getStatus());
        tree.end();
        System.out.println(tree.getStatus());


    }

    public static class DogBT{

        private String name;

        public DogBT(String name) {
            this.name = name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void bark() {
            if (new Random().nextBoolean()) log("Arf arf");
            else log("Woof");
        }

        public void startWalking() {
            log("Let's find a nice tree");
        }

        public void randomlyWalk() {
            log("SNIFF SNIFF - Dog walks randomly around!");
        }

        public void stopWalking() {
            log("This tree smells good :)");
        }

        public Boolean markATree(int i) {
            if (i == 0) {
                log("Swoosh....");
                return null;
            }
            if (new Random().nextBoolean()) {
                log("MUMBLE MUMBLE - Still leaking out");
                return Boolean.FALSE;
            }
            log("I'm ok now :)");
            return Boolean.TRUE;
        }

        public void log(String msg) {
            System.out.println("msg=" + msg);
        }

        public static class Bark extends LeafNode<DogBT> {
            private final int times;

            public Bark(int times) {
                this.times = times;
            }

            @Override
            public BTStatus execute() {
                for (int i = 0; i < times; i++) {
                    getObject().bark();
                }
                return SUCCEEDED;
            }
        }

        public static class Rest extends LeafNode<DogBT> {

            @Override
            public BTStatus execute() {
                System.out.println("zz zz zz");
                return SUCCEEDED;
            }
        }

        public static class Mark extends LeafNode<DogBT> {

            @Override
            public BTStatus execute() {
                final Boolean aBoolean = getObject().markATree(3);
                return aBoolean ? SUCCEEDED : FAILED;
            }
        }


    }
}