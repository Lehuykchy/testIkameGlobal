package com.example.testikame.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testikame.R;
import com.example.testikame.adapter.PhoneEmailAdapter;
import com.example.testikame.model.ContactInfo;
import com.example.testikame.model.DatabaseHandler;
import com.example.testikame.model.Email;
import com.example.testikame.model.PhoneNumber;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ContactDetailsActivity extends AppCompatActivity {
    private TextView tvFullname, tvEdit, tvContactImg;
    private CardView cardView;
    private ImageView imgContact;
    private RecyclerView rcvPhone, rcvEmail;
    private List<PhoneNumber> phoneNumberList;
    private List<Email> emailList;
    private PhoneEmailAdapter phoneAdapter, emailAdapter;
    private LinearLayout lnDestroy;
    private ContactInfo contactInfo;
    private DatabaseHandler databaseHandler;
    private  int idContact;
    private Animation animation;

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
                v.startAnimation(animation);
                finish();
            }
        });
    }

    private void setClickTvEdit() {
        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                Bundle bundle = new Bundle();
                bundle.putInt("idcontact", contactInfo.getIdPerson());
                bundle.putString("fullname", contactInfo.getFullnamePerson());
                bundle.putString("surname", contactInfo.getSurnamePerson());
                bundle.putString("name", contactInfo.getNamePerson());
                bundle.putString("linkimg", contactInfo.getLinkImg());
                bundle.putInt("backgroundcolor", contactInfo.getBackgroundColor());

                Intent intent = new Intent(ContactDetailsActivity.this, EditContactActivity.class);
                intent.putExtras(bundle);
                Log.d("database", "onClick: " + String.valueOf(contactInfo.getIdPerson()));
                startActivityForResult(intent, 1);
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
        cardView = findViewById(R.id.cardview_detailscontact);
        tvContactImg = findViewById(R.id.tv_detailscontactimg);
        databaseHandler = new DatabaseHandler(this, "dbcontact", null, 1);

        animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(200);

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            idContact = receivedBundle.getInt("idcontact", 0);
            String fullname = receivedBundle.getString("fullname", null);
            String surname = receivedBundle.getString("surname", null);
            String name = receivedBundle.getString("name", null);
            String linkimg = receivedBundle.getString("linkimg", null);
            int backgroundColor = receivedBundle.getInt("backgroundcolor", 0);
            contactInfo = new ContactInfo(idContact, fullname, surname, name, linkimg, backgroundColor);
        }


        phoneNumberList = new ArrayList<>();
        getPhoneNumberList();
        LinearLayoutManager lnPhoneManager = new LinearLayoutManager(this);
        rcvPhone.setLayoutManager(lnPhoneManager);
        phoneAdapter = new PhoneEmailAdapter(this, phoneNumberList);
        rcvPhone.setAdapter(phoneAdapter);
        rcvPhone.setNestedScrollingEnabled(false);

        emailList = new ArrayList<>();
        getEmailList();
        LinearLayoutManager lnEmailManager = new LinearLayoutManager(this);
        rcvEmail.setLayoutManager(lnEmailManager);
        emailAdapter = new PhoneEmailAdapter(emailList);
        rcvEmail.setAdapter(emailAdapter);
        rcvEmail.setNestedScrollingEnabled(false);






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
        contactInfo = databaseHandler.getContactInfo(contactInfo.getIdPerson());
        getPhoneNumberList();
        getEmailList();
        phoneAdapter.notifyDataSetChanged();
        emailAdapter.notifyDataSetChanged();
        tvFullname.setText(contactInfo.getFullnamePerson());
        cardView.setCardBackgroundColor(contactInfo.getBackgroundColor());
        if (contactInfo != null) {
            if(contactInfo.getLinkImg() != null) {
                tvContactImg.setVisibility(View.GONE);
                imgContact.setVisibility(View.VISIBLE);
                String fileName = contactInfo.getLinkImg();
                File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

                try {
                    String filePath = storageDir.getAbsolutePath() + File.separator + fileName;
                    Log.d("database", "onResume: " + filePath);

                    File imageFile = new File(filePath);

                    if (imageFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                        imgContact.setImageBitmap(bitmap);
                    } else {
                        if (contactInfo.getFullnamePerson().length() > 0){
                            tvContactImg.setText(String.valueOf(Character.toUpperCase(contactInfo.getFullnamePerson().charAt(0))));
                            tvContactImg.setVisibility(View.VISIBLE);
                            imgContact.setVisibility(View.GONE);
                        }else {
                            tvContactImg.setVisibility(View.GONE);
                            imgContact.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("bugimg", "Lỗi khi hiển thị ảnh");
                }
            }else {
                if (contactInfo.getFullnamePerson().trim().length() > 0){
                    tvContactImg.setText(String.valueOf(Character.toUpperCase(contactInfo.getFullnamePerson().trim().charAt(0))));
                    tvContactImg.setVisibility(View.VISIBLE);
                    imgContact.setVisibility(View.GONE);
                }else {
                    tvContactImg.setVisibility(View.GONE);
                    imgContact.setVisibility(View.VISIBLE);
                }

            }
        }

    }
}
