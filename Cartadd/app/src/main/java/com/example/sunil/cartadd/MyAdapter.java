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
 * Created by Sunil on 11/13/2017.
 */

class MyAdapter extends BaseAdapter{

    private Context mContext;
    private ArrayList<ProductModel> alist;
    LayoutInflater inflater;
    DatabaseHandler db;
    UpdateListener onUpdateListener;

    public MyAdapter(Context mContext, ArrayList<ProductModel> alist) {
        this.mContext=mContext;
        this.alist = alist;
        inflater=LayoutInflater.from(mContext);
        db=new DatabaseHandler(mContext);
    }

    public MyAdapter() {
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Object getItem(int i) {
        return alist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup vg) {
        final ViewHolder vh;
        if(view==null) {
            vh=new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lay, vg, false);

            vh.prodname=view.findViewById(R.id.tv_productname);
            vh.prodprice=view.findViewById(R.id.tv_prodprize);
            vh.click=view.findViewById(R.id.bt_click);
            view.setTag(vh);
        }
        else{
            vh= (ViewHolder) view.getTag();
        }

        final ProductModel current= (ProductModel) getItem(i);

        /*//This can also possible by using getter() method
    vh.prodname.setText(current.getProdname());
    vh.prodprice.setText(String.valueOf(current.getProdprice()));*/

        //Here we setText() by using constructor
        vh.prodname.setText(current.prodname);
        vh.prodprice.setText(String.valueOf(current.prodprice));

        vh.click.setTag(current);

        //It is neccessary else it will select multiple buttons
        if(current.isClickbutton()){
            vh.click.setEnabled(false);
        }
        else{
            vh.click.setEnabled(true);
        }

        vh.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button bt = (Button) view;

                ProductModel tmp = (ProductModel) bt.getTag();

                UserModel umd=new UserModel();
                ProductModel pmd=new ProductModel();

                boolean cartInserted=db.addtoCart(new CartModel(umd.id,pmd.pid,1));
                if(cartInserted){
                    tmp.setClickbutton(true);
                    Toast.makeText(mContext, "Add Button pressed", Toast.LENGTH_LONG).show();
                }

                notifyDataSetChanged();

                onUpdateListener.onUpdateListenernow(cartInserted,i);
                onUpdateListener.onItemaddViewListener(cartInserted,i);

            }
        });

        return view;
    }

    private class ViewHolder{
        TextView prodname,prodprice;
        Button click;

    }

    public void setOnItemListener(UpdateListener onUpdateListener){
        this.onUpdateListener=onUpdateListener;
    }

}
