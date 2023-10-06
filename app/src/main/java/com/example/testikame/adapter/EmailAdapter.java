package com.example.testikame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testikame.R;
import com.example.testikame.model.Email;


import java.util.List;

public class EmailAdapter extends RecyclerView.Adapter<EmailAdapter.EmailViewHolder>{
    private Context context;
    private List<Email> emailList;
    private PhoneNumberAdapter.IclickListenerEdt iclickListenerEdt;

    public interface IclickListenerEdt{
        public void edtChangeListener(int position, Email email);
    }

    public EmailAdapter(Context context, List<Email> emailList, PhoneNumberAdapter.IclickListenerEdt iclickListenerEdt){
        this.context = context;
        this.emailList = emailList;
        this.iclickListenerEdt = iclickListenerEdt;
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

        if(email == null){
            return;
        }

        EditText edtInputEmail = holder.edtInputEmail;
        ImageView imgDeleteInfo = holder.imgDeleteInfo;

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailList.remove(position);
                notifyDataSetChanged();
            }
        });


        edtInputEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 || edtInputEmail.getText().length()>0) {
                    imgDeleteInfo.setVisibility(View.VISIBLE);
                    email.setEmail(String.valueOf(edtInputEmail.getText().toString()));
                } else {
                    imgDeleteInfo.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        imgDeleteInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtInputEmail.setText("");
            }
        });

    }

    @Override
    public int getItemCount() {
        if(emailList != null){
            return emailList.size();
        }
        return 0;
    }

    public class EmailViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgDelete, imgDeleteInfo;
        private EditText edtInputEmail;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDelete = itemView.findViewById(R.id.img_emailitemdelete);
            imgDeleteInfo =itemView.findViewById(R.id.img_emailitemdeteteinfo);
            edtInputEmail = itemView.findViewById(R.id.edt_emailiteminfo);
        }
    }
}

