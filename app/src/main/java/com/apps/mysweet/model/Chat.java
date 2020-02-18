package com.apps.mysweet.model;

import com.google.firebase.Timestamp;

public class Chat {
    private String message;
    private String idBranch;
    private String idUser;
    private Timestamp time;
    private String sender;

    public  Chat(){ }
    public  Chat(String message,String idUser,String idBranch,Timestamp time,String sender ){
         this.message=message;
         this.idBranch=idBranch;
         this.idUser=idUser;
         this.time=time;
         this.sender=sender;

    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getIdBranch() {
        return idBranch;
    }

    public void setIdBranch(String idBranch) {
        this.idBranch = idBranch;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
