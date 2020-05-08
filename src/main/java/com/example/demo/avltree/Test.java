package com.example.demo.avltree;

public class Test {

    public static void main(String[] args) {
        AvlTree tree = new AvlTree();

        tree.insert(9);
        tree.insert(4);
        tree.insert(1);
        tree.insert(2);
        tree.insert(6);
        tree.insert(15);
        tree.insert(16);
        tree.insert(1);
        tree.insert(3);
        tree.insert(5);
        tree.insert(7);
        tree.print();

        tree.insert(8);
        tree.print();
        tree.delete(5);
        tree.print();
        tree.insert(13);
        tree.insert(12);
        tree.print();

        tree.delete(8);
        tree.print();
    }
}
