package com.example.studentcareerapp.Domains;

import java.io.Serializable;

public class DepartmentDomain implements Serializable {

    String Item;


    public DepartmentDomain() {

    }

    public DepartmentDomain(String item) {
        Item = item;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
