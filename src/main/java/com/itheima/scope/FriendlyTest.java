package com.itheima.scope;

public class FriendlyTest {
    String test;
    private String test1;
    protected String test2;
    public String test3;
    void test(){
        System.out.println("PrivateTest-test");
    }
    private void test1(){
        System.out.println("PrivateTest-test1");
    }
    protected void test2(){
        System.out.println("PrivateTest-test2");
    }
    public void test3(){
        System.out.println("PrivateTest-test3");
    }
}
