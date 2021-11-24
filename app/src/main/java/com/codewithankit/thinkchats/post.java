package com.codewithankit.thinkchats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

public class post extends AppCompatActivity {
    Toolbar posttoolbar;
    CircleImageView profile;
    TextView username,photos;
    EditText status;
    ImageView img,image;
    Button Post;
   Uri filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        posttoolbar=findViewById(R.id.post_toolbar);
        setSupportActionBar(posttoolbar);
        getSupportActionBar().setTitle("Post");

        profile=findViewById(R.id.profile_image);
        username=findViewById(R.id.username);
        photos=findViewById(R.id.photos);
        status=findViewById(R.id.status);
        img=findViewById(R.id.postimage);
        Post=findViewById(R.id.Post);
        image=findViewById(R.id.image);

        FirebaseDatabase db=FirebaseDatabase.getInstance();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        DatabaseReference myref=db.getReference().child("Users").child(uid);
        myref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
              String uprofile=snapshot.child("image").getValue(String.class);
              String name=snapshot.child("name").getValue(String.class);
              username.setText(name);
                Glide.with(getApplicationContext()).load(uprofile).placeholder(R.drawable.profile).into(profile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toasty.error(getApplicationContext(),"data fatching failed",Toasty.LENGTH_LONG,true).show();
            }
        });

        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Please select image"),102);

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                    permissionToken.continuePermissionRequest();
                    }
                }).check();

            }
        });

        Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            FirebaseDatabase postdb=FirebaseDatabase.getInstance();
            FirebaseUser postuser=FirebaseAuth.getInstance().getCurrentUser();
            String uid=postuser.getUid();
            DatabaseReference postreference=postdb.getReference().child("Users");
            postreference.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                 if (snapshot.exists()){
                    String name=snapshot.child("name").getValue(String.class);
                    String image=snapshot.child("image").getValue(String.class);
                    postphoto(name,image);

                 }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });




            }
        });

    }

    private void postphoto(String name, String image){
        ProgressDialog dialog=new ProgressDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setTitle("post uploading");
        dialog.show();


        String Status=status.getText().toString();
        String photo=photos.getText().toString();
        if (Status.isEmpty()){
            Toasty.error(getApplicationContext(),"Please enter the status",Toasty.LENGTH_LONG,true).show();
        }
        else if(photo.isEmpty()){
            Toasty.error(getApplicationContext(),"Please select the image",Toasty.LENGTH_LONG,true).show();
        }
        else {

            FirebaseStorage poststorage=FirebaseStorage.getInstance();
            StorageReference postreference=poststorage.getReference("Post_image").child("image"+System.currentTimeMillis());
            postreference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   postreference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                       @Override
                       public void onSuccess(Uri uri) {
                         FirebaseDatabase postdb=FirebaseDatabase.getInstance();
                         FirebaseUser post=FirebaseAuth.getInstance().getCurrentUser();
                         String uid=post.getUid();
                         DatabaseReference reference=postdb.getReference().child("Postimage").child(uid);

                           Calendar calendar=Calendar.getInstance();
                           SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
                           String time = dateformat.format(calendar.getTime());

                           HashMap<String,Object>usermap=new HashMap<>();
                           usermap.put("username",name);
                           usermap.put("profile",image);
                           usermap.put("Status",Status);
                           usermap.put("image",uri.toString());
                           usermap.put("time",time);
                           reference.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Toasty.success(getApplicationContext(),"Post uploaded Successfully",Toasty.LENGTH_LONG,true).show();
                                    status.setText("");
                                    dialog.dismiss();
                                }


                               }
                           });

                       }
                   });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    dialog.hide();
                 Toasty.error(getApplicationContext(),"post failed",Toasty.LENGTH_LONG,true).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                  float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                   dialog.setMessage("Uploading:"+(int)percent+"%");
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==102 && resultCode==RESULT_OK){
            filepath=data.getData();

            try {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                Bitmap bitmap= BitmapFactory.decodeStream(inputStream);
                img.setImageBitmap(bitmap);
                img.setVisibility(View.VISIBLE);
                photos.setVisibility(View.INVISIBLE);
                image.setVisibility(View.INVISIBLE);
            }catch (Exception e){
                Toasty.error(getApplicationContext(),e.getMessage(),Toasty.LENGTH_LONG,true).show();

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
