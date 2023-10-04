package com.example.testikame.model;

public class Email {
    private int id;
    private int idPerson;
    private String email;

    public Email(int id, String email, int idPerson) {
        this.id = id;
        this.email = email;
        this.idPerson = idPerson;
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
}
