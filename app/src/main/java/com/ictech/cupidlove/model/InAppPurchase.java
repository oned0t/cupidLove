package com.ictech.cupidlove.model;

import java.util.ArrayList;
import java.util.List;

public class InAppPurchase {

    private String sku,price,name, description;

    public InAppPurchase(String sku, String price, String name, String description) {
        this.sku = sku;
        this.price = price;
        this.name = name;
        this.description = description;
    }

    public static List<InAppPurchase> list = new ArrayList();


    public static List<InAppPurchase> getList() {
        return list;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
