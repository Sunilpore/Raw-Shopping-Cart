package com.example.sunil.cartadd;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sunil on 11/10/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private static String tag = "myTag";
    Context mContext;

    //Create Database Version
    public static final int DATABASE_VERSION=1;

    //Define Database Name
    public static final String DATABASE_NAME="User.db";

    //Create Table
    public static final String TABLE_NAME_USER="user_table";
    public static final String TABLE_NAME_PROD="product_table";
    public static final String TABLE_NAME_CART="cart_table";

    //Create coloumns

    //For Table 1
    public static final String COL_ID ="USER_ID";
    public static final String COL_FULLNAME ="FULLNAME";
    public static final String COL_USERNAME ="NAME";
    public static final String COL_PASS ="PASS";

    //For Table 2
    public static final String COL_PROD_ID="PRODUCT_ID";
    public static final  String COL_PROD_NAME="PRODUCT_NAME";
    public static final  String COL_PROD_PRICE="PRODUCT_PRICE";


    //For Table 3
    public static final String COL_CART_ID="CART_ID";
    public static final String COL_CART_QUANTITY="CART_QTY";
    public static final String COL_CART_USERID="CART_USERID";
    public static final String COL_CART_PRODID="CART_PRODID";

    //Here pass 'DATABASE_NAME' instead of 'TABLE_NAME'
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext=context;
    }


    //Create query
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Define query For Table 1
        String CREATE_TABLE_USER="CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_USER +
                "(" +COL_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_FULLNAME + " TEXT,"
                + COL_USERNAME + " TEXT UNIQUE,"
                + COL_PASS +" TEXT" + ")";

        //Define query For Table 2
        String CREATE_TABLE_PROD="CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_PROD +
                "(" +COL_PROD_ID +" INTEGER PRIMARY KEY,"
                + COL_PROD_NAME + " TEXT,"
                + COL_PROD_PRICE +" INT" + ")";

        //Define query For Table 3
        String CREATE_TABLE_CART="CREATE TABLE IF NOT EXISTS "+ TABLE_NAME_CART +
                "(" +COL_CART_ID +" INTEGER PRIMARY KEY,"
                +COL_CART_QUANTITY +" INTEGER DEFAULT '1',"
                + COL_CART_USERID + " INTEGER,"
                + COL_CART_PRODID + " INTEGER,"
                + " FOREIGN KEY " + "(" + COL_CART_USERID + ")" + " REFERENCES " + TABLE_NAME_USER + "(" + COL_ID + ")"
                + " FOREIGN KEY " + "(" + COL_CART_PRODID + ")" + " REFERENCES " + TABLE_NAME_PROD + "(" + COL_PROD_ID + ")" +")";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PROD);
        db.execSQL(CREATE_TABLE_CART);
    }



    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_PROD);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME_CART);
        onCreate(db);
    }


    //Add data for UserModel For signin
    public boolean addUserData(UserModel umd){

        try{
            SQLiteDatabase db=this.getWritableDatabase();

            ContentValues value=new ContentValues();
            value.put(COL_FULLNAME,umd.getFullname());
            value.put(COL_USERNAME,umd.getUname());
            value.put(COL_PASS,umd.getPass());

            long result=db.insertOrThrow(TABLE_NAME_USER,null,value);
            if(result == -1)
                return false;
            else
                return true;

        }catch(android.database.sqlite.SQLiteConstraintException e){
            Toast.makeText(mContext,"User name Already Exist",Toast.LENGTH_LONG).show();
        }

    }


    public boolean addProductData(ProductModel pmd){

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues value=new ContentValues();
        value.put(COL_PROD_NAME,pmd.getProdname());
        value.put(COL_PROD_PRICE,pmd.getProdprice());

        long result=db.insert(TABLE_NAME_PROD,null,value);
        if(result==-1)
            return false;
        else
            return true;
    }

    public boolean addtoCart(CartModel cmd){

        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues value=new ContentValues();
        value.put(COL_CART_QUANTITY,cmd.getCartquantity());
        value.put(COL_CART_USERID,cmd.getUserid());
        value.put(COL_CART_PRODID,cmd.getProdid());

        long result=db.insert(TABLE_NAME_CART,null,value);
        if(result==-1)
            return false;
        else
            return true;
    }


    public Cursor getAllUserData(String username){

        SQLiteDatabase db=this.getWritableDatabase();

        Cursor cur=db.rawQuery("select "+COL_ID+" = "+username+ "from "+TABLE_NAME_USER,null);
        return cur;
    }

    public Cursor getAllProductData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select * from "+TABLE_NAME_PROD,null);

        return cur;

    }

    public Cursor getAllCartData(){
        SQLiteDatabase db=this.getWritableDatabase();

        CartModel cmd=new CartModel();
        String cartJointQuery="SELECT * FROM " + TABLE_NAME_CART
                + " JOIN " + TABLE_NAME_USER
                + " ON " + TABLE_NAME_CART + "." + COL_CART_USERID + " = " + TABLE_NAME_USER + "." + COL_ID
                + " JOIN " + TABLE_NAME_PROD
                + " ON " + TABLE_NAME_CART + "." + COL_CART_PRODID + " = " + TABLE_NAME_PROD + "." + COL_PROD_ID
                + " WHERE "+ COL_ID+ " = " ;

        Cursor cur=db.rawQuery(cartJointQuery,null);
        return cur;
    }

    public ArrayList<ProductModel> getProductData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = getAllProductData();

        ArrayList<ProductModel> mlist = new ArrayList<>();


        if (cur != null) {
            while (cur.moveToNext()) {

                String prodname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int prodprice = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));

                mlist.add(new ProductModel(prodname,prodprice));
            }
        }
        return mlist;
    }

    public ArrayList<CartModel> getCartData(){

        Cursor cur=getAllCartData();

        ArrayList<CartModel> cartlist=new ArrayList<>();

        if(cur != null){
            while(cur.moveToNext()){

                String cartProdname=cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int cartItemQty=cur.getInt(cur.getColumnIndex(COL_CART_QUANTITY));

                Log.d(tag, cartProdname);
                cartlist.add(new CartModel(cartProdname,cartItemQty));
            }
        }
        return cartlist;
    }


}


 /*public ArrayList<ProductModel> getProductData(){
        ArrayList<ProductModel> plist =new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        String query="select * from "+TABLE_NAME_PROD;
        Cursor cur=db.rawQuery(query,null);
        //Cursor cur = getAllProductData();


        String prodname;
        int prodprice;
        if(cur !=null){
            if(cur.moveToFirst()) {
                do {

               *//* prodname = productModel.setProdname(cur.getString(cur.getColumnIndex(COL_PROD_NAME)));*//*
                    prodname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));

                    prodprice = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));

                    Log.d("allData", "product Name: " + prodname);
                    Log.d("allData", "product prize: " + String.valueOf(prodprice));

                    plist.add(new ProductModel(prodname, prodprice));
                } while (cur.moveToNext());
            }
        }
        return plist;
    }*/