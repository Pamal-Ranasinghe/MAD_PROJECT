package com.example.testeasyapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddCurrency extends AppCompatActivity {
    private static final String TAG = "AddCurrency";

    private EditText title,desc,amount;
    private Button add;
    private DBHandler dbHandler;
    private Context context;
    private EditText mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_to_do);

        title = findViewById(R.id.enterTask);
        desc = findViewById(R.id.enterDescription);
        amount = findViewById(R.id.enterAmount);
        add = findViewById(R.id.confirmAddToDo);
        mDisplayDate =findViewById(R.id.enterDate);
        context = this;

        dbHandler = new DBHandler(context);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateTitle() | !validateDesc() | !validateAmount()){
                    return;
                }

                String userTitle = title.getText().toString();
                String userDesc = desc.getText().toString();
                double userAmount = Double.parseDouble(amount.getText().toString());
                long started = System.currentTimeMillis();
                String useTargetedDate = mDisplayDate.getText().toString();

                Currency todo = new Currency(userTitle,userDesc,userAmount,started,0,useTargetedDate);
                dbHandler.addToDo(todo);

                Toast.makeText(getApplicationContext(),"New task has been added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context,MainActivity.class));
                finish();

            }
        });

        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                System.out.print(year +":"+month+":"+":"+day);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddCurrency.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG,"onDateSet: mm/dd/yyyy" + month + "/" + day + "/" +year);

                String date = month + "/"+ day +"/" +year;
                mDisplayDate.setText(date);
            }
        };




    }
    private Boolean validateTitle(){
        String val = title.getText().toString();
        if(val.isEmpty()){
            title.setError("Field cannot be empty");
            return false;
        }else{
            title.setError(null);
            return true;
        }
    }

    private Boolean validateDesc(){
        String val = desc.getText().toString();
        if(val.isEmpty()){
            desc.setError("Field cannot be empty");
            return false;
        }else{
            desc.setError(null);
            return true;
        }
    }

    private Boolean validateAmount(){
        String val = amount.getText().toString();
        if(val.isEmpty()) {
            amount.setError("Field cannot be empty");
            return false;
        }
        else{
            amount.setError(null);
            return true;
        }
    }


}