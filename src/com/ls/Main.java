package com.ls;

public class Main {
    public static void main(String[] args) {
        try {
            var ls = new Ls(args);
            ls.run();
        } catch(BadArgumentException e) {
            System.out.println("Bad or unexpected argument: " + e.arg);
        }
    }
}
