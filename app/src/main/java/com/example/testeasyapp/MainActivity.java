package com.example.testeasyapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.makeText;

public class MainActivity extends AppCompatActivity {

    private Button add,typeBtn,deleteType;
    private ListView listView;
    private TextView count,amount;
    Context context;
    private DBHandler dbHandler;
    private List<Currency> currencies;
    private List<Type> types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        dbHandler = new DBHandler(context);
        add = findViewById(R.id.addToDo);
        typeBtn = findViewById(R.id.addType);
        deleteType = findViewById(R.id.deleteType);
        listView = findViewById(R.id.todoList);
        count = findViewById(R.id.countView);
        currencies = new ArrayList<>();
        types = new ArrayList<>();
        amount = findViewById(R.id.allocatedAmount);

        typeBtn.setEnabled(false);


        add.setEnabled(false);
        //add.setBackgroundResource(R.drawable.rounded_button_disable);


        currencies = dbHandler.getAllToDos();

        ToDoAdapter adapter = new ToDoAdapter(context,R.layout.single_todo, currencies);

        listView.setAdapter(adapter);

        deleteType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.deleteType();
                dbHandler.deleteCurrency();
                Toast toast = makeText(getApplicationContext(),"Currency plan has been deleted", Toast.LENGTH_SHORT);
                toast.show();
                startActivity(new Intent(context,MainActivity.class));
                finish();
            }
        });
        double allocatedAmount = dbHandler.getAllocatedAmount();
        double expenses = dbHandler.getExpenses();

        if(getRemain(allocatedAmount,expenses) < 0){
            amount.setText("allocated amount exceeded");
        }else{
            amount.setText("Remaining: "+Double.toString(getRemain(allocatedAmount,expenses))+"/"+Double.toString( dbHandler.getAllocatedAmount()));
        }

        int countTodo = dbHandler.countToDo();
        count.setText("You have "+ countTodo + " tasks");

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddCurrency.class));
                finish();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                final Currency todo = currencies.get(i);
                final Type type = types.get(0);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(todo.getTitle());
                builder.setMessage(todo.getDescription() + " \nAmount is: "+Double.toString(todo.getAmount())
                +"\nPlan name: "+type.getType()+"\nAllocated Total Amount: "+type.getAmount()
                +"\nTargeted Date: "+ todo.getTargeted_date());
                builder.setPositiveButton("Finished", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        todo.setFinished(System.currentTimeMillis());
                        dbHandler.updateSingleToDo(todo);
                        startActivity(new Intent(context,MainActivity.class));
                        finish();
                    }
                });

                builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dbHandler.deleteToDo(todo.getId());
                        Toast toast = makeText(getApplicationContext(),"Task is deleted...", Toast.LENGTH_SHORT);
                        toast.show();
                        startActivity(new Intent(context,MainActivity.class));
                        finish();
                    }
                });
                builder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(context, EditCurrency.class);
                        intent.putExtra("id",String.valueOf(todo.getId()));
                        startActivity(intent);

                    }
                });
                builder.show();

            }

        });

        typeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddType.class);
                startActivity(intent);
            }
        });

        types = dbHandler.getPlans();
        if(types.isEmpty()){
            typeBtn.setEnabled(true);
             add.setBackgroundResource(R.drawable.rounded_button_disable);

        }else {
            add.setEnabled(true);
            typeBtn.setBackgroundResource(R.drawable.rounded_button_disable);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        context = this;
        currencies = new ArrayList<>();
        currencies = dbHandler.getAllToDos();
        final ToDoAdapter adapter = new ToDoAdapter(context,R.layout.single_todo, currencies);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_menu,menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public double getRemain(double allocatedAmount,double expenses){
        return  allocatedAmount -  expenses ;
    }
}