package com.wzp.practice;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

public class ListTest {

    @Test
    public void listPutTest() {
        List<String> list = null;
        int baseNum = 100000;
        long linkedTime = 0;
        long arrayTime = 0;

        // test linkedList put
        list = new LinkedList<String>();
        long ts = System.nanoTime();
        for (int i = 0; i < baseNum; i++) {
            list.add("testLinkedList");
        }
        linkedTime = System.nanoTime() - ts;
        
        //test arrayList put
        list.clear();
        list = null;
        list = new ArrayList<>();
        ts = System.nanoTime();
        for (int i = 0; i < baseNum; i++) {
            list.add("testLinkedList");
        }
        arrayTime = System.nanoTime() - ts;
        
        System.out.println(String.format("LinkedList put:%s ArrayList put :%s", linkedTime,arrayTime));
    }
    
    @Test
    public void listForTest() {
        List<String> list = null;
        int baseNum = 100000;
        long linkedForTime = 0;
        long linkedGetTime = 0;
        long arrayForTime = 0;
        long arrayGetTime = 0;
        String temp = null;

        // test linkedList put
        list = new LinkedList<String>();
        
        for (int i = 0; i < baseNum; i++) {
            list.add("testLinkedList");
        }
        long ts = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis());
        for(int index = 0; index < list.size(); index++){
            temp = list.get(index);
        }
        System.out.println(System.currentTimeMillis());
        linkedGetTime = System.currentTimeMillis() - ts;
        
        ts = System.currentTimeMillis();
        System.out.println(System.currentTimeMillis());
        for(String element : list){
            temp = element;
        }
        System.out.println(System.currentTimeMillis());
        linkedForTime = System.currentTimeMillis() - ts;
        System.out.println(String.format("LinkedForTime put:%s LinkedGetTime put :%s", linkedForTime,linkedGetTime));
        
        
        //test arrayList put
        list.clear();
        list = null;
        list = new ArrayList<>();
        for (int i = 0; i < baseNum; i++) {
            list.add("testArrayList");
        }
        ts = System.nanoTime();
        for(String element : list){
            temp = element;
        }
        arrayForTime = System.nanoTime() - ts;
        
        ts = System.nanoTime();
        for(int index = 0; index < list.size(); index++){
            temp = list.get(index);
        }
        arrayGetTime = System.nanoTime() - ts;
        System.out.println(String.format("ArrayForTime put:%s ArrayGetTime put :%s", arrayForTime,arrayGetTime));
        
    }
}
