package org.elsys.fileshare00.SNAPSHOT.jar.Files;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FileLinkRepo extends JpaRepository<FileLink, Integer> {
    @Query("select link from FileLink link where link.code = ?1")
    public FileLink findByLink(String link);

    @Query("select case when count(link)> 0 then true else false end from FileLink link where link.path = ?1")
    public boolean pathExists(String path);

    @Query("select link from FileLink link where link.path = ?1")
    public FileLink findByPath(String path);
}
