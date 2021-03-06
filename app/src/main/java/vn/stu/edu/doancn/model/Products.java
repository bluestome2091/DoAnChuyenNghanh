package vn.stu.edu.doancn.model;

import java.io.Serializable;

public class Products implements Serializable {
    private String id, count, name, category, price, date, image, description, pid, time, size;

    public Products() {
    }

    public Products(String id, String count, String name, String category, String price, String date, String image, String description, String pid, String time, String size) {
        this.id = id;
        this.count = count;
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = date;
        this.image = image;
        this.description = description;
        this.pid = pid;
        this.time = time;
        this.size = size;
    }

    public Products(String count, String name, String category, String price, String date, String image, String description, String pid, String time) {
        this.count = count;
        this.name = name;
        this.category = category;
        this.price = price;
        this.date = date;
        this.image = image;
        this.description = description;
        this.pid = pid;
        this.time = time;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
