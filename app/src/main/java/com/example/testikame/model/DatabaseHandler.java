package com.example.testikame.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DatabaseHandler extends SQLiteOpenHelper implements DatabaseHandlerI{

    private static final String TABLE_PERSON = "person";
    private static final String KEY_IDPERSON = "idperson";
    private static final String COLUMN_FULLNAMEPERSON = "fullnamePerson";
    private static final String COLUMN_SURNAMEPERSON = "surnamePerson";
    private static final String COLUMN_NAMEPERSON = "namePerson";
    private static final String COLUMN_LINKIMGPERSON = "linkImg";


    private static final String TABLE_EMAIL = "email";
    private static final String KEY_IDEMAIL = "idemail";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_IDPERSON_EMAIL = "idperson";


    private static final String TABLE_PHONENUMBER = "phonenumber";
    private static final String KEY_IDPHONE = "idphone";
    private static final String COLUMN_IDPERSON_PHONE = "idperson";
    private static final String COLUMN_PHONENUMBER = "phonenumber";

    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("database", "Create table folder");
        String createPersonTable = String.format("CREATE TABLE " + TABLE_PERSON + " ("
                + KEY_IDPERSON + " INTEGER PRIMARY KEY, "
                + COLUMN_FULLNAMEPERSON + " TEXT,"
                + COLUMN_SURNAMEPERSON  + " TEXT,"
                + COLUMN_NAMEPERSON + " TEXT,"
                + COLUMN_LINKIMGPERSON+ " TEXT);");

        Log.i("database", "Create table email");
        String createEmailTable = String.format("CREATE TABLE " + TABLE_EMAIL + " ("
                + KEY_IDEMAIL + " INTEGER PRIMARY KEY, "
                + COLUMN_IDPERSON_EMAIL + " TEXT,"
                + COLUMN_EMAIL  + " TEXT);");

        Log.i("database", "Create table phone");
        String createPhoneTable = String.format("CREATE TABLE " + TABLE_PHONENUMBER + " ("
                + KEY_IDPHONE + " INTEGER PRIMARY KEY, "
                + COLUMN_IDPERSON_PHONE + " TEXT,"
                + COLUMN_PHONENUMBER+ " TEXT);");

        db.execSQL(createPersonTable);
        db.execSQL(createEmailTable);
        db.execSQL(createPhoneTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Xoá bảng cũ
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSON);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHONENUMBER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EMAIL);
        //Tiến hành tạo bảng mới
        onCreate(db);
    }

    @Override
    public void addPerson(Person person) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_SURNAMEPERSON, person.getSurnamePerson());
            values.put(COLUMN_NAMEPERSON, person.getNamePerson());
            values.put(COLUMN_FULLNAMEPERSON, person.getFullnamePerson());
            values.put(COLUMN_LINKIMGPERSON, person.getLinkImg());
            db.insert(TABLE_PERSON, null, values);
            db.close();

            Log.i("database", "thêm person thành công");
        }catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
    }

    @Override
    public void addEmail(Email email) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDPERSON_EMAIL, email.getIdPerson());
            values.put(COLUMN_EMAIL, email.getEmail());
            db.insert(TABLE_EMAIL, null, values);
            db.close();

            Log.i("database", "Thêm email thành công");
        }catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
    }

    @Override
    public void addPhone(PhoneNumber phoneNumber) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDPERSON_PHONE, phoneNumber.getIdPerson());
            values.put(COLUMN_PHONENUMBER, phoneNumber.getPhoneNumber());
            db.insert(TABLE_PHONENUMBER, null, values);
            db.close();

            Log.i("database", "Thêm phone thành công");
        }catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
    }
}
