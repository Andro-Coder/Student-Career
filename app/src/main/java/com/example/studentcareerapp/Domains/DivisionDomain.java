package com.example.studentcareerapp.Domains;

import java.io.Serializable;

public class DivisionDomain implements Serializable {

    String Item;

    public DivisionDomain() {
    }

    public DivisionDomain(String item) {
        Item = item;
    }

    public String getItem() {
        return Item;
    }

    public void setItem(String item) {
        Item = item;
    }
}
