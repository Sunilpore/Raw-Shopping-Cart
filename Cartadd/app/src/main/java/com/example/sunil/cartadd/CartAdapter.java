package com.example.sunil.cartadd;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Sunil on 11/21/2017.
 */

public class CartAdapter extends BaseAdapter {

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

        CartViewHolder vch;
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

        CartModel currentCart= (CartModel) getItem(i);

        String prize="Rs."+currentCart.getProdItem().getProdprice();
        Toast.makeText(mContext,"Cartprize:"+currentCart.getProdItem().getProdprice(),Toast.LENGTH_LONG).show();

        vch.cartProdname.setText(currentCart.getProdItem().getProdname());
        vch.cartPrize.setText(prize);
        vch.cartItemQty.setText(String.valueOf(currentCart.cartquantity));


        vch.incrQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Toast.makeText(mContext,"",Toast.LENGTH_LONG).show();
            }
        });


        vch.decrQty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        vch.cartDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        return view;
    }

    private class CartViewHolder {

        TextView cartProdname,cartPrize,cartItemQty;
        Button incrQty,decrQty,cartDel;
    }

}
