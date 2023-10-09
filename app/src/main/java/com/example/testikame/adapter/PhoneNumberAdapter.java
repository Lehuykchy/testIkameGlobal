package com.example.testikame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testikame.R;
import com.example.testikame.model.PhoneNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.PhoneNumberViewHolder> {
    private Context context;
    private List<PhoneNumber> phoneNumberList, phoneNumberListEdit;
    private Map<Integer, Boolean> hashMap = new HashMap<>();
    private Map<Integer, Boolean> hashMapEqual = new HashMap<>();
    private EditTextPhoneNumberChangeListener editTextPhoneNumberChangeListener;
    private boolean anyTrueValueFound = false, anyTrueValueFoundEqual = false;

    public interface EditTextPhoneNumberChangeListener {
        void onEditTextPhoneChanged(int position, String newText, boolean isCheck);

        void onEditTextPhoneChangedEdit(int position, String newText, boolean isCheckEdit);
    }

    public void setEditTextPhoneNumberChangeListener(EditTextPhoneNumberChangeListener listener) {
        this.editTextPhoneNumberChangeListener = listener;
    }

    public PhoneNumberAdapter(Context context, List<PhoneNumber> phoneNumberList) {
        this.context = context;
        this.phoneNumberList = phoneNumberList;
    }

    public PhoneNumberAdapter(Context context, List<PhoneNumber> phoneNumberList, List<PhoneNumber> phoneNumberListEdit) {
        this.context = context;
        this.phoneNumberList = phoneNumberList;
        this.phoneNumberListEdit = phoneNumberListEdit;
    }

    @NonNull
    @Override
    public PhoneNumberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phone_item, parent, false);
        PhoneNumberAdapter.PhoneNumberViewHolder viewHolder = new PhoneNumberAdapter.PhoneNumberViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneNumberViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PhoneNumber phoneNumber = phoneNumberList.get(position);

        if (phoneNumber == null) {
            return;
        }
        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberList.remove(position);
                phoneNumberListEdit.remove(position);
                notifyDataSetChanged();
            }
        });


        holder.bind(phoneNumber, position);

        holder.imgDeleteInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.edtInputPhone.setText("");
            }
        });
    }

    @Override
    public int getItemCount() {
        if (phoneNumberList != null) {
            return phoneNumberList.size();
        }
        return 0;
    }

    public class PhoneNumberViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgDelete, imgDeleteInfo;
        private EditText edtInputPhone;
        private TextView tvPhoneType;

        public PhoneNumberViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDelete = itemView.findViewById(R.id.img_phoneitemdelete);
            imgDeleteInfo = itemView.findViewById(R.id.img_phoneitemdeteteinfo);
            edtInputPhone = itemView.findViewById(R.id.edt_phoneiteminfo);
            tvPhoneType = itemView.findViewById(R.id.tv_phoneitemtype);

            // Thêm TextWatcher vào EditText trong ViewHolder
            edtInputPhone.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    handleTextChange(s.toString(), getAdapterPosition());
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }

        public void bind(PhoneNumber phoneNumber, int position) {
            // Gán nội dung của EditText từ PhoneNumber
            if (phoneNumberListEdit != null) {
                if (phoneNumberListEdit.get(position).getPhoneNumber() != null) {
                    edtInputPhone.setText(phoneNumberListEdit.get(position).getPhoneNumber());
                } else {
                    edtInputPhone.setText("");
                }
            } else {
                if(phoneNumber.getPhoneNumber() != null){
                    edtInputPhone.setText(phoneNumber.getPhoneNumber());
                }else{
                    edtInputPhone.setText("");
                }

            }
        }


        // Xử lý sự thay đổi của EditText
        private void handleTextChange(String newText, int position) {
            // Xử lý sự thay đổi của EditText ở đây
            if (newText.length() > 0) {
                imgDeleteInfo.setVisibility(View.VISIBLE);
                hashMap.put(position, true);
            } else {
                imgDeleteInfo.setVisibility(View.GONE);
                hashMap.put(position, false);
            }

            for (Map.Entry<Integer, Boolean> entry : hashMap.entrySet()) {
                Boolean value = entry.getValue();
                if (value) {
                    anyTrueValueFound = true;
                    editTextPhoneNumberChangeListener.onEditTextPhoneChanged(position, newText, true);
                    break;
                } else {
                    anyTrueValueFound = false;
                }
            }

            if (!anyTrueValueFound) {
                editTextPhoneNumberChangeListener.onEditTextPhoneChanged(position, newText, false);
            }

            PhoneNumber phoneNumber = phoneNumberList.get(position);
            if ((phoneNumber.getId() == 0)) {
                if (newText.length() > 0) {
                    hashMapEqual.put(position, true);
                } else {
                    hashMapEqual.put(position, false);
                }
            } else if (!newText.trim().equals(phoneNumber.getPhoneNumber().trim())) {
                hashMapEqual.put(position, true);
                Log.d("databaseadap", "onTextChangedx: " + String.valueOf(phoneNumber.getId())
                        + ":" + newText + " " + phoneNumber.getPhoneNumber().trim());
            } else {
                hashMapEqual.put(position, false);
            }

            for (Map.Entry<Integer, Boolean> entry : hashMapEqual.entrySet()) {
                Boolean value = entry.getValue();
                if (value) {
                    anyTrueValueFoundEqual = true;
                    editTextPhoneNumberChangeListener.onEditTextPhoneChangedEdit(position, newText, true);
                    break;
                } else {
                    anyTrueValueFoundEqual = false;
                }
            }

            if (!anyTrueValueFoundEqual) {
                editTextPhoneNumberChangeListener.onEditTextPhoneChangedEdit(position, newText, false);
            }
        }

    }

    public void addPhoneNumber(PhoneNumber phoneNumber) {
        phoneNumberList.add(phoneNumber);
        notifyDataSetChanged();
    }
}

