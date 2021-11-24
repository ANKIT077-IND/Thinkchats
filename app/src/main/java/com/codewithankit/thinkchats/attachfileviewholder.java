package com.codewithankit.thinkchats;

import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class attachfileviewholder extends RecyclerView.ViewHolder {

    ImageButton image;
    TextView filename;
    RelativeLayout alldocument;
    public attachfileviewholder(@NonNull View itemView) {
        super(itemView);

        image=itemView.findViewById(R.id.image);
        filename=itemView.findViewById(R.id.filename);
        alldocument=itemView.findViewById(R.id.alldocument);
    }
}
