package com.uraurora.udoll.core.ai.bt.decorator;

import com.uraurora.udoll.core.ai.bt.BTStatus;
import com.uraurora.udoll.core.ai.bt.DecoratorNode;
import com.uraurora.udoll.core.ai.bt.INode;
import com.uraurora.udoll.core.ai.bt.annotation.NodeAttribute;
import com.uraurora.udoll.core.ai.bt.util.Times;
import com.uraurora.udoll.core.ai.bt.value.NodeContext;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author : gaoxiaodong04
 * @program : crescent
 * @date : 2020-09-23 21:03
 * @description : 信号量节点，允许指定可以进入该节点内部并发执行该节点装饰的子节点的线程（角色）的数量
 * 该节点修饰的子节点通常表示一个具有限定数量的资源，该资源可能使用在不同的行为树当中
 * 当资源被耗尽（信号量的许可数量为0）或者获取资源发生中断异常时，该节点会失败。这可以使得较高位置的行为树节点避免陷入资源竞争
 * <p>
 *     比如，作简单的限流作用，将某个方法调用作为子节点，限制调用该方法的数量；又比如，连接池有20个连接，限制最多20个连接接入
 */
public class SemaphoreNode<E> extends DecoratorNode<E> {

    private final Object lock = new Object();

    @NodeAttribute(name = "semaphore")
    private transient Semaphore semaphore;

    public SemaphoreNode(int resource) {
        this.semaphore = new Semaphore(resource);
    }

    public SemaphoreNode(INode<E> child, int resource) {
        super(child);
        this.semaphore = new Semaphore(resource);
    }

    public int availablePermits(){
        return semaphore.availablePermits();
    }

    @Override
    public void run() {
        // TODO:多线程下有问题
        synchronized (lock){
            if(semaphore.availablePermits()>0){
                super.run();
            }else{
                interruptedFail();
            }
        }
    }

    @Override
    public void start(){
        try {
            semaphore.tryAcquire(Times.BT_NODE_TIME_OUT_SECONDS, TimeUnit.MILLISECONDS);
            super.start();
        } catch (InterruptedException e) {
            // 获取资源异常，该装饰节点失败
            interruptedFail();
        }
    }

    @Override
    public void end() {
        semaphore.release();
        super.end();
    }

    /**
     * 在start方法中获取资源时发生中断异常的时候调用的失败方法，用来通知父节点该装饰节点失败
     */
    private void interruptedFail(){
        BTStatus previousStatus = status;
        status = BTStatus.FAILED;
        if (tree.listeners != null && tree.listeners.size() > 0) {
            tree.notifyStatusUpdated(this, previousStatus);
        }
        // 此时不应调用该类的end，因为会导致导致已有的信号量释放一个资源，应该直接调用父类的end方法
        super.end();
        if (control != null) {
            final NodeContext<E> context = NodeContext.<E>builder()
                    .withNode(this)
                    .withStatus(status)
                    .build();
            control.childFail(context);
        }
    }

    @Override
    public void resetTask() {
        super.resetTask();
        semaphore = null;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }
}
