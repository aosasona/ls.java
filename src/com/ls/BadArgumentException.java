package com.ls;

public class BadArgumentException extends Throwable {
    public String arg;

    BadArgumentException(String arg) {
        this.arg = arg;
    }
}
