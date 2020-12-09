package com.itheima.scope;

public class PublicTest {
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
        FriendlyTest test = new FriendlyTest();
        String test4 = test.test;
        //String test5 = test.test1;
        String test6 = test.test2;
        String test7 = test.test3;

        test.test();
        //test.test1();
        test.test2();
        test.test3();
    }
}
