package com.example.sunil.cartadd.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.Adapter.MyAdapter;
import com.example.sunil.cartadd.Model.ProductModel;
import com.example.sunil.cartadd.R;
import com.example.sunil.cartadd.Interface.UpdateListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements UpdateListener {

    public static final String MyprefK="Prefkey";
    public static final String CheckK="Checkkey";

    String add;
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    ListView lv;
    CoordinatorLayout cordlay;
    MenuItem countview;
    DatabaseHandler db;
    MyAdapter adapter;
    ArrayList<ProductModel> plist;
    Context mContext;
    static int buttonCount,countstatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar ab=getSupportActionBar();
        ab.setLogo(R.drawable.tick);
        ab.setDisplayUseLogoEnabled(true);   //This method will enable your logo
        ab.setDisplayShowHomeEnabled(true);  //This method will enable your home

        mContext=this;
//        adapter=new MyAdapter();
        db=new DatabaseHandler(mContext);

        lv= (ListView) findViewById(R.id.list_view);
        cordlay= (CoordinatorLayout) findViewById(R.id.cord_lay);

        sp=getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
        ed=sp.edit();



        /*This method is use when you are not added item to Listview via Database.

        alist =new ArrayList<>();

        alist.add(new ProductModel("Poduct 1"));
        alist.add(new ProductModel("Poduct 2"));
        alist.add(new ProductModel("Poduct 3"));
        alist.add(new ProductModel("Poduct 4"));
        alist.add(new ProductModel("Poduct 5"));
        alist.add(new ProductModel("Poduct 6"));
        alist.add(new ProductModel("Poduct 7"));
        alist.add(new ProductModel("Poduct 8"));
        alist.add(new ProductModel("Poduct 9"));
        alist.add(new ProductModel("Poduct 10"));
        MyAdapter adapter=new MyAdapter(this,alist);
        lv.setAdapter(adapter);*/

        if(sp.getBoolean(CheckK,true)){
            db.addProductData(new ProductModel("Poduct 1",10));
            db.addProductData(new ProductModel("Poduct 2",20));
            db.addProductData(new ProductModel("Poduct 3",20));
            db.addProductData(new ProductModel("Poduct 4",20));
            db.addProductData(new ProductModel("Poduct 5",10));
            db.addProductData(new ProductModel("Poduct 6",10));
            db.addProductData(new ProductModel("Poduct 7",20));
            db.addProductData(new ProductModel("Poduct 8",20));
            db.addProductData(new ProductModel("Poduct 9",30));
            db.addProductData(new ProductModel("Poduct 10",30));
            db.addProductData(new ProductModel("Poduct 11",40));
            db.addProductData(new ProductModel("Poduct 12",50));
            db.addProductData(new ProductModel("Poduct 13",60));
            db.addProductData(new ProductModel("Poduct 14",60));
            db.addProductData(new ProductModel("Poduct 15",60));

            //Here used apply() instead of commit();
            //Because, commit() blocks and writes its data to persistent storage immediately,where apply() will handle data in background.

            ed.putBoolean(CheckK,false);
            ed.apply();
//           ed.commit();
        }

        plist=db.getProductData();
        adapter=new MyAdapter(mContext,plist);
        lv.setAdapter(adapter);

        adapter.setOnItemListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity,menu);
        countview=menu.findItem(R.id.count_id);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.count_id:

                Intent in=new Intent(HomeActivity.this,CartView.class);

                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);

                //When you call finish() method it will not hold the current status of 'ADD' button,while returning from CartView to HomeActivity
//                finish();
                break;

            case R.id.view_id:
                Toast.makeText(this,"Move to next Page for My Order",Toast.LENGTH_LONG).show();
                break;

            case R.id.logout_id:

                Intent i=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(i);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateListenernow(boolean status, int position) {

        if(status){
            buttonCount++;
            Snackbar.make(cordlay,buttonCount+" Product added in Cart",Snackbar.LENGTH_LONG).show();
        }
        else{

            Snackbar sn=Snackbar.make(cordlay,"Item is already added into the Cart",Snackbar.LENGTH_LONG);
            View snackbarView = sn.getView();
            snackbarView.setBackgroundColor(Color.RED);
            sn.show();
        }


    }

    public void onItemaddViewListener(boolean status, int position){

        if(status){
            countstatus++;

            add=String.valueOf(countstatus) + "Items Added";
            countview.setTitle(add);
        }

    }

}
