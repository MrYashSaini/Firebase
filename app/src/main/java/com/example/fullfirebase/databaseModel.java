package com.example.fullfirebase;

public class databaseModel {
    String name;
    int phoneno;
    Boolean whatsapp;

    public databaseModel() {
    }

    public databaseModel(Boolean whatsapp, int phoneno, String name ) {
        this.name = name;
        this.phoneno = phoneno;
        this.whatsapp = whatsapp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPhoneNo() {
        return phoneno;
    }

    public void setPhoneNo(int phoneno) {
        this.phoneno = phoneno;
    }

    public Boolean getWhatsapp() {
        return whatsapp;
    }

    public void setWhatsapp(Boolean whatsapp) {
        this.whatsapp = whatsapp;
    }
}
