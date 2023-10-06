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
import com.example.testikame.model.PhoneNumber;

import java.util.List;

public class PhoneNumberAdapter extends RecyclerView.Adapter<PhoneNumberAdapter.PhoneNumberViewHolder>{
    private Context context;
    private List<PhoneNumber> phoneNumberList;
    private PhoneNumberAdapter.IclickListenerEdt iclickListenerEdt;

    public interface IclickListenerEdt{
        public void edtChangeListener(int position, PhoneNumber phoneNumber);
    }

    public PhoneNumberAdapter(Context context, List<PhoneNumber> phoneNumberList, PhoneNumberAdapter.IclickListenerEdt iclickListenerEdt){
        this.context = context;
        this.phoneNumberList = phoneNumberList;
        this.iclickListenerEdt = iclickListenerEdt;
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

        if(phoneNumber == null){
            return;
        }

        EditText edtInputPhone = holder.edtInputPhone;
        ImageView imgDeleteInfo = holder.imgDeleteInfo;

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumberList.remove(position);
                notifyDataSetChanged();
            }
        });


        edtInputPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0 || edtInputPhone.getText().length()>0) {
                    imgDeleteInfo.setVisibility(View.VISIBLE);
                    phoneNumber.setPhoneNumber(String.valueOf(edtInputPhone.getText().toString()));
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
                edtInputPhone.setText("");
            }
        });

    }

    @Override
    public int getItemCount() {
        if(phoneNumberList != null){
            return phoneNumberList.size();
        }
        return 0;
    }

    public class PhoneNumberViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgDelete, imgDeleteInfo;
        private EditText edtInputPhone;

        public PhoneNumberViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDelete = itemView.findViewById(R.id.img_phoneitemdelete);
            imgDeleteInfo =itemView.findViewById(R.id.img_phoneitemdeteteinfo);
            edtInputPhone = itemView.findViewById(R.id.edt_phoneiteminfo);
        }
    }
}
