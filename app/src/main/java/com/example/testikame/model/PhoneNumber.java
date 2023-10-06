package com.example.testikame.model;

public class PhoneNumber {
    private int id;
    private int idPerson;
    private String phoneType;
    private String phoneNumber;

    public  PhoneNumber(){}

    public PhoneNumber(int id, int idPerson,String phoneType, String phoneNumber) {
        this.id = id;
        this.phoneType = phoneType;
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

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }
}
