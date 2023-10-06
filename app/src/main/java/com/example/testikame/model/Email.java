package com.example.testikame.model;

public class Email {
    private int id;
    private int idPerson;
    private String emailType;
    private String email;

    public Email(int id, int idPerson, String emailType, String email) {
        this.id = id;
        this.email = email;
        this.emailType = emailType;
        this.idPerson = idPerson;
    }

    public Email() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdPerson() {
        return idPerson;
    }

    public void setIdPerson(int idPerson) {
        this.idPerson = idPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }
}
