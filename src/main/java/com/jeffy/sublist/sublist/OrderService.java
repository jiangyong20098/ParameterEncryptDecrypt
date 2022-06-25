package com.jeffy.sublist;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 运行此程序，内存、CPU会飙升
 *
 *     public List<E> subList(int fromIndex, int toIndex) {
 *         subListRangeCheck(fromIndex, toIndex, size);
 *         return new SubList(this, 0, fromIndex, toIndex);
 *     }
 *
 *     private class SubList extends AbstractList<E> implements RandomAccess {
 *         private final AbstractList<E> parent;
 *         private final int parentOffset;
 *         private final int offset;
 *         int size;
 *
 *         SubList(AbstractList<E> parent,
 *                 int offset, int fromIndex, int toIndex) {
 *             this.parent = parent;
 *             this.parentOffset = fromIndex;
 *             this.offset = offset + fromIndex;
 *             this.size = toIndex - fromIndex;
 *             this.modCount = ArrayList.this.modCount;
 *         }
 *         ...
 *     }
 * 每次调用ArrayList#subList的时候都会生成一个SubList对象，而这个对象的parent属性值却持有原ArrayList的引用，这样一来就说得通了，
 * allFailedList持有历次调用queryOrder产生的List对象，这些对象最终都转移到了老年代而得不到释放。
 */
public class OrderService {
    public static void main(String[] args) {
        OrderService orderService = new OrderService();
        orderService.process();
    }
    
    public void process() {
        List<Long> orderIdList = queryOrder();
        List<List<Long>> allFailedList = new ArrayList<>();
        for(int i = 0; i < Integer.MAX_VALUE; i++) {
            System.out.println(i);
            List<Long> failedList = doProcess(orderIdList);
            allFailedList.add(failedList);
        }
    }
    
    private List<Long> doProcess(List<Long> orderIdList) {
        List<Long> failedList = new ArrayList<>();
        for (Long orderId : orderIdList) {
            if (orderId % 2 == 0) {
                failedList.add(orderId) ;
            }
        }
        // 只取一个失败的订单id做分析
        return failedList.subList(0, 1);
    }
    
    private List<Long> queryOrder() {
        List<Long> orderIdList = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            orderIdList.add(random.nextLong());
        }
        return orderIdList;
    }
}