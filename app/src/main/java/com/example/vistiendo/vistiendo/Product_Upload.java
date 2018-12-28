package com.example.vistiendo.vistiendo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class Product_Upload extends Fragment {

    Upload_Click_Listner upload_listner;

    public interface Upload_Click_Listner{
    }

    public Product_Upload() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product__upload, container, false);
        return view;
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
}
