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
 * @since 28/11/2019
 * @see java.io.Serializable
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "data_tracker")
public class DataTracker implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "end_point")
    private String end_point;

    @Column(name = "method_name")
    private String method_name;

    @Column(name = "user_agent")
    private String user_agent;

    @Column(name = "browser")
    private String browser;

    @Column(name = "os")
    private String os;

    @Column(name = "ip_address")
    private String ip_address;

    @Column(name = "username")
    private String username;

    @Column(name = "longitude")
    private String latitude;

    @Column(name = "request_header")
    private String request_header;

    @Column(name = "request_body")
    private String request_body;

    @Column(name = "request_param")
    private String request_param;

    @Column(name = "response_header")
    private String response_header;

    @Column(name = "response_body")
    private String response_body;

    @Column(name = "city")
    private String city;

    @Column(name = "district")
    private String district;

    @Column(name = "is_racking")
    private Integer is_racking;

    @Column(name = "feature")
    private String feature;

    @Column(name = "space_service")
    private String space_service;

    @Column(name = "temperature")
    private String temperature;

    @Column(name = "category")
    private String category;

    @Column(name = "space")
    private String space;

    @Column(name = "type")
    private String type;

    @Column(name = "service")
    private String service;

    @Column(name = "automation")
    private String automation;

    @Column(name = "size_min")
    private String size_min;

    @Column(name = "size_max")
    private String size_max;

    @Column(name = "unit")
    private String unit;

    @Column(name = "request_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private Date request_time;

    @Column(name = "response_time")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    private Date response_time;

    @Override
    public String toString() {
        return "DataTracker{" +
                "id=" + id +
                ", end_point='" + end_point + '\'' +
                ", method_name='" + method_name + '\'' +
                ", user_agent='" + user_agent + '\'' +
                ", browser='" + browser + '\'' +
                ", os='" + os + '\'' +
                ", ip_address='" + ip_address + '\'' +
                ", username='" + username + '\'' +
                ", latitude='" + latitude + '\'' +
                ", request_header='" + request_header + '\'' +
                ", request_body='" + request_body + '\'' +
                ", request_param='" + request_param + '\'' +
                ", response_header='" + response_header + '\'' +
                ", response_body='" + response_body + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", feature='" + feature + '\'' +
                ", space_service='" + space_service + '\'' +
                ", temperature='" + temperature + '\'' +
                ", category='" + category + '\'' +
                ", space='" + space + '\'' +
                ", type='" + type + '\'' +
                ", service='" + service + '\'' +
                ", automation='" + automation + '\'' +
                ", size_min='" + size_min + '\'' +
                ", size_max='" + size_max + '\'' +
                ", unit='" + unit + '\'' +
                ", request_time=" + request_time +
                ", response_time=" + response_time +
                '}';
    }
}
