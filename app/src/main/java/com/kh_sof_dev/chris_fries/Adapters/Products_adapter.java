package com.kh_sof_dev.chris_fries.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
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

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kh_sof_dev.chris_fries.Clasess.Notifi;
import com.kh_sof_dev.chris_fries.R;


import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.kh_sof_dev.chris_fries.Clasess.Product;
import com.kh_sof_dev.chris_fries.Clasess.Request;

import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.MODE_PRIVATE;

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
private String token;
    public Products_adapter(Context context, List<Product> names, onEditeListenner listenner) {
        mItems = names;
        mContext = context;
        mlistenner=listenner;
        database=FirebaseDatabase.getInstance();
        reference=database.getReference("Requests").child("Waite");
        auth=FirebaseAuth.getInstance();
        DatabaseReference tokenref=database.getReference("Notification").child("Token");
        tokenref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    token=dataSnapshot.getValue(String.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
    holder.weight.setText(mItems.get(position).getWeight().toString() +" pcs");
}catch (Exception e){

}
try{
        holder.price.setText(mItems.get(position).getPrice().toString() +" EGP");
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





                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd  HH:mm");
                Date date = new Date();

                Double cnt=0.0,tlf=0.0;

                try {
                    cnt=Double.parseDouble(count.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }

                try {
                   tlf= Double.parseDouble(talif.getText().toString());
                }catch (Exception e){
                    e.printStackTrace();
                }
                if (count.getText().toString().isEmpty()  || cnt==0.0 ){
                    count.setError(count.getHint());
                    return;
                }
                if (talif.getText().toString().isEmpty() ){
                    talif.setError(talif.getHint());
                    return;
                }

Double priceTOT=mItems.get(position).getPrice()*cnt
        -
        mItems.get(position).getPrice()*tlf;

                Request request=new Request(dateFormat.format(date)
                        ,mItems.get(position).getName()+"( "+mItems.get(position).getWeight()+" KG )",
                        mItems.get(position).getWeight()
                ,priceTOT,cnt,tlf);
                request.setProd_id(mItems.get(position).getId());
                 create_request(request);


                dialog.dismiss();
            }
        });
    }

    private void create_request(final Request request) {
        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference_nb=database.getReference("App_number").child("requests_nb");
        reference_nb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int nb=dataSnapshot.getValue(int.class);
                    add_newrequest(nb,request);
                }else {
                    add_newrequest(0,request);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void add_newrequest(int i, Request request) {
        final DecimalFormat decimalFormat = new DecimalFormat("000000");
request.setNb(decimalFormat.format(i+1));

        reference.child(auth.getCurrentUser().getUid()).push().setValue(request);

        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference_nb=database.getReference("App_number").child("requests_nb");
        reference_nb.setValue(i+1);

        Toast.makeText(mContext,mContext.getString(R.string.ur_req_succ),Toast.LENGTH_LONG).show();
        add_request();
        add_wallet(request.getPrice());
        try {
            Post_notificition(request.getProduct());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
private void add_request(){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Users").child(auth.getCurrentUser().getUid());
        reference.child("request_wail_nb").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    int nb=dataSnapshot.getValue(int.class)+1;
                    reference.child("request_wail_nb").setValue(nb);
                }else {
                    reference.child("request_wail_nb").setValue(1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
}
    private void add_wallet(final Double newPrice){
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference reference=database.getReference("Users").child(auth.getCurrentUser().getUid());
        reference.child("wallet").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Double wallet=dataSnapshot.getValue(Double.class)+newPrice;
                    reference.child("wallet").setValue(wallet);
                }else {
                    reference.child("wallet").setValue(newPrice);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void Post_notificition(String prodname) throws JSONException {
        Notifi notifi_=new Notifi();
        notifi_.setTitle(prodname);
        notifi_.setBody(mContext.getSharedPreferences("user_info", MODE_PRIVATE).getString("name"," ")
                +" "+mContext.getString(R.string.baypro));
        notifi_.setToken(token);


        RequestQueue queue = Volley.newRequestQueue(mContext);
        String url ="https://fcm.googleapis.com/fcm/send";

        // POST parameters

        JSONObject cart=notifi_.Notifi();

        Log.d("results",cart.toString());
// Request a json response from the provided URL
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, url, cart,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.d("results",jsonObject.toString());
                    }
                }, new Response.ErrorListener (){

            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        }
        ){
            @Override
            public Map<String, String> getHeaders()  {
                Map<String, String>  Headers = new HashMap<String, String>();
                Headers.put("Authorization",
                        "key=" +
                                mContext.getString(R.string.notify_key)
                );
//

                return Headers;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
        // prepare the Request

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