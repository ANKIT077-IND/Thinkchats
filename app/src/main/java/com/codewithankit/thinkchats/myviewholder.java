package com.codewithankit.thinkchats;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class myviewholder extends RecyclerView.ViewHolder {
    CircleImageView profile;
    TextView username,Status,like_text,time;
    ImageView image;
    ImageButton liker_img,comment,share;

    public myviewholder(@NonNull View itemView) {
        super(itemView);

        profile=itemView.findViewById(R.id.profile_image);
        username=itemView.findViewById(R.id.username);
        Status=itemView.findViewById(R.id.poststatus);
        like_text=itemView.findViewById(R.id.liker);
        image=itemView.findViewById(R.id.postimage);
        liker_img=itemView.findViewById(R.id.like);
        comment=itemView.findViewById(R.id.comment);
        share=itemView.findViewById(R.id.share);
        time=itemView.findViewById(R.id.time);

    }

    public void getlikebuttonstatus(String uid, String postkey) {

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference likereference = db.getReference("likes");
        likereference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(postkey).hasChild(uid)) {
                    int likecount = (int) snapshot.child(postkey).getChildrenCount();
                    like_text.setText(likecount + "Likes");
                    liker_img.setImageResource(R.mipmap.thumb_foreground);
                } else {
                    int likecount = (int) snapshot.child(postkey).getChildrenCount();
                    like_text.setText(likecount + "Likes");
                    liker_img.setImageResource(R.mipmap.like1_foreground);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
