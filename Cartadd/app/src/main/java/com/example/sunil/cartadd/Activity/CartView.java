package com.example.sunil.cartadd.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sunil.cartadd.Adapter.CartAdapter;
import com.example.sunil.cartadd.Model.CartModel;
import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.R;

import java.util.ArrayList;

public class CartView extends AppCompatActivity {

    private final String PLUS="INCREMENT";
    private final String MINUS="DECREMENT";
    private final String DEL="DELETE";

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

        cartlist=db.getCartData();
        ctadapter=new CartAdapter(mContext,cartlist);
        cartlv.setAdapter(ctadapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mContext.registerReceiver(A,new IntentFilter(PLUS));
        mContext.registerReceiver(A,new IntentFilter(MINUS));
        mContext.registerReceiver(A,new IntentFilter(DEL));
    }

    BroadcastReceiver A=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(PLUS) || intent.getAction().equals(MINUS) ||  intent.getAction().equals(DEL)){

                Toast.makeText(mContext,"BroadcastReciever",Toast.LENGTH_SHORT).show();
                ctadapter.notifyDataSetChanged();

                //This part is necessary.If you skip this one,then your textview is not update untill you not restart the activity again
                {
                    cartlist=db.getCartData();
                    ctadapter=new CartAdapter(mContext,cartlist);
                    cartlv.setAdapter(ctadapter);
                }

                //If you are not using above code then use it for refresh the value for textview i.e Cart Product Quantity
               /* Intent i = getIntent();
                finish();
                startActivity(i);*/
            }

        }
    };


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