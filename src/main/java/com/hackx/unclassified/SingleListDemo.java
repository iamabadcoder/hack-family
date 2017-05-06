package com.hackx.unclassified;

public class SingleListDemo {

    public static void main(String[] args) {
        Node head = new Node(0);
        Node node1 = new Node(1);
        head.setNext(node1);
        Node node2 = new Node(2);
        node1.setNext(node2);
        Node node3 = new Node(3);
        node2.setNext(node3);
        Node newHead = reverse2(head);
        while (newHead != null) {
            System.out.println(newHead.getVal());
            newHead = newHead.getNext();
        }
    }

    public static Node reverse1(Node head) {
        if (head == null || head.getNext() == null) {
            return head;
        }
        Node preNode = head;
        Node curNode = head.getNext();
        Node tmpNode;

        while (curNode != null) {
            tmpNode = curNode.getNext();
            curNode.setNext(preNode);
            preNode = curNode;
            curNode = tmpNode;
        }
        head.setNext(null);
        return preNode;
    }

    public static Node reverse2(Node head) {
        if (head == null || head.getNext() == null) {
            return head;
        }
        Node tmpHead = reverse2(head.getNext());
        head.getNext().setNext(head);
        head.setNext(null);
        return tmpHead;
    }


//
//    public static Node Reverse1(Node head) {
//        if (head == null || head.getNext() == null){
//            return head;
//        }
//        Node reHead = Reverse1(head.getNext());
//        head.getNext().setNext(head);
//        head.setNext(null);
//        return reHead;
//    }
}

class Node {

    private int val;

    private Node next;

    public Node(int val) {
        this.val = val;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}
