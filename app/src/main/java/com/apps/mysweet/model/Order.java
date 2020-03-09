package com.apps.mysweet.model;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Order {
    private int deliveryPrice;
    private String address;
  private Timestamp deliveryTime;
  private int totalAmount;
  private String userName;
   private ArrayList<String>listTitles;

    java.sql.Timestamp timestamp;
  public Order (){

   }
    public Order (String address,Timestamp deliveryTime,String userName){
    this.address=address;
    this.deliveryTime=deliveryTime;
    this.userName=userName;
    }
    public Order (String address,java.sql.Timestamp deliveryTime,String userName){
        this.address=address;
        this.timestamp=deliveryTime;
        this.userName=userName;
    }


        public int getDeliveryPrice() {
        return deliveryPrice;
    }

    public void setDeliveryPrice(int deliveryPrice) {
        this.deliveryPrice = deliveryPrice;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(Timestamp deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<String> getListTitles() {
        return listTitles;
    }

    public void setListTitles(ArrayList<String> listTitles) {
        this.listTitles = listTitles;
    }
}
