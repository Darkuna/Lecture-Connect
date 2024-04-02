package com.example.lecture_connect.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name="Userx")
public class Userx {

    @Id
    @Column(length = 45)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int UserId;

    @Column(length = 255)
    private String name;

    @Column(length = 255)
    private String password;

    public Userx() {
    }

    public Userx(String name, String password) {
        this.name = name;
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Userx userx = (Userx) o;

        if (getUserId() != userx.getUserId()) return false;
        if (getName() != null ? !getName().equals(userx.getName()) : userx.getName() != null) return false;
        return getPassword() != null ? getPassword().equals(userx.getPassword()) : userx.getPassword() == null;
    }

    @Override
    public int hashCode() {
        int result = getUserId();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getPassword() != null ? getPassword().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Userx{" +
                "UserId=" + UserId +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
