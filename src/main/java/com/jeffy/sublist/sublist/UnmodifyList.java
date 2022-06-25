package com.jeffy.sublist.sublist;

import java.util.ArrayList;
import java.util.List;

/**
 *
 这段代码最后会抛出ConcurrentModificationException

 List-: [Delhi, Bangalore, New York, London]
 Sub List-: [Bangalore, New York]

 After Structural Change...

 List-: [Delhi, Bangalore, New York, London, Mumbai]
 Exception in thread "main" java.util.ConcurrentModificationException
 at java.util.ArrayList$SubList.checkForComodification(ArrayList.java:1231)
 at java.util.ArrayList$SubList.listIterator(ArrayList.java:1091)
 at java.util.AbstractList.listIterator(AbstractList.java:299)
 at java.util.ArrayList$SubList.iterator(ArrayList.java:1087)
 at java.util.AbstractCollection.toString(AbstractCollection.java:454)
 at java.lang.String.valueOf(String.java:2982)
 at java.lang.StringBuilder.append(StringBuilder.java:131)
 at infosys.Research.main(Research.java:26)
 简单看下ArrayList的源码：

 public boolean add(E e) {
 ensureCapacityInternal(size + 1);  // Increments modCount!!
 elementData[size++] = e;
 return true;
 }

 private void ensureCapacityInternal(int minCapacity) {
 ensureExplicitCapacity(calculateCapacity(elementData, minCapacity));
 }

 private void ensureExplicitCapacity(int minCapacity) {
 // 注意这行对原list的modCount这个变量做了自增操作
 modCount++;

 // overflow-conscious code
 if (minCapacity - elementData.length > 0)
 grow(minCapacity);
 }
 要注意，调用原数组的add方法时已经修改了原数组的modCount属性，当程序执行到打印subList这行代码时会调用Sublist#toString方法，到最后会调用到下面这个私有方法：

 private void checkForComodification() {
 if (ArrayList.this.modCount != this.modCount)
 throw new ConcurrentModificationException();
 }
 根据前面分析，原ArrayList的modCount属性已经自增，所以ArrayList.this.modCount != this.modCount执行的结果是true，最终抛出了ConcurrentModificationException异常。

 关于modCount这个属性，Oracle的文档中也有详细的描述

 The number of times this list has been structurally modified. Structural modifications are those that change the size of the list.

 翻译过来就是：

 modCount记录的是List被结构性修改的次数，所谓结构性修改是指能够改变List大小的操作

 如果提前没有知识储备，这类异常是比较难排查的
 */
public class UnmodifyList {

    public static void main(String[] args) {
        List<String> listArr = new ArrayList<>();
        listArr.add("Delhi");
        listArr.add("Bangalore");
        listArr.add("New York");
        listArr.add("London");

        List<String> listArrSub = listArr.subList(1, 3);

        System.out.println("List-: " + listArr);
        System.out.println("Sub List-: " + listArrSub);

        // 母列表不能修改了
        // Performing Structural Change in list.
        listArr.add("Mumbai");

        // 但子列表可以修改
        listArrSub.add("ShenZhen");

        System.out.println("\nAfter Structural Change...\n");

        System.out.println("List-: " + listArr);
        System.out.println("Sub List-: " + listArrSub);
    }

}
