package org.elsys.fileshare00.SNAPSHOT.jar.JsonResponses;

import java.util.List;

public class FileInfo {
    public boolean isDirectory;
    public String fileName;
    public boolean hasLink;

    public FileInfo(boolean isDirectory, String fileName, boolean hasLink) {
        this.isDirectory = isDirectory;
        this.fileName = fileName;
        this.hasLink = hasLink;
    }
}
