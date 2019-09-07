package com.kh_sof_dev.chris_fries.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kh_sof_dev.chris_fries.R;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kh_sof_dev.chris_fries.Clasess.Product;
import com.kh_sof_dev.chris_fries.Clasess.Request;

public class Products_adapter extends RecyclerView.Adapter<Products_adapter.ViewHolder> {

    public interface onEditeListenner{
       void Onselected(Product product);
    }
    private static final String TAG = "RecyclerViewAdapter";

    //vars
    private List<Product> mItems = new ArrayList<>();

    private Context mContext;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseAuth auth;
private onEditeListenner mlistenner;
    public Products_adapter(Context context, List<Product> names, onEditeListenner listenner) {
        mItems = names;
        mContext = context;
        mlistenner=listenner;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Requests").child("Waite");
        auth=FirebaseAuth.getInstance();

    }
    private View mView;
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) { //parent = theme type
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_produt_items, parent, false);
        mView=view;

        return new ViewHolder(view); // Inflater means reading a layout XML
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");
try{
        holder.name.setText(mItems.get(position).getName());
}catch (Exception e){

}
try {
    holder.weight.setText(mItems.get(position).getWeight().toString() +" KG");
}catch (Exception e){

}
try{
        holder.price.setText(mItems.get(position).getPrice().toString() +" $");
}catch (Exception e){

}

        holder.bay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
bay_popup(position);
            }
        });

    }

    private void bay_popup(final int position) {
        final Dialog dialog=new Dialog(mContext);
        dialog.setContentView(R.layout.popup_bay);
        final EditText count=dialog.findViewById(R.id.order_count);
        final EditText talif=dialog.findViewById(R.id.order_talif);
        Button cancel=dialog.findViewById(R.id.cancel);
        Button save=dialog.findViewById(R.id.bay);
        dialog.show();
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                double count_=Double.parseDouble(count.getText().toString());
                double talif_=Double.parseDouble(talif.getText().toString());
                if (count.getText().toString().isEmpty() || count_==0.0){
                    count.setError(count.getHint());
                    return;
                }
                if (count.getText().toString().isEmpty() ){
                    talif.setError(talif.getHint());
                    return;
                }



                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date = new Date();


                Request request=new Request(dateFormat.format(date)
                        ,mItems.get(position).getName(),
                        mItems.get(position).getWeight()
                ,mItems.get(position).getPrice(),count_,talif_);

                reference.child(auth.getCurrentUser().getUid()).push().setValue(request);
                Toast.makeText(mContext,mContext.getString(R.string.ur_req_succ),Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView name,weight,price;
   Button bay;
        public ViewHolder(View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.prod_name);
            weight=itemView.findViewById(R.id.wight);
            price=itemView.findViewById(R.id.price);


            bay=itemView.findViewById(R.id.bay);


        }
    }
}