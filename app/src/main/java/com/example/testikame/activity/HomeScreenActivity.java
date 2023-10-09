package com.example.testikame.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.widget.SearchView;

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

public class HomeScreenActivity extends AppCompatActivity implements FragmentBottomSheetMoreAddContact.OnDataChangeListener {
    private ImageView imgAddContact;
    private DatabaseHandler databaseHandler;
    private RecyclerView recyclerView, rcvSearch;
    private SearchView searchViewMain;
    private LinearLayout lnHome;
    private ContactAdapter contactAdapter, contactAdapterSearch;
    private List<ContactInfo> contactInfoList, contactInfoListSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen_activity);

        initUI();

        setClickImgAddContact();
        setClickSearchView();
    }

    private void setClickSearchView() {
        searchViewMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()>0){
                    performSearch(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(newText.length()==0){
                    rcvSearch.setVisibility(View.GONE);
                    lnHome.setVisibility(View.VISIBLE);
                }else {
                    performSearch(newText);
                }
                Log.d("searchabc", "onQueryTextChange: " + newText);
                return true;
            }
        });
    }

    private void performSearch(String text) {
        contactInfoListSearch = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcvSearch.setLayoutManager(linearLayoutManager);

        contactAdapterSearch = new ContactAdapter(this, contactInfoListSearch, new ContactAdapter.IClickListener() {
            @Override
            public void onClickItem(int position) {
                ContactInfo contactInfo = contactAdapterSearch.GetContactInfoByPosition(position);
                Bundle bundle = new Bundle();
                bundle.putInt("idcontact", contactInfo.getIdPerson());
                bundle.putString("fullname", contactInfo.getFullnamePerson());
                bundle.putString("surname", contactInfo.getSurnamePerson());
                bundle.putString("name", contactInfo.getNamePerson());
                bundle.putString("linkimg", contactInfo.getLinkImg());
                bundle.putInt("backgroundcolor", contactInfo.getBackgroundColor());

                Intent intent = new Intent(HomeScreenActivity.this, ContactDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rcvSearch.setAdapter(contactAdapterSearch);
        for(int i=0; i<contactInfoList.size(); i++){
            if(contactInfoList.get(i).getFullnamePerson().toUpperCase().contains(text.toUpperCase())){
                contactInfoListSearch.add(contactInfoList.get(i));
            }
        }
        rcvSearch.setVisibility(View.VISIBLE);
        lnHome.setVisibility(View.GONE);
    }

    private void setClickImgAddContact() {
        imgAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentBottomSheetMoreAddContact bottomSheetFragment = new FragmentBottomSheetMoreAddContact();
                bottomSheetFragment.setOnDataChangeListener(HomeScreenActivity.this);
                bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
            }
        });
    }

    private void initUI() {
        imgAddContact = findViewById(R.id.img_addcontact);
        recyclerView = findViewById(R.id.rcv_listcontact);
        rcvSearch = findViewById(R.id.rcvseachviewmain);
        searchViewMain = findViewById(R.id.searchview_main);
        lnHome = findViewById(R.id.ln_homescreen);
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
                bundle.putInt("backgroundcolor", contactInfo.getBackgroundColor());

                Intent intent = new Intent(HomeScreenActivity.this, ContactDetailsActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        rcvSearch.setNestedScrollingEnabled(false);
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

    @Override
    public void onDataChanged() {
        getListContact();
        contactAdapter.notifyDataSetChanged();
    }
}
