package com.example.testikame.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testikame.R;
import com.example.testikame.adapter.EmailAdapter;
import com.example.testikame.adapter.PhoneNumberAdapter;
import com.example.testikame.model.ContactInfo;
import com.example.testikame.model.DatabaseHandler;
import com.example.testikame.model.Email;
import com.example.testikame.model.PhoneNumber;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class FragmentBottomSheetMoreAddContact extends BottomSheetDialogFragment
        implements PhoneNumberAdapter.EditTextPhoneNumberChangeListener , EmailAdapter.EditTextEmailChangeListener{
    private static final int REQUEST_CODE_IMAGE_PICK = 1;
    private View view;
    private DatabaseHandler databaseHandler;
    private TextView tvAddImg, tvFullName, tvDestroy, tvSave;
    private TextView tvCheck;
    private Button btCheck;
    private EditText edtSurname, edtName;
    private ImageView imgAddImg;
    private LinearLayout lnAddPhone, lnAddEmail;
    private String fileName, a;
    private PhoneNumberAdapter phoneNumberAdapter;
    private EmailAdapter emailAdapter;
    private RecyclerView rcvPhone, rcvEmail;
    private List<PhoneNumber> phoneNumberList;
    private List<Email> emailList;
    private boolean isCheckInputPhone, isCheckInputEmail, isCheckInputSurname, isCheckInPutName;
    private OnDataChangeListener listener;
    public void setOnDataChangeListener(OnDataChangeListener listener) {
        this.listener = listener;
    }

    public interface OnDataChangeListener {
        void onDataChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bottomsheetmore_addcontact, container, false);

        initUI();

        setClickTvDestroy();
        setCLickTvSave();
        setClickEdtSurname();
        setClickEdtName();
        setClickTvAddImg();
        setClickImgAddImg();
        setClickLnAddPhone();
        setClickLnAddEmail();

        setBtCheck();

        return view;
    }

    private void isCheck(){
        Log.d("databasecheck", " isCheckInputSurname:" + String.valueOf(isCheckInputSurname)
                + " isCheckInputPhone:" + String.valueOf(isCheckInputPhone)
                + " isCheckInputEmail:" + String.valueOf(isCheckInputEmail)
                + " isCheckInputName:" + String.valueOf(isCheckInPutName));
    }

    private void setBtCheck() {
        btCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                a = String.valueOf(phoneNumberList.size());
                for(int i = 0; i < phoneNumberList.size(); i++){
                    a += phoneNumberList.get(i).getPhoneNumber() + " - " ;
                }

                for(int i = 0; i < emailList.size(); i++){
                    a += emailList.get(i).getEmail() + " - " ;
                }
                tvCheck.setText(a);
            }
        });
    }

    private void setClickLnAddEmail() {
        lnAddEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email email = new Email();
                emailList.add(email);
                emailAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setClickLnAddPhone() {
        lnAddPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneNumber phoneNumber = new PhoneNumber();
//                phoneNumberList.add(phoneNumber);
//                phoneNumberAdapter.notifyDataSetChanged();
                phoneNumberAdapter.addPhoneNumber(phoneNumber);
            }
        });

    }

    private void setClickImgAddImg() {
        imgAddImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICK);
            }
        });
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

    private void setClickEdtName() {
        edtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    int color = Color.parseColor("#95017AFA");
                    tvSave.setEnabled(true);
                    tvSave.setTextColor(color);
                    tvFullName.setText(edtSurname.getText().toString()
                            + " " + edtName.getText().toString());
                    isCheckInPutName = true;
                    isCheck();
                }else if(isCheckInputSurname == true || isCheckInputPhone == true || isCheckInputEmail == true){
                    int color = Color.parseColor("#95017AFA");
                    tvSave.setEnabled(true);
                    tvSave.setTextColor(color);
                    tvFullName.setText(edtSurname.getText().toString()
                            + " " + edtName.getText().toString());
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
                if (s.length() > 0 ) {
                    int color = Color.parseColor("#95017AFA");
                    tvSave.setEnabled(true);
                    tvSave.setTextColor(color);
                    tvFullName.setText(edtSurname.getText().toString()
                            + " " + edtName.getText().toString());
                    isCheckInputSurname = true;
                    isCheck();
                } else if(isCheckInPutName == true || isCheckInputPhone == true || isCheckInputEmail == true){
                    int color = Color.parseColor("#95017AFA");
                    tvSave.setEnabled(true);
                    tvSave.setTextColor(color);
                    tvFullName.setText(edtSurname.getText().toString()
                            + " " + edtName.getText().toString());
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

    private void setCLickTvSave() {
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ContactInfo contactInfo = new ContactInfo(edtSurname.getText().toString()+ " " +edtName.getText().toString()
                        , edtSurname.getText().toString(), edtName.getText().toString(), fileName);
                long idContact = databaseHandler.addContact(contactInfo);

                for (int i=0; i<phoneNumberList.size(); i++){
                    if(phoneNumberList.get(i).getPhoneNumber().length()>0){
                        phoneNumberList.get(i).setIdPerson((int)idContact);
                        databaseHandler.addPhone(phoneNumberList.get(i));
                        Log.d("database", "onClick: " + phoneNumberList.get(i).getPhoneNumber());
                    }
                }

                for (int i=0; i<emailList.size(); i++){
                    if(emailList.get(i).getEmail().length()>0){
                        emailList.get(i).setIdPerson((int)idContact);
                        databaseHandler.addEmail(emailList.get(i));
                        Log.d("database", "onClick: " + phoneNumberList.get(i).getPhoneNumber());
                    }
                }
                if (listener != null) {
                    listener.onDataChanged();
                }
                dismiss();
            }
        });
    }

    private void setClickTvDestroy() {
        tvDestroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE_PICK && resultCode == getActivity().RESULT_OK && data != null) {
            // Lấy URI của ảnh đã chọn
            Uri selectedImageUri = data.getData();

            // Tạo tên tệp ảnh duy nhất
            fileName = "image_" + System.currentTimeMillis() + ".jpg";

            // Tạo một đối tượng File để lưu ảnh vào vị trí lưu trữ riêng tư của ứng dụng
            File storageDir = requireActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imageFile = new File(storageDir, fileName);

            // Sao chép dữ liệu từ InputStream vào tệp ảnh
            try {
                InputStream inputStream = requireContext().getContentResolver().openInputStream(selectedImageUri);
                OutputStream outputStream = new FileOutputStream(imageFile);
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                inputStream.close();
                outputStream.close();

                // Hiển thị ảnh trong ImageView của Fragment
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireContext().getContentResolver(), Uri.fromFile(imageFile));
                imgAddImg.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void initUI() {
        tvAddImg = view.findViewById(R.id.tv_addimgaddcontact);
        tvFullName = view.findViewById(R.id.tv_fullnameaddcontact);
        edtSurname = view.findViewById(R.id.edt_curnameaddcontact);
        edtName = view.findViewById(R.id.edt_nameaddcontact);
        tvDestroy = view.findViewById(R.id.tv_destroyaddcontact);
        tvSave = view.findViewById(R.id.tv_saveaddcontact);
        imgAddImg = view.findViewById(R.id.img_addimgaddcontact);
        rcvPhone = view.findViewById(R.id.rcv_addcontactaddphone);
        rcvEmail = view.findViewById(R.id.rcv_addcontactaddemail);
        lnAddPhone = view.findViewById(R.id.ln_addcontactaddphone);
        lnAddEmail = view.findViewById(R.id.ln_addcontactaddemail);
        databaseHandler = new DatabaseHandler(getActivity(), "dbcontact", null, 1);
        tvCheck = view.findViewById(R.id.tvCheck);
        btCheck = view.findViewById(R.id.btcheck);
        tvSave.setEnabled(false);

        phoneNumberList = new ArrayList<>();
        LinearLayoutManager lnPhoneManager = new LinearLayoutManager(getActivity());
        rcvPhone.setLayoutManager(lnPhoneManager);
        phoneNumberAdapter = new PhoneNumberAdapter(getActivity(), phoneNumberList);
        phoneNumberAdapter.setEditTextPhoneNumberChangeListener(this);
        rcvPhone.setAdapter(phoneNumberAdapter);
//        rcvPhone.setNestedScrollingEnabled(false);


        emailList = new ArrayList<>();
        LinearLayoutManager lnEmailManager = new LinearLayoutManager(getActivity());
        rcvEmail.setLayoutManager(lnEmailManager);
        emailAdapter = new EmailAdapter(getActivity(), emailList);
        emailAdapter.setEditTextEmailChangeListener(this);
        rcvEmail.setAdapter(emailAdapter);
        rcvEmail.setNestedScrollingEnabled(false);
    }

    @Override
    public void onEditTextPhoneChanged(int position, String newText, boolean isCheck) {
        phoneNumberList.get(position).setPhoneNumber(newText);
        isCheckInputPhone = isCheck;
        if(isCheck){
            int color = Color.parseColor("#95017AFA");
            tvSave.setEnabled(true);
            tvSave.setTextColor(color);
            isCheck();
        }else if(isCheckInPutName == true || isCheckInputSurname == true || isCheckInputEmail == true){
            int color = Color.parseColor("#95017AFA");
            tvSave.setEnabled(true);
            tvSave.setTextColor(color);
            isCheck();
        }else {
            int color = Color.parseColor("#A5A5A5");
            tvSave.setEnabled(false);
            tvSave.setTextColor(color);
            isCheck();
        }
    }

    @Override
    public void onEditTextPhoneChangedEdit(int position, String newText, boolean isCheckEdit) {

    }

    @Override
    public void onEditTextEmailChanged(int position, String newText, boolean isCheck) {
        isCheckInputEmail = isCheck;
        emailList.get(position).setEmail(newText);
        if(isCheck){
            int color = Color.parseColor("#95017AFA");
            tvSave.setEnabled(true);
            tvSave.setTextColor(color);
            isCheck();
        }else if(isCheckInPutName == true || isCheckInputSurname == true || isCheckInputPhone == true){
            int color = Color.parseColor("#95017AFA");
            tvSave.setEnabled(true);
            tvSave.setTextColor(color);
            isCheck();
        }else {
            int color = Color.parseColor("#A5A5A5");
            tvSave.setEnabled(false);
            tvSave.setTextColor(color);
            isCheck();
        }
    }

    @Override
    public void onEditTextEmailChangedEdit(int position, String newText, boolean check) {

    }
}
