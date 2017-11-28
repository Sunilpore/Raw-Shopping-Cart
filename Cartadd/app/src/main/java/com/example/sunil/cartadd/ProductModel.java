package com.example.sunil.cartadd;

/**
 * Created by Sunil on 11/13/2017.
 */

public class ProductModel {

    public int pid,prodprice;
    public String prodname;
    public boolean clickbutton;

    public ProductModel() {
    }

    public ProductModel(String prodname) {
        this.prodname = prodname;
    }
/*
    public ProductModel(String prodname) {
        this.prodname = prodname;
    }*/

    /*public ProductModel(String prodname, int prodprice) {
        this.prodname = prodname;
        this.prodprice = prodprice;
    }*/

    public ProductModel( String prodname , int prodprice) {
        this.prodname = prodname;
        this.prodprice = prodprice;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getProdname() {
        return prodname;
    }

    public void setProdname(String prodname) {
        this.prodname = prodname;
    }

    public int getProdprice() {
        return prodprice;
    }

    public void setProdprice(int prodprice) {
        this.prodprice = prodprice;
    }

    public boolean isClickbutton() {
        return clickbutton;
    }

    public void setClickbutton(boolean clickbutton) {
        this.clickbutton = clickbutton;
    }

}
