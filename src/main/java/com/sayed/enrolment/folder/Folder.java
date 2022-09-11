package com.sayed.enrolment.folder;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sayed.enrolment.file.AppFile;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Setter @Getter
public class Folder {
    @Id
    @Column(nullable = false)
    private String id;
    private String url;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private Timestamp date;
    private String parentId;
    private final String type = "FOLDER";
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<AppFile> childrenFiles;
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Folder> childrenFolders;

    public Folder() {
        childrenFolders = new ArrayList<>();
        childrenFiles = new ArrayList<>();
    }

    public void addChildFile(AppFile file) {
        childrenFiles.add(file);
    }

    public void addChildFolder(Folder folder) {
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

    public Integer getSize() {
        Integer size = 0;
        for (var child : childrenFolders) {
            size += child.getSize();
        }
        for (var child : childrenFiles) {
            size += child.getSize();
        }
        return size;
    }


}
