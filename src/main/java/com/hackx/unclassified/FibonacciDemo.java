package com.hackx.unclassified;

public class FibonacciDemo {

    public static void main(String[] args) {
        fibonacci(5);
    }

    public static void fibonacci(int n) {

        if (n < 1) {
            System.out.println("Error");
        } else if (n == 1) {
            System.out.println(1);
            return;
        } else if (n >= 2) {
            System.out.println(1);
            System.out.println(1);
        }
        int n1 = 1, n2 = 1, n3 = -1;
        for (int i = 0; i < n - 2; i++) {
            n3 = n1 + n2;
            System.out.println(n3);
            n1 = n2;
            n2 = n3;
        }

    }

}
