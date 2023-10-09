package com.example.testikame.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.testikame.R;
import com.example.testikame.model.ContactInfo;

import java.io.File;
import java.util.List;
import java.util.Random;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder>{
    private List<ContactInfo> contactInfoList;
    private Context context;

    private ContactAdapter.IClickListener iClickListener;

    public interface IClickListener{
        void onClickItem(int position);
    }

    public ContactAdapter(Context context, List<ContactInfo> contactInfoList, ContactAdapter.IClickListener iClickListener){
        this.iClickListener = iClickListener;
        this.context = context;
        this.contactInfoList = contactInfoList;
    }
    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);
        ContactAdapter.ContactViewHolder viewHolder = new ContactAdapter.ContactViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ContactInfo contactInfo = contactInfoList.get(position);

        if(contactInfo == null){
            return;
        }

        String name = contactInfo.getFullnamePerson().toString().trim();
        if(name != null){
            holder.tvName.setText(contactInfo.getFullnamePerson().toString());
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iClickListener.onClickItem(position);
            }
        });

        Log.d("database", "adapter" + contactInfo.getLinkImg());
        if(contactInfo.getLinkImg() == null){
            if(name.length()>0) {
                holder.tvImg.setText(String.valueOf(Character.toUpperCase(name.charAt(0))));
                holder.tvImg.setVisibility(View.VISIBLE);
                holder.imgItemContact.setVisibility(View.GONE);
            }else {
                holder.imgItemContact.setVisibility(View.VISIBLE);
                holder.tvImg.setVisibility(View.GONE);
                holder.imgItemContact.setImageResource(R.drawable.user);
            }
            holder.cardView.setCardBackgroundColor(contactInfo.getBackgroundColor());
        }else {
            //xét ảnh imgViewContactItem
            holder.tvImg.setVisibility(View.GONE);
            holder.imgItemContact.setVisibility(View.VISIBLE);
            String fileName = contactInfo.getLinkImg();
            File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                String filePath = storageDir.getAbsolutePath() + File.separator + fileName;
                Log.d("database", "onResume: " + filePath);

                File imageFile = new File(filePath);

                if (imageFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    holder.imgItemContact.setImageBitmap(bitmap);
                } else {
                    Log.d("databaseadpater", "Lỗi khi hiển thị ảnh ko tồn tại");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("databaseadpater", "Lỗi khi hiển thị ảnh" + e);
            }
        }

    }

    @Override
    public int getItemCount() {
        if(contactInfoList != null){
            return contactInfoList.size();
        }
        return 0;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder{
        private ImageView imgItemContact;
        private TextView tvName, tvImg;
        private LinearLayout linearLayout;
        private CardView cardView;
        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_contactitemfullname);
            imgItemContact = itemView.findViewById(R.id.img_contactitem);
            tvImg = itemView.findViewById(R.id.tv_contactitem);
            linearLayout = itemView.findViewById(R.id.lnitemcontact);
            cardView = itemView.findViewById(R.id.cardview_contactitem);
        }
    }

    public ContactInfo GetContactInfoByPosition(int position) {
        List<ContactInfo> list = this.GetListContacts();
        return list.get(position);
    }

    private List<ContactInfo> GetListContacts() {
        return this.contactInfoList;
    }
}
