package com.codewithankit.thinkchats;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class chatFragment extends Fragment {
   RecyclerView chatrecylerview;

    public chatFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chat, container, false);
        chatrecylerview=view.findViewById(R.id.chatrecyclerview);
        chatrecylerview.setLayoutManager(new LinearLayoutManager(getContext()));



        FirebaseRecyclerOptions<chatmodel> options =
                new FirebaseRecyclerOptions.Builder<chatmodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Users"), chatmodel.class)
                        .build();

        FirebaseRecyclerAdapter<chatmodel,chatviewholder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<chatmodel, chatviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull chatviewholder holder, int position, @NonNull chatmodel model) {
              holder.name.setText(model.getName());
              Glide.with(holder.profile.getContext()).load(model.getImage()).placeholder(R.drawable.profile).into(holder.profile);
               String postkey=getRef(position).getKey();


                holder.chatrow.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent=new Intent(getContext(),chatting.class);
                       intent.putExtra("id",postkey);
                       startActivity(intent);

                   }
               });

            }

            @NonNull
            @Override
            public chatviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                 View view1=LayoutInflater.from(parent.getContext()).inflate(R.layout.chatfatchrow,parent,false);
                 return new chatviewholder(view1);
            }
        };

        chatrecylerview.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

        return view;
    }


    }

