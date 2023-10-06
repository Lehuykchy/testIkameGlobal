package com.example.testikame.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testikame.R;
import com.example.testikame.model.ContactInfo;
import com.example.testikame.model.DatabaseHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EditContactActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMAGE_PICK = 1;
    private TextView tvAddImg, tvSave, tvDestroy, tvDelete;
    private EditText edtSurname, edtName;
    private TextView tvFullname;
    private ContactInfo contactInfo;
    private ImageView imgEditContact;
    private DatabaseHandler databaseHandler;
    private File imageFile;
    private String fileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editontact_activity);
        initUI();

        setClickTvSave();
        setClickTvDestroy();
        setClickEdtSurname();
        setClickEdtName();
        setClickDeleteContact();
        setImgEditContact();
        setClickTvAddImg();

    }

    private void setImgEditContact() {
        String fileName = contactInfo.getLinkImg();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        try {
            String filePath = storageDir.getAbsolutePath() + File.separator + fileName;
            Log.d("database", "onResume: " + filePath);

            File imageFile = new File(filePath);

            if (imageFile.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                imgEditContact.setImageBitmap(bitmap);
            }else {
                Toast.makeText(this, "Lỗi khi hiển thị ảnh ko tồn tại", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Lỗi khi hiển thị ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    private void setClickTvAddImg() {
        tvAddImg.setOnClickListener(new View.OnClickListener() {
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
            // Lấy URI của ảnh đã chọn
            Uri selectedImageUri = data.getData();

            // Tạo tên tệp ảnh duy nhất
            fileName = "image_" + System.currentTimeMillis() + ".jpg";

            // Tạo một đối tượng File để lưu ảnh vào vị trí lưu trữ riêng tư của ứng dụng
            File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            imageFile = new File(storageDir, fileName);

            // Sao chép dữ liệu từ InputStream vào tệp ảnh
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

                // Hiển thị ảnh trong ImageView
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.fromFile(imageFile));
                imgEditContact.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setClickDeleteContact() {
        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void setClickEdtName() {
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!edtSurname.getText().equals(contactInfo.getSurnamePerson())
                        || edtName.getText().equals(contactInfo.getNamePerson())){
                    if ((s.length() > 0 || edtSurname.getText().length()>0)) {
                        int color = Color.parseColor("#95017AFA");
                        tvSave.setEnabled(true);
                        tvSave.setTextColor(color);
                        tvFullname.setText(edtSurname.getText().toString()
                                + " " + edtName.getText().toString());
                    } else {
                        int color = Color.parseColor("#A5A5A5");
                        tvSave.setEnabled(false);
                        tvSave.setTextColor(color);
                    }
                }else {
                    int color = Color.parseColor("#A5A5A5");
                    tvSave.setEnabled(false);
                    tvSave.setTextColor(color);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setClickEdtSurname() {
        edtSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!(edtSurname.getText().equals(contactInfo.getSurnamePerson())
                        && edtName.getText().equals(contactInfo.getNamePerson()))){
                    if ((s.length() > 0 || edtSurname.getText().length()>0)) {
                        int color = Color.parseColor("#95017AFA");
                        tvSave.setEnabled(true);
                        tvSave.setTextColor(color);
                        tvFullname.setText(edtSurname.getText().toString()
                                + " " + edtName.getText().toString());
                    } else {
                        int color = Color.parseColor("#A5A5A5");
                        tvSave.setEnabled(false);
                        tvSave.setTextColor(color);
                    }
                }else {
                    int color = Color.parseColor("#A5A5A5");
                    tvSave.setEnabled(false);
                    tvSave.setTextColor(color);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setClickTvDestroy() {
        tvDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setClickTvSave() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContactInfo contactInfo1 = new ContactInfo(edtSurname.getText().toString()+ " " +edtName.getText().toString()
                        , edtSurname.getText().toString(), edtName.getText().toString(), fileName);
                databaseHandler.update(contactInfo.getIdPerson(), contactInfo1);
                finish();
            }
        });

    }

    private void initUI() {
        tvFullname = findViewById(R.id.tv_editcontactfullname);
        edtSurname = findViewById(R.id.edt_editcontactsurname);
        edtName = findViewById(R.id.edt_editcontactname);
        tvAddImg = findViewById(R.id.tv_editcontactaddimg);
        tvSave = findViewById(R.id.tv_editcontactsave);
        tvDestroy = findViewById(R.id.tv_editcontactdestroy);
        tvDelete = findViewById(R.id.tv_editcontactfullname);
        imgEditContact = findViewById(R.id.img_editcontact);
        databaseHandler = new DatabaseHandler(this, "dbcontact", null, 1);

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            int idContact = receivedBundle.getInt("idcontact", 0);
            String fullname = receivedBundle.getString("fullname", null);
            String surname = receivedBundle.getString("surname", null);
            String name = receivedBundle.getString("name", null);
            String linkimg = receivedBundle.getString("linkimg", null);
            contactInfo = new ContactInfo(idContact, fullname, surname, name, linkimg);

            tvFullname.setText(fullname);
            edtSurname.setText(surname);
            edtName.setText(name);
        }
    }
}

