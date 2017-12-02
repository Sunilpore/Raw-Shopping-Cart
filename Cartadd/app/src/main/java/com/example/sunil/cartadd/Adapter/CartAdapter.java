package com.example.sunil.cartadd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.Model.CartModel;
import com.example.sunil.cartadd.R;

import java.util.ArrayList;

/**
 * Created by Sunil on 11/21/2017.
 */

public class CartAdapter extends BaseAdapter {

    public static final String MyprefK = "Prefkey";
    public static final String UserIDK = "UserIDkey";

    SharedPreferences sp;
    SharedPreferences.Editor ed;

    private final String PLUS="INCREMENT";
    private final String MINUS="DECREMENT";
    private final String DEL="DELETE";

    Intent a,b,c;

    static int Qtychange;
    private Context mContext;
    private ArrayList<CartModel> cartlist;
    LayoutInflater inflater;
    DatabaseHandler db;

    public CartAdapter(Context mContext, ArrayList<CartModel> cartlist) {
        this.mContext=mContext;
        this.cartlist=cartlist;
        inflater=LayoutInflater.from(mContext);
        db=new DatabaseHandler(mContext);
    }

    @Override
    public int getCount() {
        return cartlist.size();
    }

    @Override
    public Object getItem(int i) {
        return cartlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup vg) {

        sp = mContext.getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
        ed = sp.edit();

        final CartViewHolder vch;
        if(view == null){

            vch= new CartViewHolder();

            view=LayoutInflater.from(mContext).inflate(R.layout.lay2, vg, false);

            vch.cartProdname=view.findViewById(R.id.tv_cartProdname);
            vch.cartPrize=view.findViewById(R.id.tv_cartPrize);
            vch.cartItemQty=view.findViewById(R.id.tv_cartQty);
            vch.incrQty=view.findViewById(R.id.bt_cartIncrement);
            vch.decrQty=view.findViewById(R.id.bt_cartDecrement);
            vch.cartDel=view.findViewById(R.id.bt_cartDelete);
            view.setTag(vch);
        }
        else{
            vch= (CartViewHolder) view.getTag();
        }

        final CartModel currentCart= (CartModel) getItem(i);

        int setprize=currentCart.getProdItem().getProdprice()*currentCart.cartquantity;
        String prize="Rs."+setprize;
//        Toast.makeText(mContext,"Cartprize:"+currentCart.getProdItem().getProdprice(),Toast.LENGTH_LONG).show();


        a=new Intent(PLUS);
        vch.incrQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int userid=sp.getInt(UserIDK,0);
                int qty=currentCart.cartquantity+1;

                /*Toast.makeText(mContext,"Qty. Before Update:"+currentCart.cartquantity+"\tCartid:"+(cartlist.indexOf(currentCart)+1),Toast.LENGTH_LONG).show();*/
                int cartid=cartlist.indexOf(currentCart)+1;
                Toast.makeText(mContext,"userid:"+userid,Toast.LENGTH_SHORT).show();


                boolean isQtyUpdate=db.qtyUpdate(userid,cartid,qty);

                /*if(isQtyUpdate)
                Toast.makeText(mContext,"Qty. increased\t After Update:"+currentCart.cartquantity,Toast.LENGTH_LONG).show();
                else
                Toast.makeText(mContext,"Qty. NOT increased",Toast.LENGTH_LONG).show();*/

                mContext.sendBroadcast(a);
                notifyDataSetChanged();
            }
        });


        b=new Intent(MINUS);
        vch.decrQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int userid=sp.getInt(UserIDK,0);
                int cartid=cartlist.indexOf(currentCart)+1;
                int qty=currentCart.cartquantity-1;

                if(qty>0){
                    boolean isQtyUpdate=db.qtyUpdate(userid,cartid,qty);
                }

                mContext.sendBroadcast(b);
                notifyDataSetChanged();
               /* Toast.makeText(mContext,"Qty. decreased",Toast.LENGTH_LONG).show();*/
            }
        });


        c=new Intent(DEL);
        vch.cartDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int userid=sp.getInt(UserIDK,0);

                int cartid=cartlist.indexOf(currentCart)+1;
                boolean isDeleted=db.cartItemdelete(userid,cartid);

                mContext.sendBroadcast(c);
                Toast.makeText(mContext,"Qty. deleted",Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
            }
        });

        vch.cartProdname.setText(currentCart.getProdItem().getProdname());
        vch.cartPrize.setText(prize);
        vch.cartItemQty.setText(String.valueOf(currentCart.cartquantity));


        return view;
    }



    private class CartViewHolder {

        TextView cartProdname,cartPrize,cartItemQty;
        Button incrQty,decrQty,cartDel;
    }

}