package org.elsys.fileshare00.SNAPSHOT.jar.JsonResponses;

import java.util.List;

public class FileJson {
    public ServedFile file = null;
    public List<FileInfo> files = null;

    public FileJson(ServedFile file, List<FileInfo> files) {
        this.file = file;
        this.files = files;
    }
}
