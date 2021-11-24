package com.codewithankit.thinkchats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class comment extends AppCompatActivity {
    RecyclerView commentrecycler;
    CircleImageView profile;
    EditText comment;
    Button post;
    String postkey;
    DatabaseReference userreference,commentreference;
    Toolbar commenttoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        postkey=getIntent().getStringExtra("postkey");
        userreference= FirebaseDatabase.getInstance().getReference().child("Users");
        commentreference=FirebaseDatabase.getInstance().getReference().child("Postimage").child(postkey).child("comments");
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();

        commenttoolbar=findViewById(R.id.comment_toolbar);
        setSupportActionBar(commenttoolbar);
        getSupportActionBar().setTitle("Comment");

        commentrecycler=findViewById(R.id.comment_recview);
        profile=findViewById(R.id.profile_image);
        comment=findViewById(R.id.comment_text);
        post=findViewById(R.id.comment_btn);

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        FirebaseUser profileuser= FirebaseAuth.getInstance().getCurrentUser();
        String userid=profileuser.getUid();
        DatabaseReference reference=db.getReference().child("Users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( @NotNull DataSnapshot snapshot) {
                String image=snapshot.child("image").getValue(String.class);
                Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.profile).into(profile);

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {
             Toasty.error(getApplicationContext(),"database error",Toasty.LENGTH_LONG,true).show();
            }
        });


        commentrecycler.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<commentmodel> options =
                new FirebaseRecyclerOptions.Builder<commentmodel>()
                        .setQuery(commentreference, commentmodel.class)
                        .build();


        FirebaseRecyclerAdapter<commentmodel,commentViewholder> firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<commentmodel, commentViewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull commentViewholder holder, int position, @NonNull @NotNull commentmodel model) {
                holder.time.setText(model.getTime());
                holder.comment.setText(model.getComment());
                holder.name.setText(model.getName());
                Glide.with(holder.image.getContext()).load(model.image).placeholder(R.drawable.profile).into(holder.image);

            }

            @NonNull
            @NotNull
            @Override
            public commentViewholder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.commentfatchrow,parent,false);
                return new commentViewholder(view);
            }
        };

        firebaseRecyclerAdapter.startListening();
        commentrecycler.setAdapter(firebaseRecyclerAdapter);


        post.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               userreference.child(uid).addValueEventListener(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                   if (snapshot.exists()){
                       String name=snapshot.child("name").getValue(String.class);
                       String image=snapshot.child("image").getValue(String.class);
                       processcomment(name,image);


                   }
                   }

                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });

           }

           private void processcomment(String name, String image) {
               String Comments = comment.getText().toString().trim();
               if (Comments.isEmpty()) {
                   Toasty.error(getApplicationContext(), "Please write a Comments", Toasty.LENGTH_LONG, true).show();
               } else {
                   String randompostkey = uid + "" + System.currentTimeMillis();
                   Calendar calendar = Calendar.getInstance();
                   SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
                   String time = dateformat.format(calendar.getTime());

                   HashMap<String, Object> usermap = new HashMap<>();
                   usermap.put("name", name);
                   usermap.put("image", image);
                   usermap.put("comment", Comments);
                   usermap.put("time", time);

                   commentreference.child(randompostkey).updateChildren(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if (task.isSuccessful()) {
                               comment.setText("");
                               Toasty.success(getApplicationContext(), "Comments posted successfully", Toasty.LENGTH_SHORT, true).show();
                           } else {
                               Toasty.error(getApplicationContext(), "Comments posted failed", Toasty.LENGTH_LONG, true).show();

                           }

                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Toasty.error(getApplicationContext(), "database error", Toasty.LENGTH_LONG, true).show();
                       }
                   });

               }
           }
       });




    }


}
