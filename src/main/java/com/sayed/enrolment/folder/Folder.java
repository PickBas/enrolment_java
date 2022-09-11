package com.sayed.enrolment.folder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.sayed.enrolment.file.AppFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.*;

@Entity
@Setter @Getter
public class Folder {
    @Id
    @Column(nullable = false)
    private String id;
    private String url;
    private Timestamp date;
    private String parentId;
    private Integer size;
    private final String type = "FOLDER";
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AppFile> childrenFiles;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Folder> childrenFolders;

    public Folder() {
        size = 0;
        childrenFolders = new ArrayList<>();
        childrenFiles = new ArrayList<>();
    }

    public void addChildFile(AppFile file) {
        this.size += file.getSize();
        childrenFiles.add(file);
    }

    public void addChildFolder(Folder folder) {
        this.size += folder.getSize();
        childrenFolders.add(folder);
    }

    public void removeChildFile(AppFile file) {
        childrenFiles.remove(file);
    }

    public void removeChildFolder(Folder folder) {
        childrenFolders.remove(folder);
    }

    public Collection<Object> getChildren() {
        Collection<Object> children = new ArrayList<>();
        children.addAll(childrenFiles);
        children.addAll(childrenFolders);
        return children;
    }


}
