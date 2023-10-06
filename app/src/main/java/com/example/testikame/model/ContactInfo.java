package com.example.testikame.model;

public class ContactInfo {
    private int idPerson;
    private String fullnamePerson;
    private String surnamePerson;
    private String namePerson;
    private String linkImg;


    public ContactInfo(String fullnamePerson, String surnamePerson
            , String namePerson, String linkImg) {
        this.fullnamePerson = fullnamePerson;
        this.surnamePerson = surnamePerson;
        this.namePerson = namePerson;
        this.linkImg = linkImg;
    }

    public ContactInfo(int idPerson, String fullnamePerson, String surnamePerson
            , String namePerson, String linkImg) {
        this.idPerson = idPerson;
        this.fullnamePerson = fullnamePerson;
        this.surnamePerson = surnamePerson;
        this.namePerson = namePerson;
        this.linkImg = linkImg;
    }

    public ContactInfo() {
    }


    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public String getFullnamePerson() {
        return fullnamePerson;
    }

    public void setFullnamePerson(String fullnamePerson) {
        this.fullnamePerson = fullnamePerson;
    }

    public String getSurnamePerson() {
        return surnamePerson;
    }

    public void setSurnamePerson(String surnamePerson) {
        this.surnamePerson = surnamePerson;
    }

    public String getNamePerson() {
        return namePerson;
    }

    public void setNamePerson(String namePerson) {
        this.namePerson = namePerson;
    }

    public String getLinkImg() {
        return linkImg;
    }

    public void setLinkImg(String linkImg) {
        this.linkImg = linkImg;
    }
}
