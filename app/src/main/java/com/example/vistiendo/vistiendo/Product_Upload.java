package com.example.vistiendo.vistiendo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Product_Upload extends Fragment {

    Upload_Click_Listner upload_listner;
    ImageView img1,img2,img3,img4;
    Button add_btn;
    String pro_name,pro_type,pro_category,pro_qty,pro_price,pro_seller,id;
    private EditText name,qty,price,seller;
    private Spinner category,type;
    public interface Upload_Click_Listner{
        public void upload_image1(View v);
        public void upload_image2(View v);
        public void upload_image3(View v);
        public void upload_image4(View v);
        public void upload_product(String pro_name,String pro_price, String pro_qty, String pro_category,String pro_type,String pro_seller, View v);
    }

    public Product_Upload() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product__upload, container, false);
        img1 = view.findViewById(R.id.new_post_image1);
        img2 = view.findViewById(R.id.new_post_image2);
        img3 = view.findViewById(R.id.new_post_image3);
        img4 = view.findViewById(R.id.new_post_image4);
        add_btn = view.findViewById(R.id.add_btn);
        name = view.findViewById(R.id.product_name);
        price = view.findViewById(R.id.product_price);
        qty = view.findViewById(R.id.product_quantity);
        seller = view.findViewById(R.id.mail);
        category = view.findViewById(R.id.gender);
        type = view.findViewById(R.id.type);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_image1(view);
            }
        });

        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_image2(view);
            }
        });

        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_image3(view);
            }
        });

        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_image4(view);
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pro_name = name.getText().toString();
                pro_price = price.getText().toString();
                pro_qty = qty.getText().toString();
                pro_category = category.getSelectedItem().toString();
                pro_seller = seller.getText().toString();
                pro_type = type.getSelectedItem().toString();

                if(pro_name.length() == 0){
                    name.setError("Can't be empty");
                    name.requestFocus();
                    return;
                }

                if(pro_price.length() == 0){
                    price.setError("Can't be empty");
                    price.requestFocus();
                    return;
                }

                if(pro_qty.length() == 0){
                    qty.setError("Can't be empty");
                    qty.requestFocus();
                    return;
                }

                if(pro_seller.length()==0){
                    seller.setError("You must enter the valid email of seller");
                    seller.requestFocus();
                    return;
                }

                if(!(Main_Activity.upload1!=null || Main_Activity.upload2!=null || Main_Activity.upload3!=null || Main_Activity.upload4!=null )){
                   // doing toast for the images
                    return;
                }

                RequestQueue queue = Volley.newRequestQueue(getContext());
                String url ="https://us-central1-vistiendo-deecb.cloudfunctions.net/getUID?email="+pro_seller;

                ProgressDialog pd = new ProgressDialog(getContext());
                pd.setMessage("validating details");
                pd.show();

                StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                id = response;
                                pd.hide();
                                queue.stop();
                                upload(view);
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pd.hide();
                        queue.stop();
                        Toast.makeText(getContext(),"Invalid mail id of the seller",Toast.LENGTH_SHORT).show();
                    }
                });
                queue.start();
                queue.add(stringRequest);
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Main_Activity.upload1!=null){
            img1.setImageURI(Main_Activity.upload1);
        }
        if(Main_Activity.upload2!=null){
            img2.setImageURI(Main_Activity.upload2);
        }
        if(Main_Activity.upload3!=null){
            img3.setImageURI(Main_Activity.upload3);
        }
        if(Main_Activity.upload4!=null){
            img4.setImageURI(Main_Activity.upload4);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            upload_listner=(Product_Upload.Upload_Click_Listner) activity;
        }catch (ClassCastException e) {
            throw new ClassCastException(activity.toString());
        }
    }

    private void add_image1(View v){
        upload_listner.upload_image1(v);
    }

    private void add_image2(View v){
        upload_listner.upload_image2(v);
    }

    private void add_image3(View v){
        upload_listner.upload_image3(v);
    }

    private void add_image4(View v){
        upload_listner.upload_image4(v);
    }

    private void upload(View view) {
        upload_listner.upload_product(pro_name,pro_price,pro_qty,pro_category,pro_type,id,view);
    }
}
