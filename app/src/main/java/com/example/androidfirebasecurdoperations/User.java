package com.example.androidfirebasecurdoperations;

public class User {

    String id, name, email, contact, city, language;

    public User() {
    }

    public User(String id, String name, String email, String contact, String city, String language) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
        this.city = city;
        this.language = language;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }


}
