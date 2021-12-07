package vn.stu.edu.doancn.model;

public class Users {
    public Users(String users, String password, String phonenumber, String name) {
        Users = users;
        Password = password;
        Phonenumber = phonenumber;
        Name = name;
    }

    String Users;
    String Password;
    String Phonenumber;
    String Name;

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
                '}';
    }
}
