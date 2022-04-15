package com.example.studentcareerapp.Domains;

import java.io.Serializable;

public class SemesterDomain implements Serializable {

    String item;


    public SemesterDomain() {

    }

    public SemesterDomain(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }
}
