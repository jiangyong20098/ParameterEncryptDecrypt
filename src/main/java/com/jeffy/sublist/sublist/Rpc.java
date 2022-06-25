package com.jeffy.sublist;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 四、作为RPC接口入参时序列化失败
 * 从上面SubList的定义可以看出来，SubList并没有实现Serializable接口，因此在一些依赖Java原生序列化协议的RPC的框架中会序列化失败，如Dubbo等。
 * <p>
 * 五、最佳实践
 * subList设计之初是作为原List的一个视图，经常在只读的场景下使用，这和大多数人理解的不太一样，即便只在只读的场景下使用，也容易产生内存泄露，况且这个视图的存在还不允许原List和SubList做结构性修改，个人认为subList这个Api的设计糟糕透了，尽量在代码中避免直接使用ArrayList#subList，获取List的subList有两条最佳实践：
 * <p>
 * 5.1 拷贝到新的ArrayList中
 * ArrayList myArrayList = new ArrayList();
 * ArrayList part1 = new ArrayList(myArrayList.subList(0, 25));
 * ArrayList part2 = new ArrayList(myArrayList.subList(26, 51));
 * 5.2 使用lambda表达式
 * dataList.stream().skip(5).limit(10).collect(Collectors.toList());
 * dataList.stream().skip(30).limit(10).collect(Collectors.toList());
 */
public class Rpc {
    public static void main(String[] args) {
        List<String> listArr = new ArrayList<>();
        listArr.add("Delhi");
        listArr.add("Bangalore");
        listArr.add("New York");
        listArr.add("London");
        listArr.add("ShenZhen");
        listArr.add("GuangZhou");
        listArr.add("ShangHai");

        // 拷贝到新的ArrayList中
        List<String> part1 = new ArrayList<>(listArr.subList(0, 3));
        List<String> part2 = new ArrayList<>(listArr.subList(4, 5));

        System.out.println("part1 = " + part1);
        System.out.println("part2 = " + part2);

        // 使用lambda表达式生成新的List
        listArr.stream().skip(1).limit(2).collect(Collectors.toList());
        listArr.stream().skip(3).limit(5).collect(Collectors.toList());

    }
}

