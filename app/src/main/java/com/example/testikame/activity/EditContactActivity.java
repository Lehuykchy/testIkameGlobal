package com.example.testikame.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testikame.R;
import com.example.testikame.adapter.EmailAdapter;
import com.example.testikame.adapter.PhoneNumberAdapter;
import com.example.testikame.model.ContactInfo;
import com.example.testikame.model.DatabaseHandler;
import com.example.testikame.model.Email;
import com.example.testikame.model.PhoneNumber;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class EditContactActivity extends AppCompatActivity implements
        EmailAdapter.EditTextEmailChangeListener, PhoneNumberAdapter.EditTextPhoneNumberChangeListener {
    private static final int REQUEST_CODE_IMAGE_PICK = 1;
    private TextView tvAddImg, tvSave, tvDestroy, tvDelete, tvFullname, tvEditContactImg;
    private CardView cardView;
    private EditText edtSurname, edtName;
    private ContactInfo contactInfo;
    private ImageView imgEditContact;
    private DatabaseHandler databaseHandler;
    private File imageFile;
    private String fileName;
    private EmailAdapter emailAdapter;
    private PhoneNumberAdapter phoneNumberAdapter;
    private List<Email> emailList, emailListEdit;
    private List<PhoneNumber> phoneNumberList, phoneNumberListEdit;
    private RecyclerView rcvPhone, rcvEmail;
    private LinearLayout lnAddPhone, lnAddEmail;
    private boolean isCheckInputPhone, isCheckInputEmail, isCheckInputSurname, isCheckInPutName, isCheckImg;
    private Animation animation;

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
        setClickLnAddPhone();
        setClickLnAddEmail();

    }

    private void setClickLnAddEmail() {
        lnAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                Email email = new Email();
                emailListEdit.add(email);
                emailAdapter.addEmail(email);
            }
        });
    }

    private void setClickLnAddPhone() {
        lnAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                PhoneNumber phoneNumber = new PhoneNumber();
                phoneNumberListEdit.add(phoneNumber);
                phoneNumberAdapter.addPhoneNumber(phoneNumber);
            }
        });
    }

    private void setImgEditContact() {
        if(contactInfo.getLinkImg() != null) {
            tvEditContactImg.setVisibility(View.GONE);
            imgEditContact.setVisibility(View.VISIBLE);
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
                    Log.d("bugimg", "Lỗi khi hiển thị ảnh ko tồn tại");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("bugimg", "Lỗi khi hiển thị ảnh ");
            }
        }else {
            if (contactInfo.getFullnamePerson().length() > 0){
                tvEditContactImg.setText(String.valueOf(Character.toUpperCase(contactInfo.getFullnamePerson().charAt(0))));
                tvEditContactImg.setVisibility(View.VISIBLE);
                imgEditContact.setVisibility(View.GONE);
            }else {
                tvEditContactImg.setVisibility(View.GONE);
                imgEditContact.setVisibility(View.VISIBLE);
                imgEditContact.setImageResource(R.drawable.user);
            }
            cardView.setCardBackgroundColor(contactInfo.getBackgroundColor());

        }



    }

    private void setClickTvAddImg() {
        tvAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
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
                tvEditContactImg.setVisibility(View.GONE);
                imgEditContact.setVisibility(View.VISIBLE);
                isCheckImg = true;

                int color = Color.parseColor("#95017AFA");
                tvSave.setEnabled(true);
                tvSave.setTextColor(color);
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
                v.startAnimation(animation);
                onClickDetete();
            }
        });

    }

    private void onClickDetete() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_destroy_editcontact);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);

        TextView tvNo = dialog.findViewById(R.id.tv_editcontactcontinue);
        TextView tvYes = dialog.findViewById(R.id.tv_editcontactdestroy);
        TextView tvDeleteDialog =  dialog.findViewById(R.id.tv_editcontactdeletedialog);

        tvNo.setText("Hủy");
        tvYes.setText("Xóa liên hệ");
        tvDeleteDialog.setText("Bạn có muốn xóa liên hệ này không");


        tvYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHandler.deleteContact(contactInfo.getIdPerson());
                databaseHandler.deletePhoneNumber(contactInfo.getIdPerson());
                databaseHandler.deleteEmail(contactInfo.getIdPerson());
                Intent intent = new Intent(EditContactActivity.this, HomeScreenActivity.class);
                startActivity(intent);
            }
        });
        tvNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setClickEdtName() {
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvFullname.setText(edtSurname.getText().toString() + " " + s);
                if (!s.toString().trim().equals(contactInfo.getNamePerson().toString().trim())) {
                    int color = Color.parseColor("#95017AFA");
                    tvSave.setEnabled(true);
                    tvSave.setTextColor(color);
                    isCheckInPutName = true;
//                    Log.d("databasecheck", " isCheckInputSurname:" + String.valueOf(!s.equals(contactInfo.getNamePerson())) + ":" + String.valueOf(isCheckInputSurname)
//                            + " isCheckInputPhone:" + String.valueOf(isCheckInputPhone)
//                            + " isCheckInputEmail:" + String.valueOf(isCheckInputEmail)
//                            + " isCheckInputName:" + String.valueOf(isCheckInPutName) + ":"  + String.valueOf(s) + "=" + contactInfo.getNamePerson());
                    isCheck();
                } else if(isCheckInputSurname == true || isCheckInputPhone == true || isCheckInputEmail == true || isCheckImg ==true){
                    int color = Color.parseColor("#95017AFA");
                    tvSave.setEnabled(true);
                    tvSave.setTextColor(color);
                    isCheckInPutName = false;
                    isCheck();
                } else {
                    int color = Color.parseColor("#A5A5A5");
                    tvSave.setEnabled(false);
                    tvSave.setTextColor(color);
                    isCheckInPutName = false;
                    isCheck();
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
                tvFullname.setText(s + " " + edtName.getText().toString());
                if (!s.toString().trim().equals(contactInfo.getSurnamePerson().trim())) {
                    int color = Color.parseColor("#95017AFA");
                    tvSave.setEnabled(true);
                    tvSave.setTextColor(color);
                    isCheckInputSurname = true;
                    isCheck();
                } else if(isCheckInPutName == true || isCheckInputPhone == true || isCheckInputEmail == true || isCheckImg == true){
                    int color = Color.parseColor("#95017AFA");
                    tvSave.setEnabled(true);
                    tvSave.setTextColor(color);
                    isCheckInputSurname = false;
                    isCheck();
                } else {
                    int color = Color.parseColor("#A5A5A5");
                    tvSave.setEnabled(false);
                    tvSave.setTextColor(color);
                    isCheckInputSurname = false;
                    isCheck();
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
                v.startAnimation(animation);
                if (!isCheckInputEmail && !isCheckInputSurname && !isCheckInPutName && !isCheckInputPhone){
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("idcontact", contactInfo.getIdPerson());
                    setResult(RESULT_OK, resultIntent);
                    finish();
                }else {
                    onClickDestroy();
                }
            }
        });
    }

    private void onClickDestroy() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_destroy_editcontact);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);

        TextView tvContinue = dialog.findViewById(R.id.tv_editcontactcontinue);
        TextView tvDestroyDialog = dialog.findViewById(R.id.tv_editcontactdestroy);


        tvDestroyDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                finish();
            }
        });
        tvContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void setClickTvSave() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(animation);
                ContactInfo contactInfo1 = new ContactInfo(edtSurname.getText().toString()+ " " +edtName.getText().toString()
                        , edtSurname.getText().toString(), edtName.getText().toString(), fileName);
                databaseHandler.update(contactInfo.getIdPerson(), contactInfo1);

                databaseHandler.deletePhoneNumber(contactInfo.getIdPerson());
                databaseHandler.deleteEmail(contactInfo.getIdPerson());
                for(int i = 0; i<phoneNumberListEdit.size(); i++){
                    if(phoneNumberListEdit.get(i).getPhoneNumber() == null || phoneNumberListEdit.get(i).getPhoneNumber().length()>0){
                        phoneNumberListEdit.get(i).setIdPerson((int)contactInfo.getIdPerson());
                        databaseHandler.addPhone(phoneNumberListEdit.get(i));
                    }
                }

                for(int i =0; i<emailListEdit.size(); i++){
                    if(emailListEdit.get(i).getEmail() == null || emailListEdit.get(i).getEmail().length()>0){
                        emailListEdit.get(i).setIdPerson((int)contactInfo.getIdPerson());
                        databaseHandler.addEmail(emailListEdit.get(i));
                    }
                }

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
        tvDelete = findViewById(R.id.tv_editcontactdelete);
        imgEditContact = findViewById(R.id.img_editcontact);
        rcvEmail = findViewById(R.id.rcv_editcontactemail);
        rcvPhone = findViewById(R.id.rcv_editcontactphone);
        lnAddPhone = findViewById(R.id.ln_editcontactaddphone);
        lnAddEmail = findViewById(R.id.ln_editcontactaddemail);
        cardView = findViewById(R.id.cardview_editcontact);
        tvEditContactImg = findViewById(R.id.tv_editcontactimg);
        databaseHandler = new DatabaseHandler(this, "dbcontact", null, 1);

        //xét hiệu ứng click
        animation = new AlphaAnimation(1.0f, 0.0f);
        animation.setDuration(200);

        int color = Color.parseColor("#A5A5A5");
        tvSave.setEnabled(false);
        tvSave.setTextColor(color);

        Bundle receivedBundle = getIntent().getExtras();
        if (receivedBundle != null) {
            int idContact = receivedBundle.getInt("idcontact", 0);
            String fullname = receivedBundle.getString("fullname", null);
            String surname = receivedBundle.getString("surname", null);
            String name = receivedBundle.getString("name", null);
            String linkimg = receivedBundle.getString("linkimg", null);
            int backgroundColor = receivedBundle.getInt("backgroundcolor", 0);
            contactInfo = new ContactInfo(idContact, fullname, surname, name, linkimg, backgroundColor);
            if(linkimg != null){
                tvAddImg.setText("Sửa ảnh");
            }else {
                tvAddImg.setText("Thêm ảnh");
            }

            tvFullname.setText(fullname);
            edtSurname.setText(surname);
            edtName.setText(name);
        }

        emailList = new ArrayList<>();
        emailListEdit = new ArrayList<>();
        getEmailList();
        LinearLayoutManager lnEmailManager = new LinearLayoutManager(this);
        rcvEmail.setLayoutManager(lnEmailManager);
        emailAdapter = new EmailAdapter(this, emailList, emailListEdit) ;
        emailAdapter.setEditTextEmailChangeListener(this);
        rcvEmail.setAdapter(emailAdapter);
        rcvEmail.setNestedScrollingEnabled(false);


        phoneNumberList = new ArrayList<>();
        phoneNumberListEdit = new ArrayList<>();
        getPhoneNumberList();
        LinearLayoutManager lnPhoneManager = new LinearLayoutManager(this);
        rcvPhone.setLayoutManager(lnPhoneManager);
        phoneNumberAdapter = new PhoneNumberAdapter(this, phoneNumberList, phoneNumberListEdit);
        phoneNumberAdapter.setEditTextPhoneNumberChangeListener(this);
        rcvPhone.setAdapter(phoneNumberAdapter);
        rcvPhone.setNestedScrollingEnabled(false);
    }

    private void getPhoneNumberList() {
        phoneNumberList.addAll(databaseHandler.getPhoneNumberContact(contactInfo.getIdPerson()));
        phoneNumberListEdit.addAll(databaseHandler.getPhoneNumberContact(contactInfo.getIdPerson()));
    }

    private void getEmailList() {
        emailList.addAll(databaseHandler.getEmailContact(contactInfo.getIdPerson()));
        emailListEdit.addAll(databaseHandler.getEmailContact(contactInfo.getIdPerson()));
    }

    @Override
    public void onEditTextEmailChanged(int position, String newText, boolean check) {

    }

    @Override
    public void onEditTextEmailChangedEdit(int position, String newText, boolean check) {
        isCheckInputEmail = check;
        emailListEdit.get(position).setEmail(newText);
        Log.d("databaseadap", "onTextChangededit: " + String.valueOf(position) +" "+ emailList.get(position).getEmail()
                +":" + newText + " " + String.valueOf(emailList.size()) + " " + String.valueOf(emailListEdit.size()));
        if (check) {
            int color = Color.parseColor("#95017AFA");
            tvSave.setEnabled(true);
            tvSave.setTextColor(color);
            isCheck();
        } else if(isCheckInPutName == true || isCheckInputSurname == true || isCheckInputPhone == true || isCheckImg == true){
            int color = Color.parseColor("#95017AFA");
            tvSave.setEnabled(true);
            tvSave.setTextColor(color);
            isCheck();
        } else {
            int color = Color.parseColor("#A5A5A5");
            tvSave.setEnabled(false);
            tvSave.setTextColor(color);
            isCheck();
        }
    }

    @Override
    public void onEditTextPhoneChanged(int position, String newText, boolean isCheck) {

    }

    @Override
    public void onEditTextPhoneChangedEdit(int position, String newText, boolean isCheckEdit) {
        isCheckInputPhone = isCheckEdit;
        phoneNumberListEdit.get(position).setPhoneNumber(newText);
        Log.d("databaseadap", "onTextChangededit: " + String.valueOf(position) +" "+ phoneNumberList.get(position).getPhoneNumber()
                +":" + newText + " " + String.valueOf(phoneNumberList.size()) + " "+String.valueOf(phoneNumberListEdit.size()));
        if (isCheckEdit) {
            int color = Color.parseColor("#95017AFA");
            tvSave.setEnabled(true);
            tvSave.setTextColor(color);
            isCheck();
        } else if(isCheckInPutName == true || isCheckInputSurname == true || isCheckInputEmail == true || isCheckImg == true){
            int color = Color.parseColor("#95017AFA");
            tvSave.setEnabled(true);
            tvSave.setTextColor(color);
            isCheck();
        } else {
            int color = Color.parseColor("#A5A5A5");
            tvSave.setEnabled(false);
            tvSave.setTextColor(color);
            isCheck();
        }

    }

    private void isCheck(){
        Log.d("databasecheck", " isCheckInputSurname:" + String.valueOf(isCheckInputSurname)
                + " isCheckInputPhone:" + String.valueOf(isCheckInputPhone)
                + " isCheckInputEmail:" + String.valueOf(isCheckInputEmail)
                + " isCheckInputName:" + String.valueOf(isCheckInPutName));
    }
}

