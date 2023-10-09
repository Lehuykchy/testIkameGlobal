package com.example.testikame.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testikame.R;
import com.example.testikame.model.Email;
import com.example.testikame.model.PhoneNumber;

import java.util.List;

public class PhoneEmailAdapter extends RecyclerView.Adapter<PhoneEmailAdapter.PhoneEmailViewholder>{
    private Context context;
    private List<PhoneNumber> phoneNumberList;
    private List<Email> emailList;
    private List<?> dataList;

    public PhoneEmailAdapter(Context context, List<PhoneNumber> phoneNumberList){
        this.context = context;
        this.phoneNumberList = phoneNumberList;
        this.dataList = phoneNumberList;
    }

    public PhoneEmailAdapter(List<Email> emailList){
        this.emailList = emailList;
        this.dataList = emailList;
    }

    @NonNull
    @Override
    public PhoneEmailViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.phoneemail_item, parent, false);
        PhoneEmailAdapter.PhoneEmailViewholder viewHolder = new PhoneEmailAdapter.PhoneEmailViewholder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhoneEmailViewholder holder, int position) {
        Object item = dataList.get(position);

        if (item instanceof PhoneNumber) {
            PhoneNumber phoneNumber = (PhoneNumber) item;
            holder.tvType.setText(phoneNumber.getPhoneType());
            holder.tvInfo.setText(phoneNumber.getPhoneNumber());
        }
        else if (item instanceof Email) {
            Email email = (Email) item;
            holder.tvType.setText(email.getEmailType());
            holder.tvInfo.setText(email.getEmail());
        }

        if(position == dataList.size() - 1){
            holder.view.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if(dataList != null){
            return dataList.size();

        }
        return 0;
    }

    public class PhoneEmailViewholder extends RecyclerView.ViewHolder{
        private TextView tvType, tvInfo;
        private View view;
        public PhoneEmailViewholder(@NonNull View itemView) {
            super(itemView);
            tvType = itemView.findViewById(R.id.tv_phoneemailitemtype);
            tvInfo = itemView.findViewById(R.id.tv_phoneemailiteminfo);
            view = itemView.findViewById(R.id.phoneemailitemview);
        }
    }


}
