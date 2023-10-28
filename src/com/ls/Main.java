package com.ls;

public class Main {
    public static void main(String[] args) {
        try {
            var ls = new Ls(args);
            ls.run();
        } catch(BadArgumentException e) {
            System.err.println("Bad or unexpected argument: " + e.arg);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            System.exit(1);
        }
    }
}
