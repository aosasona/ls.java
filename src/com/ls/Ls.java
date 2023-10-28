package com.ls;


public class Ls {
    private final String[] args;
    private String path;
    private boolean listAll = false;
    private boolean humanReadable = false;

    public Ls(String[] args) {
        this.args = args;
    }

    public void run() throws BadArgumentException {
        this.parseArgs();
        System.out.println("path: " + this.path);
        System.out.println("listAll: " + this.listAll);
        System.out.println("humanReadable: " + this.humanReadable);
    }

    private void parseArgs() throws BadArgumentException {
        for (String arg : this.args) {
            if (arg.charAt(0) != '-' && this.path == null) {
                this.path = arg;
                continue;
            } else if (arg.charAt(0) != '-') {
                throw new BadArgumentException(arg);
            }

            switch (arg.substring(1)) {
                case "l" -> this.humanReadable = true;
                case "a" -> this.listAll = true;
                case "la" -> {
                    this.listAll = true;
                    this.humanReadable = true;
                }
                default -> throw new BadArgumentException(arg);
            }
        }

        if (this.path == null) {
            this.path = ".";
        }
    }
}
