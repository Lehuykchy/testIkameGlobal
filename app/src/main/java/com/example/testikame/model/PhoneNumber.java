package com.example.testikame.model;

public class PhoneNumber {
    private int id;
    private int idPerson;
    private String phoneNumber;

    public PhoneNumber(int id, int idPerson, String phoneNumber) {
        this.id = id;
        this.phoneNumber = phoneNumber;
        this.idPerson = idPerson;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }
}
