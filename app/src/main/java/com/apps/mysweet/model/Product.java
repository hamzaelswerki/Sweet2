package com.apps.mysweet.model;

import com.google.firebase.firestore.Exclude;

public class Product {

    private String productName;
    private String category;
    private String numberOfPersons;
    private String size;
    private int price;
    private int quantity;
    private int votes;
    private String percentageOfSugar;
    private String instrucations;
    private String ProductPhotoUrl;
    private boolean isFavorite;
    private boolean isBestProduct;
    private boolean isRecommended;
    @Exclude
    private
    String productId;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getNumberOfPersons() {
        return numberOfPersons;
    }

    public void setNumberOfPersons(String numberOfPersons) {
        this.numberOfPersons = numberOfPersons;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getPercentageOfSugar() {
        return percentageOfSugar;
    }

    public void setPercentageOfSugar(String percentageOfSugar) {
        this.percentageOfSugar = percentageOfSugar;
    }

    public String getInstrucations() {
        return instrucations;
    }

    public void setInstrucations(String instrucations) {
        this.instrucations = instrucations;
    }

    public String getProductPhotoUrl() {
        return ProductPhotoUrl;
    }

    public void setProductPhotoUrl(String productPhotoUrl) {
        ProductPhotoUrl = productPhotoUrl;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public boolean isBestProduct() {
        return isBestProduct;
    }

    public void setBestProduct(boolean bestProduct) {
        isBestProduct = bestProduct;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    @Exclude
    public String getProductId() {
        return productId;
    }

    @Exclude
    public void setProductId(String productId) {
        this.productId = productId;
    }
}
