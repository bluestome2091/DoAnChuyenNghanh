package vn.stu.edu.doancn.model;

public class AdminHistoryProduct {
    private String date, discount, name, pid, quatity, time, user;

    public AdminHistoryProduct() {
    }

    public AdminHistoryProduct(String date, String discount, String name, String pid, String quatity, String time, String user) {
        this.date = date;
        this.discount = discount;
        this.name = name;
        this.pid = pid;
        this.quatity = quatity;
        this.time = time;
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getQuatity() {
        return quatity;
    }

    public void setQuatity(String quatity) {
        this.quatity = quatity;
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
