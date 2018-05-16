package com.hitesh_sahu.retailapp.view.activities;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.hitesh_sahu.retailapp.R;

import java.util.ArrayList;
import java.util.List;

public class SellActivity extends AppCompatActivity{
    private RadioButton male,female;
    private TextView milktxt;
    private EditText breed, milkRec,Price,Age;
    private Spinner category, type;
    ArrayAdapter<String> dataAdapter,petAdapter;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_sell);
        category=(Spinner)findViewById(R.id.category);
        type=(Spinner)findViewById(R.id.type);
        milkRec=(EditText)findViewById(R.id.milkval);
        milktxt=(TextView)findViewById(R.id.milktext);
        milkRec.setVisibility(View.GONE);
        milktxt.setVisibility(View.GONE);
        type.setVisibility(View.GONE);
        List<String> animalList = new ArrayList<String>();
        animalList.add("Animal");
        animalList.add("Animal Accesories");
        animalList.add("Animal Food");
        animalList.add("Animal Medicine");

        List<String> petList = new ArrayList<String>();
        petList.add("Pet");
        petList.add("Pet Accesories");
        petList.add("Pet Food");
        petList.add("Pet Medicine");
        /* Assign the name array to that adapter and
        also choose a simple layout for the list items */
        dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, animalList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        petAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, petList);
        petAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);



        category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // your code here
                if(position==0){
                    type.setVisibility(View.GONE);
                    milkRec.setVisibility(View.GONE);
                    milktxt.setVisibility(View.GONE);
                }
                if(position==1){
                    type.setAdapter(dataAdapter);
                    type.setVisibility(View.VISIBLE);
                    milkRec.setVisibility(View.VISIBLE);
                    milktxt.setVisibility(View.VISIBLE);
                }else if(position==2){
                    type.setAdapter(petAdapter);
                    type.setVisibility(View.GONE);
                    milkRec.setVisibility(View.GONE);
                    milktxt.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });

    }

}
