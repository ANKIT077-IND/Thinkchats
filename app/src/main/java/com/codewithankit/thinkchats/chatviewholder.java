package com.codewithankit.thinkchats;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class chatviewholder extends RecyclerView.ViewHolder {
    CircleImageView profile;
    TextView name;
    RelativeLayout chatrow;
    public chatviewholder(@NonNull View itemView) {
        super(itemView);

        profile=itemView.findViewById(R.id.profile_image);
        name=itemView.findViewById(R.id.username);
        chatrow=itemView.findViewById(R.id.chatrow);
         }
}
