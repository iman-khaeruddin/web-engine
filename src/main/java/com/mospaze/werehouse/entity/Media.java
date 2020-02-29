package com.mospaze.werehouse.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Iman Khaeruddin
 * @since 13/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "media")
public class Media implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "model_id")
    private int model_id;

    @Column(name = "model_type")
    private String model_type;

    @Column(name = "collection_name")
    private String collection_name;

    @Column(name = "name")
    private String name;

    @Column(name = "file_name")
    private String file_name;

    @Column(name = "mime_type")
    private String mime_type;

    @Column(name = "disk")
    private String disk;

    @Column(name = "size")
    private int size;

    @Column(name = "manipulations")
    private String manipulations;

    @Column(name = "custom_properties")
    private String custom_properties;

    @Column(name = "order_column")
    private int order_column;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date created_at;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updated_at;

    @Override
    public String toString() {
        return "Media{" +
                "id=" + id +
                ", model_id=" + model_id +
                ", model_type='" + model_type + '\'' +
                ", collection_name='" + collection_name + '\'' +
                ", name='" + name + '\'' +
                ", file_name='" + file_name + '\'' +
                ", mime_type='" + mime_type + '\'' +
                ", disk='" + disk + '\'' +
                ", size=" + size +
                ", manipulations='" + manipulations + '\'' +
                ", custom_properties='" + custom_properties + '\'' +
                ", order_column=" + order_column +
                ", created_at=" + created_at +
                ", updated_at=" + updated_at +
                '}';
    }
}
