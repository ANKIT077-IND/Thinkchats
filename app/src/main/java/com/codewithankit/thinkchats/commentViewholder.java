package com.codewithankit.thinkchats;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentViewholder extends RecyclerView.ViewHolder {

    CircleImageView image;
    TextView name,time,comment;
    public commentViewholder(@NonNull View itemView) {
        super(itemView);

       image=itemView.findViewById(R.id.profile_image);
       name=itemView.findViewById(R.id.user);
       time=itemView.findViewById(R.id.time);
       comment=itemView.findViewById(R.id.usercomment);
    }
}
