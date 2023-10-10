package com.example.testikame.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DatabaseHandler extends SQLiteOpenHelper implements DatabaseHandlerI {

    private static final String TABLE_PERSON = "person";
    private static final String KEY_IDPERSON = "idperson";
    private static final String COLUMN_FULLNAMEPERSON = "fullnamePerson";
    private static final String COLUMN_SURNAMEPERSON = "surnamePerson";
    private static final String COLUMN_NAMEPERSON = "namePerson";
    private static final String COLUMN_LINKIMGPERSON = "linkImg";
    private static final String COLUMN_BACKGROUNDCOLORPERSON = "backgroundColorPerson";


    private static final String TABLE_EMAIL = "email";
    private static final String KEY_IDEMAIL = "idemail";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_EMAIL_TYPE = "emailtype";
    private static final String COLUMN_IDPERSON_EMAIL = "idperson";


    private static final String TABLE_PHONENUMBER = "phonenumber";
    private static final String KEY_IDPHONE = "idphone";
    private static final String COLUMN_IDPERSON_PHONE = "idperson";
    private static final String COLUMN_PHONE_TYPE = "phonetype";
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
                + COLUMN_SURNAMEPERSON + " TEXT,"
                + COLUMN_NAMEPERSON + " TEXT,"
                + COLUMN_LINKIMGPERSON + " TEXT,"
                + COLUMN_BACKGROUNDCOLORPERSON + " INTERGER);");

        Log.i("database", "Create table email");
        String createEmailTable = String.format("CREATE TABLE " + TABLE_EMAIL + " ("
                + KEY_IDEMAIL + " INTEGER PRIMARY KEY, "
                + COLUMN_IDPERSON_EMAIL + " TEXT,"
                + COLUMN_EMAIL_TYPE + " TEXT,"
                + COLUMN_EMAIL + " TEXT);");

        Log.i("database", "Create table phone");
        String createPhoneTable = String.format("CREATE TABLE " + TABLE_PHONENUMBER + " ("
                + KEY_IDPHONE + " INTEGER PRIMARY KEY, "
                + COLUMN_IDPERSON_PHONE + " TEXT,"
                + COLUMN_PHONE_TYPE + " TEXT,"
                + COLUMN_PHONENUMBER + " TEXT);");

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
    public long addContact(ContactInfo contactInfo) {
        long idContact = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_SURNAMEPERSON, contactInfo.getSurnamePerson().toString().trim());
            values.put(COLUMN_NAMEPERSON, contactInfo.getNamePerson().toString().trim());
            values.put(COLUMN_FULLNAMEPERSON, contactInfo.getFullnamePerson().toString().trim());
            values.put(COLUMN_LINKIMGPERSON, contactInfo.getLinkImg());

            Random random = new Random();
            int red = random.nextInt(256);
            int green = random.nextInt(256);
            int blue = random.nextInt(256);
            int randomColor = Color.rgb(red, green, blue);
            values.put(COLUMN_BACKGROUNDCOLORPERSON, randomColor);


            idContact = db.insert(TABLE_PERSON, null, values);
            db.close();

            Log.i("database", "thêm person thành công");
        } catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
        return idContact;
    }

    @Override
    public void addEmail(Email email) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDPERSON_EMAIL, email.getIdPerson());
            values.put(COLUMN_EMAIL, email.getEmail().toString().trim());
            values.put(COLUMN_EMAIL_TYPE, "Cá nhân");
            db.insert(TABLE_EMAIL, null, values);
            db.close();

            Log.i("database", "Thêm email thành công");
        } catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
    }

    @Override
    public void addPhone(PhoneNumber phoneNumber) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(COLUMN_IDPERSON_PHONE, phoneNumber.getIdPerson());
            values.put(COLUMN_PHONE_TYPE, "Di động");
            values.put(COLUMN_PHONENUMBER, phoneNumber.getPhoneNumber().toString().trim());
            db.insert(TABLE_PHONENUMBER, null, values);
            db.close();
            Log.i("database", "Thêm phone thành công");
        } catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
    }

    @Override
    public List<ContactInfo> getAllContact() {
        List<ContactInfo> contactInfos = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_PERSON;

            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false) {
                int id = cursor.getInt(0);
                String fullname = cursor.getString(1);
                String surname = cursor.getString(2);
                String name = cursor.getString(3);
                String linkimg = cursor.getString(4);
                int backgroundColor = cursor.getInt(5);

                ContactInfo contactInfo = new ContactInfo(id, fullname, surname, name, linkimg, backgroundColor);
                contactInfos.add(contactInfo);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
        return contactInfos;
    }

    @Override
    public void update(int id, ContactInfo contactInfo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(COLUMN_FULLNAMEPERSON, contactInfo.getFullnamePerson().toString().trim());
            values.put(COLUMN_SURNAMEPERSON, contactInfo.getSurnamePerson().toString().trim());
            values.put(COLUMN_NAMEPERSON, contactInfo.getNamePerson().toString().trim());
            values.put(COLUMN_LINKIMGPERSON, contactInfo.getLinkImg());

            String whereClause = KEY_IDPERSON + " = ?";
            String[] whereArgs = {String.valueOf(id)};

            db.update(TABLE_PERSON, values, whereClause, whereArgs);
            Log.i("database", "update  contact thành công" );
            db.close();
        }catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
    }

    public ContactInfo getContactInfo(int id) {
        ContactInfo contactInfo = null;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_PERSON + " WHERE " + KEY_IDPERSON + " = " + id;

            Cursor cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                String fullname = cursor.getString(1);
                String surname = cursor.getString(2);
                String name = cursor.getString(3);
                String linkimg = cursor.getString(4);
                int backgroundColor = cursor.getInt(5);

                contactInfo = new ContactInfo(id, fullname, surname, name, linkimg, backgroundColor);
                cursor.moveToNext();
            }
        } catch (SQLiteException e) {
            Log.d("database", e.toString());
        }

        return contactInfo;
    }

    @Override
    public List<PhoneNumber> getPhoneNumberContact(int idPerson) {
        List<PhoneNumber> phoneNumbers = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_PHONENUMBER + " WHERE " + COLUMN_IDPERSON_PHONE + " = " + idPerson;

            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false) {
                int id = cursor.getInt(0);
                String phoneType = cursor.getString(2);
                String phone= cursor.getString(3);

                PhoneNumber phoneNumber = new PhoneNumber(id, idPerson, phoneType, phone);
                phoneNumbers.add(phoneNumber);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
        return phoneNumbers;
    }

    @Override
    public List<Email> getEmailContact(int idPerson) {
        List<Email> emails = new ArrayList<>();

        try {
            SQLiteDatabase db = this.getReadableDatabase();
            String query = "SELECT * FROM " + TABLE_EMAIL + " WHERE " + COLUMN_IDPERSON_PHONE + " = " + idPerson;

            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            while(cursor.isAfterLast() == false) {
                int id = cursor.getInt(0);
                String emailType = cursor.getString(2);
                String emailInfo= cursor.getString(3);

                Email email = new Email(id, idPerson,emailType, emailInfo);
                emails.add(email);
                cursor.moveToNext();
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
        return emails;
    }

    @Override
    public void deleteEmail(int idContact) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String whereClause = COLUMN_IDPERSON_EMAIL + " = ?";
            String[] whereArgs = {String.valueOf(idContact)};
            db.delete(TABLE_EMAIL, whereClause, whereArgs);

            db.close();

            Log.d("database", "deleteEmail: " + " Xóa email thành công");
        }catch (SQLiteException e) {
            Log.d("database", e.toString());
        }

    }

    @Override
    public void deletePhoneNumber(int idContact) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String whereClause = COLUMN_IDPERSON_PHONE + " = ?";
            String[] whereArgs = {String.valueOf(idContact)};
            db.delete(TABLE_PHONENUMBER, whereClause, whereArgs);

            db.close();

            Log.d("database", "deletePhone: " + " Xóa Phone thành công");
        }catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
    }

    @Override
    public void deleteContact(int idContact) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            String whereClause = KEY_IDPERSON + " = ?";
            String[] whereArgs = {String.valueOf(idContact)};
            db.delete(TABLE_PERSON, whereClause, whereArgs);

            String whereClause1 = COLUMN_IDPERSON_EMAIL + " = ?";
            String[] whereArgs1 = {String.valueOf(idContact)};
            db.delete(TABLE_EMAIL, whereClause1, whereArgs1);

            String whereClause2 = COLUMN_IDPERSON_PHONE + " = ?";
            String[] whereArgs2 = {String.valueOf(idContact)};
            db.delete(TABLE_PHONENUMBER, whereClause2, whereArgs2);

            db.close();

            Log.d("database", "deletePhone: " + " Xóa Contact thành công");
        }catch (SQLiteException e) {
            Log.d("database", e.toString());
        }
    }
}
