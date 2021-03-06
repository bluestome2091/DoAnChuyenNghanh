package vn.stu.edu.doancn.model;

public class AdminHistoryOrder {
    private String address, city, date, totalPrice, name, phone, state, time;

    public AdminHistoryOrder() {
    }

    public AdminHistoryOrder(String address, String city, String date, String totalPrice, String name, String phone, String state, String time) {
        this.address = address;
        this.city = city;
        this.date = date;
        this.totalPrice = totalPrice;
        this.name = name;
        this.phone = phone;
        this.state = state;
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
