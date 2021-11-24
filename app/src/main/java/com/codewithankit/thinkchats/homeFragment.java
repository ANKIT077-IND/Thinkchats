package com.codewithankit.thinkchats;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;


/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment {
    FloatingActionButton floatingbutton;
    RecyclerView recyclerView;
    DatabaseReference likereference;
    boolean likeclick=false;
    public homeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        likereference=FirebaseDatabase.getInstance().getReference("likes");
        floatingbutton=view.findViewById(R.id.floating);
        recyclerView=view.findViewById(R.id.recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        FirebaseRecyclerOptions<postmodel> options =
                new FirebaseRecyclerOptions.Builder<postmodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Postimage"), postmodel.class)
                        .build();


        FirebaseRecyclerAdapter<postmodel,myviewholder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<postmodel, myviewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull postmodel model) {
            holder.username.setText(model.getUsername());
            holder.Status.setText(model.getStatus());
            holder.time.setText(model.getTime());
            Glide.with(holder.profile.getContext()).load(model.getProfile()).placeholder(R.drawable.profile).into(holder.profile);
            Glide.with(holder.image.getContext()).load(model.getImage()).into(holder.image);

                FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                String uid= user.getUid();
                String postkey=getRef(position).getKey();

                holder.getlikebuttonstatus(uid,postkey);
                holder.liker_img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                        DatabaseReference reference = db.getReference("likes");

                        likeclick = true;
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(likeclick) {
                                    if(snapshot.child(postkey).hasChild(uid)) {
                                        reference.child(postkey).child(uid).removeValue();
                                        likeclick = false;
                                    } else {
                                        reference.child(postkey).child(uid).setValue(true);
                                        likeclick = false;
                                    }
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                          Toasty.error(getContext(),"database error",Toasty.LENGTH_LONG,true).show();
                            }
                        });
                    }
                });

                holder.comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(getContext(),comment.class);
                        intent.putExtra("postkey",postkey);
                        startActivity(intent);
                    }
                });

                holder.share.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                                Toasty.info(getContext(),"this feature comming soon",Toasty.LENGTH_SHORT,true).show();


                    }
                });



            }

            @NonNull
            @Override
            public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.postphotodesign,parent,false);
                return new myviewholder(view);
            }
        };
        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();

        floatingbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),post.class);
                startActivity(intent);

            }
        });

        return view;
    }

}
