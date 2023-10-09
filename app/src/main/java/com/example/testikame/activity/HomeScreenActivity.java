package com.example.testikame.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeScreenActivity extends AppCompatActivity implements FragmentBottomSheetMoreAddContact.OnDataChangeListener {
    private static final int REQUEST_CODE_IMAGE_PICK = 1;
    private ImageView imgAddContact;
    private DatabaseHandler databaseHandler;
    private RecyclerView recyclerView, rcvSearch;
    private SearchView searchViewMain;
    private ImageView imgHomeScreen;
    private LinearLayout lnHome, lnSearch;
    private ContactAdapter contactAdapter, contactAdapterSearch;
    private List<ContactInfo> contactInfoList, contactInfoListSearch;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen_activity);

        initUI();

        setClickImgAddContact();
        setClickLnSearch();
        setClickSearchView();
        setClickImgHome();

    }

    private void setClickImgHome() {
        String fileName = "image_homescreen.jpg";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            String filePath = storageDir.getAbsolutePath() + File.separator + fileName;
            Log.d("database", "onResume: " + filePath);

            File imageFile = new File(filePath);

            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                imgHomeScreen.setImageBitmap(bitmap);
            }else {
                Log.d("bugimg", "Lỗi khi hiển thị ảnh ko tồn tại");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("bugimg", "Lỗi khi hiển thị ảnh ");
        }
        imgHomeScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            String fileName = "image_homescreen.jpg";
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(storageDir, fileName);
            if (imageFile.exists()) {
                imageFile.delete();
            }

            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                OutputStream outputStream = new FileOutputStream(imageFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(imageFile));
                imgHomeScreen.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setClickLnSearch() {
        lnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchViewMain.setIconified(false);
            }
        });
    }

    private void setClickSearchView() {
        searchViewMain.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length()>0){
                    performSearch(query);
                }
                if(contactInfoListSearch.size()==0){
                    Toast.makeText(HomeScreenActivity.this, "Không tìm thấy liên hệ cần tìm", Toast.LENGTH_SHORT).show();
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
        lnSearch = findViewById(R.id.ln_search);
        imgHomeScreen = findViewById(R.id.img_homescreen);
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
        recyclerView.setNestedScrollingEnabled(false);


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
