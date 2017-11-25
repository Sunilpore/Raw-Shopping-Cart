package com.example.sunil.cartadd;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class CartView extends AppCompatActivity {

    DatabaseHandler db;
    ListView cartlv;
    Context mContext;
    CartAdapter ctadapter;
    ArrayList<CartModel> cartlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartview);

        cartlv= (ListView) findViewById(R.id.cartlist_view);

        mContext=this;
        db=new DatabaseHandler(mContext);

        //Toast.makeText(getApplicationContext(),"DATA:"+db.getCartData()+" JOIN:"+db.getAllCartData(),Toast.LENGTH_LONG).show();

        cartlist=db.getCartData();
        ctadapter=new CartAdapter(mContext,cartlist);
        cartlv.setAdapter(ctadapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.order_activity,menu);

        ActionBar ab=getSupportActionBar();
        ab.setLogo(R.drawable.tick);
        ab.setDisplayUseLogoEnabled(true);   //This method will enable your logo

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);  //This method will enable your home

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.logout_id2:
                Intent i1=new Intent(CartView.this,MainActivity.class);
                startActivity(i1);
                finish();
                break;

              //without this also possible to call back previous intent Activity via Manifest
            case android.R.id.home:

                /*Intent i=new Intent(CartView.this,HomeActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                startActivity(i);*/
               onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
