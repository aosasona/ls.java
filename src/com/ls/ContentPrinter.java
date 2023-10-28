package com.ls;

import java.nio.file.*;

public class ContentPrinter extends Thread {
    private final String path;
    private final String item;
    private final boolean listAll;
    private final boolean showMeta;

    public ContentPrinter(String path, String item, boolean listAll, boolean showMeta) {
        this.path = path;
        this.item = item;
        this.listAll = listAll;
        this.showMeta = showMeta;
    }


    public void run() {
        var builder = new StringBuilder();
        if (!this.listAll && item.charAt(0) == '.') {
            return;
        }

        var path = Paths.get(this.path, item);
        var file = path.toFile();

        if (this.showMeta) {
            var size = this.toReadableSize(this.getPathSize(path));
            var lastModified = file.lastModified();
            var date = new java.util.Date(lastModified);
            var formattedDate = new java.text.SimpleDateFormat("MMM dd HH:mm").format(date);

            builder.append(String.format("%-10s", size));
            builder.append(String.format("%-15s", formattedDate));
        }

        if (file.isDirectory()) {
            builder.append(AnsiColor.GREEN);
            builder.append(item);
            builder.append(AnsiColor.RESET);
        } else if (file.canExecute()) {
            builder.append(AnsiColor.RED);
            builder.append(item);
            builder.append(AnsiColor.RESET);
        } else {
            builder.append(item);
        }

        System.out.println(builder.toString());
    }

    // Recursive method to get the size of a directory and all its subdirectories - calling .length() on a directory returns a wrong value.
    private long getPathSize(Path path) {
        var file = path.toFile();

        if(file.isFile()) {
            return file.length();
        } else {
            var size = 0L;
            var content = file.list();

            if (content == null) {
                return 0;
            }

            for (var item : content) {
                size += this.getPathSize(Paths.get(path.toString(), item));
            }

            return size;
        }
    }

    private String toReadableSize(long size) {
        if (size < 1024) {
            return size + "B";
        } else if (size < 1024 * 1024) {
            return size / 1024 + "KB";
        } else if (size < 1024 * 1024 * 1024) {
            return size / (1024 * 1024) + "MB";
        } else {
            return size / (1024 * 1024 * 1024) + "GB";
        }
    }
}
