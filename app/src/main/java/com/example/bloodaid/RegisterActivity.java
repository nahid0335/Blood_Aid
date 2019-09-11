package com.example.bloodaid;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinner;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spinner = findViewById(R.id.regSpinner);
        checkBox = findViewById(R.id.ckboxDonar);


        /*List<String> districtName = new ArrayList<>();
        districtName.add(0,"Choose District");
        districtName.add("Dhaka");
        districtName.add("Khulna");
        districtName.add("Chitagong");
        districtName.add("Rungpur");
        districtName.add("Rajshahi");
        districtName.add("sylhet");*/

        //style and populate the spiner
        ArrayAdapter adapter = ArrayAdapter.createFromResource(
                this,
                R.array.Spinner_items,
                R.layout.color_spinner_layout
        );
        //dropdown style design
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_layout);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);




        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkBox.isChecked())
                {
                    checkBox.setTextColor(getResources().getColor(R.color.green));
                }
                else
                {
                    checkBox.setTextColor(getResources().getColor(R.color.red));
                }
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        if(adapterView.getItemAtPosition(i).equals("Choose District"))
        {
            //do nothing
        }
        else
        {
            //select spinner item
            String item = adapterView.getItemAtPosition(i).toString();
            Toast.makeText(this,item,Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
