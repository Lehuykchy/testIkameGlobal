package com.example.testikame.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.testikame.R;
import com.example.testikame.adapter.EmailAdapter;
import com.example.testikame.adapter.PhoneEmailAdapter;
import com.example.testikame.adapter.PhoneNumberAdapter;
import com.example.testikame.model.ContactInfo;
import com.example.testikame.model.DatabaseHandler;
import com.example.testikame.model.Email;
import com.example.testikame.model.PhoneNumber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContactDetailsActivity extends AppCompatActivity {
    private TextView tvFullname, tvEdit;
    private ImageView imgContact;
    private RecyclerView rcvPhone, rcvEmail;
    private List<PhoneNumber> phoneNumberList;
    private List<Email> emailList;
    private PhoneEmailAdapter phoneAdapter, emailAdapter;
    private LinearLayout lnDestroy;
    private ContactInfo contactInfo;
    private DatabaseHandler databaseHandler;
    private  int idContact;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contactdetails_activity);
        initUI();

        setClickTvEdit();
        setClickLnDestroy();

    }

    private void setClickLnDestroy() {
        lnDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setClickTvEdit() {
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("idcontact", contactInfo.getIdPerson());
                bundle.putString("fullname", contactInfo.getFullnamePerson());
                bundle.putString("surname", contactInfo.getSurnamePerson());
                bundle.putString("name", contactInfo.getNamePerson());
                bundle.putString("linkimg", contactInfo.getLinkImg());

                Intent intent = new Intent(ContactDetailsActivity.this, EditContactActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void initUI() {
        tvFullname = findViewById(R.id.tv_detailscontactfullname);
        tvEdit = findViewById(R.id.tv_detailscontactedit);
        imgContact = findViewById(R.id.img_detailscontact);
        rcvPhone = findViewById(R.id.rcv_detailscontactphone);
        rcvEmail = findViewById(R.id.rcv_detailscontactemail);
        lnDestroy = findViewById(R.id.ln_detailscontactdestroy);
        databaseHandler = new DatabaseHandler(this, "dbcontact", null, 1);

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            idContact = receivedBundle.getInt("idcontact", 0);
            String fullname = receivedBundle.getString("fullname", null);
            String surname = receivedBundle.getString("surname", null);
            String name = receivedBundle.getString("name", null);
            String linkimg = receivedBundle.getString("linkimg", null);

            tvFullname.setText(fullname);
        }


        phoneNumberList = new ArrayList<>();
        getPhoneNumberList();
        LinearLayoutManager lnPhoneManager = new LinearLayoutManager(this);
        rcvPhone.setLayoutManager(lnPhoneManager);
        phoneAdapter = new PhoneEmailAdapter(this, phoneNumberList);
        rcvPhone.setAdapter(phoneAdapter);

        emailList = new ArrayList<>();
        getEmailList();
        LinearLayoutManager lnEmailManager = new LinearLayoutManager(this);
        rcvEmail.setLayoutManager(lnEmailManager);
        emailAdapter = new PhoneEmailAdapter(emailList);
        rcvEmail.setAdapter(emailAdapter);





    }

    private void getEmailList() {
        emailList.clear();
        emailList.addAll(databaseHandler.getEmailContact(idContact));
        Log.d("database", "getPhoneNumberList: " + String.valueOf(phoneNumberList.size()));
    }

    private void getPhoneNumberList() {
        phoneNumberList.clear();
        phoneNumberList.addAll(databaseHandler.getPhoneNumberContact(idContact));
        Log.d("database", "getPhoneNumberList: " + String.valueOf(phoneNumberList.size()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        contactInfo = databaseHandler.getContactInfo(idContact);
        getPhoneNumberList();
        getEmailList();
        String fileName = contactInfo.getLinkImg();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            String filePath = storageDir.getAbsolutePath() + File.separator + fileName;
            Log.d("database", "onResume: " + filePath);

            File imageFile = new File(filePath);

            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                imgContact.setImageBitmap(bitmap);
            }else {
                Toast.makeText(this, "Lỗi khi hiển thị ảnh ko tồn tại", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi hiển thị ảnh", Toast.LENGTH_SHORT).show();
        }

    }
}
