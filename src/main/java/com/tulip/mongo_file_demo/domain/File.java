package com.tulip.mongo_file_demo.domain;

import com.sun.javafx.beans.IDProperty;
import lombok.Data;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@Document
public class File {

    @Id
    private String id;
    private String name;
    private String contentType;
    private Long size;
    private Date uploadDate;
    private String md5;
    private byte[] conent;
    private String path;

    protected File(){}

    public File(String name, String contentType, Long size, byte[] conent) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.conent = conent;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        File fileInfo = (File) object;
        return java.util.Objects.equals(size, fileInfo.size)
                && java.util.Objects.equals(name, fileInfo.name)
                && java.util.Objects.equals(contentType, fileInfo.contentType)
                && java.util.Objects.equals(uploadDate, fileInfo.uploadDate)
                && java.util.Objects.equals(md5, fileInfo.md5)
                && java.util.Objects.equals(id, fileInfo.id);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(name, contentType, size, uploadDate, md5, id);
    }

}
