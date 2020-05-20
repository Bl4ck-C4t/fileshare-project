package org.elsys.fileshare00.SNAPSHOT.jar.JsonResponses;

import java.util.List;

public class FileInfo {
    public boolean isDirectory;
    public String fileName;

    public FileInfo(boolean isDirectory, String fileName) {
        this.isDirectory = isDirectory;
        this.fileName = fileName;
    }
}
