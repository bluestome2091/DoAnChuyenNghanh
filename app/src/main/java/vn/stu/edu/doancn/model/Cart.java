package vn.stu.edu.doancn.model;

public class Cart {
    private String pid, name, price, quatity, discount, date, time, user;

    public Cart() {
    }

    public Cart(String pid, String name, String price, String quatity, String discount, String date, String time, String user) {
        this.pid = pid;
        this.name = name;
        this.price = price;
        this.quatity = quatity;
        this.discount = discount;
        this.date = date;
        this.time = time;
        this.user = user;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuatity() {
        return quatity;
    }

    public void setQuatity(String quatity) {
        this.quatity = quatity;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
