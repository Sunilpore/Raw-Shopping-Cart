package com.example.sunil.cartadd;

public class CartModel {

     public int cartid,userid,prodid,cartquantity;
     public String cartProdname;

     public CartModel(int userid, int prodid, int cartquantity) {
          this.userid = userid;
          this.prodid = prodid;
//          this.cartProdname = cartProdname;
          this.cartquantity=cartquantity;
     }

     public CartModel(String cartProdname, int cartquantity) {
          this.cartquantity = cartquantity;
          this.cartProdname = cartProdname;
     }

    public CartModel() {
    }

    public int getCartid() {
          return cartid;
     }

     public void setCartid(int cartid) {
          this.cartid = cartid;
     }

     public int getUserid() {
          return userid;
     }

     public void setUserid(int userid) {
          this.userid = userid;
     }

     public int getProdid() {
          return prodid;
     }

     public void setProdid(int prodid) {
          this.prodid = prodid;
     }

     public int getCartquantity() {
          return cartquantity;
     }

     public void setCartquantity(int cartquantity) {
          this.cartquantity = cartquantity;
     }

     public String getCartProdname() {
          return cartProdname;
     }

     public void setCartProdname(String cartProdname) {
          this.cartProdname = cartProdname;
     }

}