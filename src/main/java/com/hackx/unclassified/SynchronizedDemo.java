package com.hackx.unclassified;

import java.util.ArrayList;
import java.util.List;

public class SynchronizedDemo {

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Thread readThread = new Thread(new ReadList(list));
        Thread writeThread = new Thread(new WriteList(list));
        readThread.start();
        writeThread.start();
    }
}

class ReadList implements Runnable {

    private List<Integer> list;

    public ReadList(List<Integer> list) {
        this.list = list;
    }

    @Override
    public void run() {
        System.out.println("ReadList begin at " + System.currentTimeMillis());
        synchronized (list) {
            try {
                Thread.sleep(1000);
                System.out.println("list.wait() begin at " + System.currentTimeMillis());
                list.wait();
                System.out.println("list.wait() end at " + System.currentTimeMillis());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("ReadList end at " + System.currentTimeMillis());
    }
}

class WriteList implements Runnable {

    private List<Integer> list;

    public WriteList(List<Integer> list) {
        this.list = list;
    }

    public void run() {
        System.out.println("WriteList begin at " + System.currentTimeMillis());
        synchronized (list) {
            System.out.println("get lock at " + System.currentTimeMillis());
            list.notify();
            System.out.println("list.notify() at " + System.currentTimeMillis());
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("get out of block at " + System.currentTimeMillis());
        }
        System.out.println("WriteList end at " + System.currentTimeMillis());

    }
}
