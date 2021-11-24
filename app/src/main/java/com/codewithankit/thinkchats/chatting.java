package com.codewithankit.thinkchats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static java.lang.String.valueOf;

public class chatting extends AppCompatActivity {
    String postkey;
    CircleImageView profile;
    TextView name;
    Toolbar chattolbar;
    ImageButton sendmessage,attachfile;
    EditText message;
    DatabaseReference rootref,chatreference;
    RecyclerView chatrecyclerView;
    private FirebaseAuth mAuth;
    RelativeLayout relative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        String currentuid=user.getUid();
        mAuth=FirebaseAuth.getInstance();
        rootref=FirebaseDatabase.getInstance().getReference();
        postkey=getIntent().getStringExtra("id");
        profile=findViewById(R.id.profile_image);
        relative=findViewById(R.id.relative);
        name=findViewById(R.id.username);
        chattolbar=findViewById(R.id.chat_toolbar);
        setSupportActionBar(chattolbar);
        sendmessage=findViewById(R.id.sendmessage);
        message=findViewById(R.id.message);
        attachfile=findViewById(R.id.attachfile);
        chatrecyclerView=findViewById(R.id.chatrecycler);
        chatrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<messagemodel> options =
                new FirebaseRecyclerOptions.Builder<messagemodel>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("message").child(currentuid).child(postkey), messagemodel.class)
                        .build();


        FirebaseRecyclerAdapter<messagemodel,messageVIewholder>firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<messagemodel, messageVIewholder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull messageVIewholder holder, int position, @NonNull messagemodel model) {
                holder.time.setText(model.getPosttime());
                holder.date.setText(model.getDate());
                holder.messagechat.setText(model.getMessage());

                String currentuser=mAuth.getCurrentUser().getUid();
                String fromuser=model.getFrom();

                if (fromuser.equals(currentuser)){
                    holder.messagechat.setBackgroundResource(R.drawable.textbackground);
                    holder.messagechat.setTextColor(Color.BLACK);

                }
                else{
                    holder.messagechat.setBackgroundResource(R.drawable.fromuserbackground);
                    holder.messagechat.setTextColor(Color.BLACK);

                }
            }

            @NonNull
            @Override
            public messageVIewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
              View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.messagefatchrow,parent,false);
              return new messageVIewholder(view);
            }
        };
        firebaseRecyclerAdapter.startListening();
        chatrecyclerView.setAdapter(firebaseRecyclerAdapter);



        FirebaseDatabase database=FirebaseDatabase.getInstance();
        DatabaseReference reference=database.getReference().child("Users").child(postkey);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String uname=snapshot.child("name").getValue(String.class);
                String image=snapshot.child("image").getValue(String.class);
                Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.profile).into(profile);
                name.setText(uname);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
              Toasty.error(getApplicationContext(),"database error",Toasty.LENGTH_LONG,true).show();
            }
        });

        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chat=message.getText().toString();
                if (chat.isEmpty()){
                    Toasty.error(getApplicationContext(),"Please types a message",Toasty.LENGTH_LONG,true).show();
                }
                else{
                      FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                      String uid=user.getUid();
                      chatreference=FirebaseDatabase.getInstance().getReference().child("message").child(uid).child(postkey).push();
                      String push_id=chatreference.getKey();

                      String current_user="message/"+uid+"/"+postkey;
                      String chat_user="message/"+postkey+"/"+uid;

                    Calendar calendar=Calendar.getInstance();
                    SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-YYYY");
                    String date=dateFormat.format(calendar.getTime());
                    SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
                    String time = dateformat.format(calendar.getTime());

                    HashMap<String,Object>usermap=new HashMap<>();
                    usermap.put("message",chat);
                    usermap.put("seen",false);
                    usermap.put("type","text");
                    usermap.put("time", ServerValue.TIMESTAMP);
                    usermap.put("date",date);
                    usermap.put("posttime",time);
                    usermap.put("from",uid);

                    HashMap<String,Object>chatmap=new HashMap<>();
                    chatmap.put(current_user+"/"+push_id,usermap);
                    chatmap.put(chat_user+"/"+push_id,usermap);
                    message.setText("");

                    rootref.updateChildren(chatmap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                         if (error!=null){
                           Toasty.error(getApplicationContext(),error.getMessage(),Toasty.LENGTH_SHORT,true).show();
                         }
                        }
                    });

                }

            }
        });

        attachfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent=new Intent(chatting.this,attachfile.class);
             startActivity(intent);
            }
        });


    }
}
