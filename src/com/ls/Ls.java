package com.ls;


import java.io.IOException;
import java.nio.file.*;

public class Ls {
    private final String[] args;
    private String path;
    private boolean listAll = false;
    private boolean showMeta = false;

    public Ls(String[] args) {
        this.args = args;
    }

    public void run() throws BadArgumentException, IOException {
        this.parseArgs();
        this.expandPath();
        this.ensurePathExists();
        this.ensurePathIsDirectory();

        var content = this.getContent();
        this.printContent(content);
    }

    private String[] getContent() throws IOException {
        var path = Paths.get(this.path);
        var content = path.toFile().list();

        if (content == null) {
            throw new IOException("Could not read directory: " + this.path);
        }

        return content;
    }

    private void printContent(String[] content) {
        for (String item : content) {
            var printer = new ContentPrinter(this.path, item, this.listAll, this.showMeta);
            Thread thread = new Thread(printer);
            thread.start();
        }
    }

    // If the path is relative, expand it to an absolute path, like replacing the tilda with the user's home directory or the dot with the current working directory.
    private void expandPath() {
        if (this.path.contains("~")) {
            this.path = this.path.replace("~", System.getProperty("user.home"));
        } else if (this.path.charAt(0) == '.') {
            this.path = this.path.replace(".", System.getProperty("user.dir"));
        } else {
            this.path = Paths.get(this.path).toAbsolutePath().toString();
        }
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
                case "l" -> this.showMeta = true;
                case "a" -> this.listAll = true;
                case "la" -> {
                    this.listAll = true;
                    this.showMeta = true;
                }
                default -> throw new BadArgumentException(arg);
            }
        }

        if (this.path == null) {
            this.path = ".";
        }
    }

    private void ensurePathExists() {
        if (!Files.exists(Paths.get(this.path))) {
            System.err.println("Path does not exist: " + this.path);
            System.exit(1);
        }
    }

    private void ensurePathIsDirectory() {
        if (!Files.isDirectory(Paths.get(this.path))) {
            System.err.println("Path is not a directory: " + this.path);
            System.exit(1);
        }
    }
}
