package com.apps.mysweet.model;

import com.google.firebase.firestore.Exclude;

public class Branch {
    private String name;
    private Double _long;
    private Double lat;
    private String address;
    @Exclude
    private String idBranch;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double get_long() {
        return _long;
    }

    public void set_long(Double _long) {
        this._long = _long;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Exclude
    public String getIdBranch() {
        return idBranch;
    }
    @Exclude
    public void setIdBranch(String idBranch) {
        this.idBranch = idBranch;
    }
}
