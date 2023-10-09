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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testikame.R;
import com.example.testikame.model.Email;
import com.example.testikame.model.PhoneNumber;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder> {
    private Context context;
    private List<Email> emailList, emailListEdit;
    private EmailAdapter.EditTextEmailChangeListener editTextEmailChangeListener;
    private Map<Integer, Boolean> hashMap = new HashMap<>();
    private Map<Integer, Boolean> hashMapEqual = new HashMap<>();
    private boolean anyTrueValueFound = false, anyTrueValueFoundEqual = false;

    public interface EditTextEmailChangeListener {
        void onEditTextEmailChanged(int position, String newText, boolean check);
        void onEditTextEmailChangedEdit(int position, String newText, boolean check);
    }

    public void setEditTextEmailChangeListener(EmailAdapter.EditTextEmailChangeListener listener) {
        this.editTextEmailChangeListener = listener;
    }

    public EmailAdapter(Context context, List<Email> emailList) {
        this.context = context;
        this.emailList = emailList;
    }

    public EmailAdapter(Context context, List<Email> emailList, List<Email> emailListEdit) {
        this.context = context;
        this.emailList = emailList;
        this.emailListEdit = emailListEdit;
    }

    @NonNull
    @Override
    public EmailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.email_item, parent, false);
        EmailAdapter.EmailViewHolder viewHolder = new EmailAdapter.EmailViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EmailViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Email email = emailList.get(position);

        if (email == null) {
            return;
        }

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailList.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.bind(email, position);

        holder.imgDeleteInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.edtInputEmail.setText("");
            }
        });

    }

    @Override
    public int getItemCount() {
        if (emailList != null) {
            return emailList.size();
        }
        return 0;
    }

    public class EmailViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgDelete, imgDeleteInfo;
        private EditText edtInputEmail;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDelete = itemView.findViewById(R.id.img_emailitemdelete);
            imgDeleteInfo = itemView.findViewById(R.id.img_emailitemdeteteinfo);
            edtInputEmail = itemView.findViewById(R.id.edt_emailiteminfo);

            edtInputEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    handleTextChange(s.toString(), getLayoutPosition());
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }



        public void bind(Email email, int position) {
            if (emailListEdit != null) {
                if (emailListEdit.get(position).getEmail() != null) {
                    edtInputEmail.setText(emailListEdit.get(position).getEmail());
                } else {
                    edtInputEmail.setText("");
                }
            } else{
                if (email.getEmail() != null) {
                    edtInputEmail.setText(email.getEmail());
                } else {
                    edtInputEmail.setText("");
                }
            }
        }

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
                    editTextEmailChangeListener.onEditTextEmailChanged(position, newText, true);
                    break;
                } else {
                    anyTrueValueFound = false;
                }
            }

            if (!anyTrueValueFound) {
                editTextEmailChangeListener.onEditTextEmailChanged(position, newText, false);
            }

            Email email = emailList.get(position);
            if ((email.getId() == 0)) {
                if (newText.length() > 0) {
                    hashMapEqual.put(position, true);
                } else {
                    hashMapEqual.put(position, false);
                }
            } else if (!newText.trim().equals(email.getEmail().trim())) {
                hashMapEqual.put(position, true);
                Log.d("databaseadap", "onTextChangedx: " + String.valueOf(email.getId())
                        + ":" + newText + " " + email.getEmail().trim());
            } else {
                hashMapEqual.put(position, false);
            }

            for (Map.Entry<Integer, Boolean> entry : hashMapEqual.entrySet()) {
                Boolean value = entry.getValue();
                if (value) {
                    anyTrueValueFoundEqual = true;
                    editTextEmailChangeListener.onEditTextEmailChangedEdit(position, newText, true);
                    break;
                } else {
                    anyTrueValueFoundEqual = false;
                }
            }

            if (!anyTrueValueFoundEqual) {
                editTextEmailChangeListener.onEditTextEmailChangedEdit(position, newText, false);
            }

        }

    }




    // Xử lý sự thay đổi của EditText

    public void addEmail(Email email) {
        emailList.add(email);
        notifyDataSetChanged();
    }
}

