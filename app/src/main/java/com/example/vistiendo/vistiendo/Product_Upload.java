package com.example.vistiendo.vistiendo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class Product_Upload extends Fragment {

    Upload_Click_Listner upload_listner;
    ImageView img1,img2,img3,img4;

    public interface Upload_Click_Listner{
        public void upload_image1(View v);
        public void upload_image2(View v);
        public void upload_image3(View v);
        public void upload_image4(View v);
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
}
