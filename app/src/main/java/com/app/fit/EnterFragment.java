package com.app.fit;

import android.content.Context;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

import github.chenupt.springindicator.viewpager.ScrollerViewPager;

/**
 * Created by chenupt@gmail.com on 2015/1/31.
 * Description TODO
 */
public class EnterFragment extends Fragment {

    private int bgRes;
    int value;
    Button button;
    MaterialEditText amountEdit;
    MaterialEditText descriptionEdit;

    public static EnterFragment newInstance(int value){
        EnterFragment fragment = new EnterFragment();
        Bundle args = new Bundle();
        args.putInt("value", value );
        fragment.setArguments(args);

        return fragment;
    }

    public EnterFragment() {


    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bgRes = getArguments().getInt("data");

        if (getArguments() != null) {
            value = getArguments().getInt("value");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.enter, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ScrollerViewPager viewPager = getActivity().findViewById(R.id.view_pager);
         button = getView().findViewById(R.id.activity_sign_up_button_continue);
         amountEdit  = getView().findViewById(R.id.activity_sign_up_material_password);
         descriptionEdit  = getView().findViewById(R.id.activity_sign_up_material_name);


         button.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if (amountEdit.getText().toString().isEmpty() || descriptionEdit.getText().toString().isEmpty()){
                     showToastInMiddle("Description Or Amount Cannot be empty");
                 }else {
                     viewPager.setCurrentItem(1);




                 }
             }
         });

    }

    public void showToastInMiddle(String message){
        Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_LONG);
        TextView v = toast.getView().findViewById(android.R.id.message);
        if( v != null) v.setGravity(Gravity.CENTER);
        toast.show();
    }


}


