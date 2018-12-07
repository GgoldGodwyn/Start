package com.app.fit;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.rengwuxian.materialedittext.MaterialEditText;

import github.chenupt.springindicator.viewpager.ScrollerViewPager;

/**
 * Created by chenupt@gmail.com on 2015/1/31.
 * Description TODO
 */
public class SelectPaymentFragment extends Fragment {

    private int bgRes;
    private ImageView imageView;
    int value;
    Button proceed;
    MaterialEditText editAccount;

    public static SelectPaymentFragment newInstance(int value){
        SelectPaymentFragment fragment = new SelectPaymentFragment();
        Bundle args = new Bundle();
        args.putInt("value", value );
        fragment.setArguments(args);

        return fragment;
    }

    public SelectPaymentFragment() {


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
        return inflater.inflate(R.layout.select_payment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Spinner staticSpinner = (Spinner)view.findViewById(R.id.dynamic_spinner);

        // Create an ArrayAdapter using the string array and a default spinner
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(getContext(), R.array.brew_array,
                        android.R.layout.simple_spinner_item);

        // Specify the layout to use when the list of choices appears
        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        staticSpinner.setAdapter(staticAdapter);

        staticSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });


        final ScrollerViewPager viewPager = getActivity().findViewById(R.id.view_pager);
        proceed  = getView().findViewById(R.id.select_proceed);
        editAccount  = getView().findViewById(R.id.account_number);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editAccount.getText().toString().isEmpty()){
                    showToastInMiddle("Account Number Cannot Be Empty");
                } else {
                    viewPager.setCurrentItem(3);

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


