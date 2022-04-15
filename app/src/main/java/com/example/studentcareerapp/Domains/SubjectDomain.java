package com.example.studentcareerapp.Domains;

import java.io.Serializable;

public class SubjectDomain implements Serializable {

    String Item;

    public SubjectDomain() {
    }

    public SubjectDomain(String item) {
        Item = item;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
