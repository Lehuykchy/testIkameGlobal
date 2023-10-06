package com.example.testikame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testikame.R;
import com.example.testikame.adapter.ContactAdapter;
import com.example.testikame.fragment.FragmentBottomSheetMoreAddContact;
import com.example.testikame.model.ContactInfo;
import com.example.testikame.model.DatabaseHandler;

import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity {
    private ImageView imgAddContact;
    private DatabaseHandler databaseHandler;
    private RecyclerView recyclerView;
    private ContactAdapter contactAdapter;
    private List<ContactInfo> contactInfoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen_activity);

        initUI();

        setClickImgAddContact();
    }

    private void setClickImgAddContact() {
        imgAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentBottomSheetMoreAddContact bottomSheetFragment = new FragmentBottomSheetMoreAddContact();
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }

    private void initUI() {
        imgAddContact = findViewById(R.id.img_addcontact);
        recyclerView = findViewById(R.id.rcv_listcontact);
        databaseHandler = new DatabaseHandler(this, "dbcontact", null, 1);
        contactInfoList = new ArrayList<>();

        getListContact();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        contactAdapter = new ContactAdapter(this, contactInfoList, new ContactAdapter.IClickListener() {
            @Override
            public void onClickItem(int position) {
                ContactInfo contactInfo = contactAdapter.GetContactInfoByPosition(position);
                Bundle bundle = new Bundle();
                bundle.putInt("idcontact", contactInfo.getIdPerson());
                bundle.putString("fullname", contactInfo.getFullnamePerson());
                bundle.putString("surname", contactInfo.getSurnamePerson());
                bundle.putString("name", contactInfo.getNamePerson());
                bundle.putString("linkimg", contactInfo.getLinkImg());

                Intent intent = new Intent(HomeScreenActivity.this, ContactDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        recyclerView.setAdapter(contactAdapter);


    }

    private void getListContact() {
        contactInfoList.clear();
        contactInfoList.addAll(databaseHandler.getAllContact());
        for (int i = 0; i < contactInfoList.size(); i++) {
            Log.d("database", String.valueOf(contactInfoList.get(i).getIdPerson()
                    + " " + contactInfoList.get(i).getFullnamePerson()
                    + " " + contactInfoList.get(i).getSurnamePerson()
                    + " " + contactInfoList.get(i).getNamePerson()
                    + " " + contactInfoList.get(i).getLinkImg()));
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getListContact();
        contactAdapter.notifyDataSetChanged();
    }
}
