package com.jeffy.sublist;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 如果我说上面的这段代码是一个死循环，你会感到奇怪么。回到subList的实现
 *
 * // AbstractList
 * public boolean add(E e) {
 *     add(size(), e);
 *     return true;
 * }
 *
 * 然后会调用到ArrayList#SubList的方法
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
 *
 *         ......
 *
 *         public int size() {
 *             checkForComodification();
 *             return this.size;
 *         }
 *
 *         public void add(int index, E e) {
 *             rangeCheckForAdd(index);
 *             checkForComodification();
 *             parent.add(parentOffset + index, e);
 *             this.modCount = parent.modCount;
 *             this.size++;
 *         }
 *        ......
 *     }
 * 可以看到，调用subList的add其实是在原ArrayList中增加元素，因此原arrayList.size()会一直变大，最终导致死循环。
 *
 */
@Slf4j
public class SubListDemo {
    public static void main(String[] args) throws InterruptedException {
        List<Long> arrayList = init();
        List<Long> subList = arrayList.subList(0, 1);
        for (int i = 0; i < arrayList.size(); i++) {
            log.info("size = {}", arrayList.size());
            Thread.sleep(1000);
            if (arrayList.get(i) % 2 == 0) {
                subList.add(arrayList.get(i));
            }
        }
    }
 
    private static List<Long> init() {
        Random random = new Random();
        List<Long> arrayList = new ArrayList<>();
        arrayList.add(random.nextLong());
        arrayList.add(random.nextLong());
        arrayList.add(random.nextLong());
        arrayList.add(random.nextLong());
        arrayList.add(random.nextLong());
        return arrayList;
    }
}