package com.example.reclaim;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.ColumnInfo;

import java.util.Date;

@Entity(tableName = "user_details_entries")
public class UserEntry {
    @PrimaryKey(autoGenerate = true)
    public int userid;
    @ColumnInfo(name = "name")
    public String uname;
    @ColumnInfo(name = "age")
    public int age;
    @ColumnInfo(name = "height")
    public float height;

    @ColumnInfo(name = "weight")
    public float weight;

    @ColumnInfo(name = "email")
    public String email;

    @ColumnInfo(name = "phoneNo")
    public long  phoneNo;
    @ColumnInfo(name = "date")
    public Date date;
    public UserEntry(String uname,int age,float height,float weight,String email, long phoneNo,Date date) {
        this.uname = uname;
        this.age = age;
        this.height=height;
        this.weight=weight;
        this.email = email;
        this.phoneNo=phoneNo;
        this.date = date;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(long phoneNo) {
        this.phoneNo = phoneNo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}