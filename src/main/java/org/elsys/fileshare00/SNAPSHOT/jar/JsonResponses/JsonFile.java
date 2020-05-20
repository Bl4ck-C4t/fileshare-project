package org.elsys.fileshare00.SNAPSHOT.jar.JsonResponses;

import java.util.List;

public class JsonFile {
    public boolean isDirectory;
    public String fileName;

    public JsonFile(boolean isDirectory, String fileName) {
        this.isDirectory = isDirectory;
        this.fileName = fileName;
    }
}
