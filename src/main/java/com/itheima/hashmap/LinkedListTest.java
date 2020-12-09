package com.itheima.hashmap;

public class LinkedListTest {
    public static void main(String[] args) {

        Monkey monkey1 = new Monkey(100,"圆圆");
        Monkey monkey2 = new Monkey(101,"方方");
        Monkey monkey3 = new Monkey(102,"角角");
        Monkey monkey4 = new Monkey(103,"朱朱");

        monkey1.next = monkey2;
        monkey2.next = monkey3;
        monkey3.next = monkey4;
        monkey4.next = null;


        Monkey node = monkey1;
        while (node!=null){
            System.out.println("node:"+node.id+"  "+node.name);
            node = node.next;
        }
        System.out.println("exit");
    }
}
