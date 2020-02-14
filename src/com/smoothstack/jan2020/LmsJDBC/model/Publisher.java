package com.smoothstack.jan2020.LmsJDBC.model;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.persistence.Column;
import com.smoothstack.jan2020.LmsJDBC.persistence.Id;
import com.smoothstack.jan2020.LmsJDBC.persistence.TableName;

@TableName("tbl_publisher")
public class Publisher implements Entity<Publisher> {

    @Id
    @Column(name="publisherId")
    private Integer id;

    @Column(name="publisherName")
    private String name;

    @Column(name="publisherAddress")
    private String address;

    @Column(name="publisherPhone")
    private String phone;

    public Publisher() {
    }

    @Override
    public String toMenuLabel() {
        return String.format("%s (%s)", getName(), getAddress());
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
