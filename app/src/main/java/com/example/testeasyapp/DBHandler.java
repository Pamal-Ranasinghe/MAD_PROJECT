package com.example.testeasyapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {

    private static final int VERSION = 5 ; //If u update any kind of table column or what ever thing, it considers as it is a change of version of database
    private static final String DB_NAME = "todo";
    private static final String TABLE_NAME = "todo";
    private static final String TABLE_NAME_TYPE = "type";

    //Column names common
    private static final String ID = "id";

    //Column names todo
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String AMOUNT = "amount";
    private static final String STARTED = "started";
    private static final String FINISHED = "finished";
    private static final String TARGETED_DATE = "targeted_date";

    //column names type
    private static final String TYPE = "type";
    private static final String ALLOCATED_AMOUNT = "a_amount";


    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION); //Runs at first and create the database
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        String TABLE_CREATE_QUERY =  "CREATE TABLE " +TABLE_NAME+" "+
                "("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +TITLE+" TEXT,"
                +DESCRIPTION+" TEXT,"
                +AMOUNT+" DOUBLE,"
                +STARTED+" TEXT,"
                +FINISHED+" TEXT,"
                +TARGETED_DATE+" TEXT"
                +");";

        String TYPE_TABLE_CREATE_QUERY =  "CREATE TABLE " +TABLE_NAME_TYPE+" "+
                "("
                +ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"
                +TYPE+" TEXT,"
                +ALLOCATED_AMOUNT+" DOUBLE"
                +");";

        db.execSQL(TABLE_CREATE_QUERY);
        db.execSQL(TYPE_TABLE_CREATE_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            //method runs if VERSION number has changed

            String DROP_TABLE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME;
            String DROP_TABLE_TYPE_QUERY = "DROP TABLE IF EXISTS " + TABLE_NAME_TYPE;
            db.execSQL(DROP_TABLE_QUERY);
            db.execSQL(DROP_TABLE_TYPE_QUERY);
            onCreate(db);
    }

    //Form validations


    public void addToDo(Currency todo){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TITLE, todo.getTitle());
        contentValues.put(DESCRIPTION, todo.getDescription());
        contentValues.put(AMOUNT, todo.getAmount());
        contentValues.put(STARTED, todo.getStarted());
        contentValues.put(FINISHED, todo.getFinished());
        contentValues.put(TARGETED_DATE, todo.getTargeted_date());

        //save to table
        sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        sqLiteDatabase.close();
     }

    public void addType(Type type){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(TYPE, type.getType());
        contentValues.put(ALLOCATED_AMOUNT, type.getAmount());

        sqLiteDatabase.insert(TABLE_NAME_TYPE,null,contentValues);
        sqLiteDatabase.close();
    }

     public int countToDo(){
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM " +TABLE_NAME;

         Cursor cursor = db.rawQuery(query,null);
         return cursor.getCount();
     }

    public double getAllocatedAmount(){
        double total = 0.0;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM(" +ALLOCATED_AMOUNT + ") as Total FROM " + TABLE_NAME_TYPE,null);

        if(cursor.moveToFirst()){
            return total = cursor.getDouble(cursor.getColumnIndex("Total"));
        }else{
            return 0.0;
        }

    }

    public double getExpenses(){
        double total = 0.0;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT SUM(" +AMOUNT + ") as Total FROM " + TABLE_NAME,null);

        if(cursor.moveToFirst()){
            return total = cursor.getDouble(cursor.getColumnIndex("Total"));
        }else{
            return 0.0;
        }

    }


     public List<Currency> getAllToDos(){
        List<Currency> currencies = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Currency currency = new Currency();

                currency.setId(cursor.getInt(0));
                currency.setTitle(cursor.getString(1));
                currency.setDescription(cursor.getString(2));
                currency.setAmount(cursor.getDouble(3));
                currency.setStarted(cursor.getLong(4));
                currency.setFinished(cursor.getLong(5));
                currency.setTargeted_date(cursor.getString(6));

                currencies.add(currency);
            }while(cursor.moveToNext());
        }

        return currencies;
     }

    public List<Type> getPlans(){
        List<Type> types = new ArrayList();
        SQLiteDatabase db = getReadableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME_TYPE;

        Cursor cursor = db.rawQuery(query,null);

        if(cursor.moveToFirst()){
            do{
                Type type = new Type();

                type.setId(cursor.getInt(0));
                type.setType(cursor.getString(1));
                type.setAmount(cursor.getDouble(2));

                types.add(type);
            }while(cursor.moveToNext());
        }

        return types;
    }

     public void deleteToDo(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,ID + " =?",new String[]{String.valueOf(id)});
        db.close();
     }

    public void deleteType(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME_TYPE,null,null);
        db.close();
    }

    public void deleteCurrency(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME,null,null);
        db.close();
    }

     public Currency getSingleToDo(int id){
        SQLiteDatabase db = getWritableDatabase();

        Cursor cursor = db.query(TABLE_NAME,new String[]{ID,TITLE,DESCRIPTION,AMOUNT,STARTED,FINISHED,TARGETED_DATE}, ID + " = ?", new String[]{String.valueOf(id)} ,null,null,null,null);

        Currency currency;
        if(cursor != null){
                cursor.moveToFirst();
            currency = new Currency(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getDouble(3),
                    cursor.getLong(4),
                    cursor.getLong(5),
                    cursor.getString(6)
            );
            return currency;
        }
        return null;
     }

     public int updateSingleToDo(Currency todo){
        SQLiteDatabase db = getWritableDatabase();

         SQLiteDatabase sqLiteDatabase = getWritableDatabase();

         ContentValues contentValues = new ContentValues();
         contentValues.put(TITLE, todo.getTitle());
         contentValues.put(DESCRIPTION, todo.getDescription());
         contentValues.put(AMOUNT, todo.getAmount());
         contentValues.put(STARTED, todo.getStarted());
         contentValues.put(FINISHED, todo.getFinished());
         contentValues.put(TARGETED_DATE, todo.getTargeted_date());

         int status = db.update(TABLE_NAME,contentValues,ID + " =?",
                 new String[]{String.valueOf(todo.getId())});
         db.close();
         return status;

     }

     public int unMarkCheckBox(Currency todo){
         SQLiteDatabase db = getWritableDatabase();
         //ToDo todo = new ToDo();

         SQLiteDatabase sqLiteDatabase = getWritableDatabase();
         ContentValues contentValues = new ContentValues();
         contentValues.put(FINISHED, 0);

         int status = db.update(TABLE_NAME,contentValues,ID + " =?",
                 new String[]{String.valueOf(todo.getId())});
         db.close();
         return status;
     }



}
