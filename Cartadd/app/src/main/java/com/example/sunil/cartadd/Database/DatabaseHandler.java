package com.example.sunil.cartadd.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.sunil.cartadd.Model.CartModel;
import com.example.sunil.cartadd.Model.ProductModel;
import com.example.sunil.cartadd.Model.UserModel;

import java.util.ArrayList;

/**
 * Created by Sunil on 11/10/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String MyprefK = "Prefkey";
    public static final String UserIDK = "UserIDkey";
    /*public static final String ProductIDK = "ProductIDkey";*/

    SharedPreferences sp;
    SharedPreferences.Editor ed;

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
                + COL_CART_PRODID + " INTEGER UNIQUE,"
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
        boolean addCheck=false;

        try{
            SQLiteDatabase db=this.getWritableDatabase();

            ContentValues value=new ContentValues();
            value.put(COL_FULLNAME,umd.getFullname());
            value.put(COL_USERNAME,umd.getUname());
            value.put(COL_PASS,umd.getPass());

            long result=db.insertOrThrow(TABLE_NAME_USER,null,value);
            if(result == -1)
                addCheck=false;
            else
                addCheck=true;

        }catch(android.database.sqlite.SQLiteConstraintException e){
            Toast.makeText(mContext,"User name Already Exist",Toast.LENGTH_LONG).show();
        }
        return addCheck;
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

        boolean addCheck=false;

        try{

            SQLiteDatabase db=this.getWritableDatabase();

            ContentValues value=new ContentValues();
            value.put(COL_CART_QUANTITY,cmd.getCartquantity());
            value.put(COL_CART_USERID,cmd.getUserid());
            value.put(COL_CART_PRODID,cmd.getProdid());

            long result=db.insertOrThrow(TABLE_NAME_CART,null,value);
            if(result==-1)
                addCheck=false;
            else
                addCheck=true;

        }catch(android.database.sqlite.SQLiteConstraintException e){
            Toast.makeText(mContext,"Product already added into the Cart",Toast.LENGTH_LONG).show();
        }

        return addCheck;
    }

    public boolean  qtyUpdate(int userid, int cartid, int qty){

        SQLiteDatabase db=this.getWritableDatabase();

        new CartModel(userid,cartid,qty);
       /* Toast.makeText(mContext,"At Database value of cartquantity:"+qty,Toast.LENGTH_LONG).show();*/

        ContentValues value=new ContentValues();
        value.put(COL_CART_QUANTITY,qty);

        long result=db.update(TABLE_NAME_CART,value,"CART_ID=? AND CART_USERID=?",new String[]{String.valueOf(cartid), String.valueOf(userid)});
        if(result==-1)
            return false;
        else
            return true;

    };

    public boolean cartItemdelete(int userid,int cartid){

        SQLiteDatabase db=this.getWritableDatabase();

       int result=db.delete(TABLE_NAME_CART,"CART_ID=? AND CART_USERID=?",new String[]{String.valueOf(cartid), String.valueOf(userid)});
        if(result>0)
            return true;
        else
            return false;

    };

    public Cursor getAllUserData(String username,String password){

        SQLiteDatabase db=this.getWritableDatabase();

        String loginQuery="SELECT " + COL_ID + " FROM " + TABLE_NAME_USER + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASS + " = ?";

        Cursor cur=db.rawQuery(loginQuery,new String[]{username,password});


        return cur;
    }


    //This method is call for view Login user's SignUp Record on Login page
    public Cursor getAllUserData(){

        SQLiteDatabase db=this.getWritableDatabase();

        Cursor cur=db.rawQuery("select * from "+TABLE_NAME_USER,null);
        return cur;
    }

    public Cursor getAllProductData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select * from "+TABLE_NAME_PROD,null);

        return cur;

    }


    public Cursor getAllCartData(){

        sp=mContext.getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
        ed=sp.edit();

        int useridSP=sp.getInt(UserIDK,0);

        SQLiteDatabase db=this.getWritableDatabase();

        String cartJointQuery="SELECT * FROM " + TABLE_NAME_CART
                + " JOIN " + TABLE_NAME_USER
                + " ON " + TABLE_NAME_CART + "." + COL_CART_USERID + " = " + TABLE_NAME_USER + "." + COL_ID
                + " JOIN " + TABLE_NAME_PROD
                + " ON " + TABLE_NAME_CART + "." + COL_CART_PRODID + " = " + TABLE_NAME_PROD + "." + COL_PROD_ID
                + " WHERE "+ COL_ID + " =?";

        //Important step.At here we can use UserID in where clause for individual record
        Cursor cur=db.rawQuery(cartJointQuery,new String[]{String.valueOf(useridSP)});
        return cur;
    }

    public ArrayList<ProductModel> getProductData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = getAllProductData();

        ArrayList<ProductModel> mlist = new ArrayList<>();

        if (cur != null) {
            /*while (cur.moveToNext()) {

                String prodname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int prodprice = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));

                mlist.add(new ProductModel(prodname,prodprice));
            }*/

            cur.moveToFirst();
            do{
                String prodname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int prodprice = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));

                mlist.add(new ProductModel(prodname,prodprice));
            }while(cur.moveToNext());
        }
        return mlist;
    }

    public ArrayList<CartModel> getCartData(/*int useridSP*/){

        Cursor cur=getAllCartData();

        //CartModel cmd=new CartModel();
        ProductModel pmd;
        ArrayList<CartModel> cartlist=new ArrayList<>();

        if(cur != null){
            while(cur.moveToNext()){

                String cartProdname=cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int cartProdprize=cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));
                int cartItemQty=cur.getInt(cur.getColumnIndex(COL_CART_QUANTITY));

                /*cmd.setCartquantity(cur.getInt(cur.getColumnIndex(COL_CART_QUANTITY)));

                pmd.setProdname(cur.getString(cur.getColumnIndex(COL_PROD_NAME)));
                cmd.setProdItem(pmd);

                cartlist.add(cmd);*/

                Log.d(tag, cartProdname);

                pmd=new ProductModel(cartProdname,cartProdprize);
                cartlist.add(new CartModel(pmd,cartItemQty));
            }
        }
        return cartlist;
    }

}