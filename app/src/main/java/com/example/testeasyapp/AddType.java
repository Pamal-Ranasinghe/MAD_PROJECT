package com.example.testeasyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddType extends AppCompatActivity {

    private EditText typeHead,totalAmount;
    private Button addType;
    private DBHandler dbHandler;
    private Context context;
    public static double total;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_type);
        
        typeHead = findViewById(R.id.enterHead);
        totalAmount = findViewById(R.id.enterTotalAmount);
        addType = findViewById(R.id.addType);
        context = this;

        dbHandler = new DBHandler(context);
        addType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!validatePlan() | !validateAmount()){
                    return;
                }

                String planType = typeHead.getText().toString();
                total = Double.parseDouble(totalAmount.getText().toString());
                
                Type type = new Type(planType,total);
                dbHandler.addType(type);

                Toast.makeText(getApplicationContext(),"New plan has been added", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(context,MainActivity.class));
                finish();

            }
        });
    }

    private Boolean validatePlan(){
        String val = typeHead.getText().toString();
        if(val.isEmpty()){
            typeHead.setError("Field cannot be empty");
            return false;
        }else{
            typeHead.setError(null);
            return true;
        }
    }

    private Boolean validateAmount(){
        String val = totalAmount.getText().toString();
        if(val.isEmpty()){
            totalAmount.setError("Field cannot be empty");
            return false;
        }else{
            totalAmount.setError(null);
            return true;
        }
    }
}