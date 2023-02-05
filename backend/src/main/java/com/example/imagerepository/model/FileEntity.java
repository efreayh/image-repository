package com.example.imagerepository.model;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "FILES")
public class FileEntity {

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    private String id;

    private String name;

    private String contentType;

    private Long size;

    @Lob
    private byte[] data;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private Set<LabelEntity> labels;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Set<LabelEntity> getLabels() {
        return labels;
    }

    public void setLabels(Set<LabelEntity> labels) {
        this.labels = labels;
    }
}