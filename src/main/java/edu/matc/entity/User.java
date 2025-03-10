package edu.matc.entity;

import javax.persistence.*;
@Entity
@Table(name="users")
public class User {

    @id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @first_name
    @Column(name = "first_name")
    private String first_name;

    @last_name
    @Column(name = "last_name")
    private String last_name;

    @email
    @Column(name = "email")
    private String email;

    @password
    @Column(name = "password")
    private String password;

    //no-args constructor
    public User() {}

    //constructor
    public User(int id, String first_name, String last_name, String email, String password) {
        this.id = id;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
