package com.apps.mysweet.model;

import com.google.firebase.firestore.Exclude;

public class User {

   private String userName;
   private String emailAddress;
   private String phoneNumber;
   private String PhotoUrl;
  // private Date dateOfBirth;

    @Exclude
    private String UserId;
   public User(){};

    public User(String userName, String emailAddress, String phoneNumber, String photoUrl) {
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        PhotoUrl = photoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUrl() {
        return PhotoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        PhotoUrl = photoUrl;
    }

  /*  public Date getDateOfBirth() {
        return dateOfBirth;

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }*/
  @Exclude
   public String getUserId() {
        return UserId;
    }
    @Exclude
    public void setUserId(String userId) {
        UserId = userId;
    }
}
