package vn.stu.edu.doancn.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Users implements Serializable {
    public Users(String users, String password, String phonenumber, String name, String image, String address) {
        Users = users;
        Password = password;
        Phonenumber = phonenumber;
        Name = name;
        Image = image;
        Address = address;
    }

    String Users;
    String Password;
    String Phonenumber;
    String Name;
    String Image;
    String Address;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getUsers() {
        return Users;
    }

    public void setUsers(String users) {
        Users = users;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public Users()
    {

    }

    @Override
    public String toString() {
        return "Users{" +
                "Users='" + Users + '\'' +
                ", Password='" + Password + '\'' +
                ", Phonenumber='" + Phonenumber + '\'' +
                ", Name='" + Name + '\'' +
                ", Image='" + Image + '\'' +
                ", Address='" + Address + '\'' +
                '}';
    }
}
