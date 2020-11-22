package com.example.sendemail;

public class Customer {
    private int age;
    private String email;
    private String fullname;

    public Customer() {
    }

    public Customer(int age, String email, String fullname) {
        this.age = age;
        this.email = email;
        this.fullname = fullname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }
}
