package com.example.studentcareerapp.Domains;

import java.io.Serializable;

public class FacultyDomain implements Serializable {

    String Item;


    public FacultyDomain() {

    }

    public FacultyDomain(String item) {
        Item = item;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
