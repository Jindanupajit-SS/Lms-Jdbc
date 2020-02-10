package com.smoothstack.jan2020.LmsJDBC.model;

import com.smoothstack.jan2020.LmsJDBC.entity.Entity;
import com.smoothstack.jan2020.LmsJDBC.persistence.Column;
import com.smoothstack.jan2020.LmsJDBC.persistence.Id;
import com.smoothstack.jan2020.LmsJDBC.persistence.Table;

@Table("tbl_borrower")
public class Borrower implements Entity<Borrower> {

    @Id
    @Column(name = "cardNo")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "address")
    private String address;

    @Column(name = "phone")
    private String phone;

    public Borrower() {
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
