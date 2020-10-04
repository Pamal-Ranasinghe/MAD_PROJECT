package com.example.testeasyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class EditCurrency extends AppCompatActivity {
    private static final String TAG = "EditCurrency";

    private EditText title,des,amount;
    private Button edit,unMark;
    private DBHandler dbHandler;
    private Context context;
    private Long updateDate;
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_to_do);

        context = this;
        dbHandler = new DBHandler(context);
        title = findViewById(R.id.updateTask);
        des = findViewById(R.id.updateDescription);
        amount = findViewById(R.id.updateAmount);
        mDisplayDate = findViewById(R.id.updateDate);
        edit  = findViewById(R.id.updateToDo);
        unMark = findViewById(R.id.unMarkToDo);
        unMark.setEnabled(false);


        final String id = getIntent().getStringExtra("id");
        Currency todo = dbHandler.getSingleToDo(Integer.parseInt(id));

        title.setText(todo.getTitle());
        des.setText(todo.getDescription());
        amount.setText(Double.toString(todo.getAmount()));
        mDisplayDate.setText(todo.getTargeted_date());



        if(todo.getFinished() > 0){
            unMark.setEnabled(true);
        }else{
            unMark.setBackgroundResource(R.drawable.rounded_button_disable);
        }

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validateTitle() | !validateDesc() | !validateAmount()){
                    return;
                }

                String titleText = title.getText().toString();
                String descText = des.getText().toString();
                double amountText = Double.parseDouble(amount.getText().toString());
                String targetDate = mDisplayDate.getText().toString();
                updateDate = System.currentTimeMillis();

                Currency currency = new Currency(Integer.parseInt(id),titleText,descText,amountText,updateDate,0,targetDate);
                int state = dbHandler.updateSingleToDo(currency);

                Toast.makeText(getApplicationContext(),"Task has been updated", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context,MainActivity.class));
                finish();
            }
        });

        unMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleText = title.getText().toString();
                String descText = des.getText().toString();
                double amountText = Double.parseDouble(amount.getText().toString());
                String targetDate = mDisplayDate.getText().toString();
                updateDate = System.currentTimeMillis();

                Currency currency = new Currency(Integer.parseInt(id),titleText,descText,amountText,updateDate,0,targetDate);
                int state = dbHandler.updateSingleToDo(currency);

                Toast.makeText(getApplicationContext(),"Task has been updated", Toast.LENGTH_SHORT).show();
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

                DatePickerDialog dialog = new DatePickerDialog(
                        EditCurrency.this,
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
        String val = des.getText().toString();
        if(val.isEmpty()){
            des.setError("Field cannot be empty");
            return false;
        }else{
            des.setError(null);
            return true;
        }
    }

    private Boolean validateAmount() {
        String val = amount.getText().toString();
        if (val.isEmpty()) {
            amount.setError("Field cannot be empty");
            return false;
        } else {
            amount.setError(null);
            return true;
        }
    }

}