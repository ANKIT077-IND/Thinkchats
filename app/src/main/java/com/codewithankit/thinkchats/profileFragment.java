package com.codewithankit.thinkchats;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ScheduledExecutorService;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class profileFragment extends Fragment {
    CircleImageView profile;
    ImageButton camera;
    TextView name,email,mobile;
    Uri filepath;
    public profileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);

        profile=view.findViewById(R.id.profile_image);
        camera=view.findViewById(R.id.userprofile);
        name=view.findViewById(R.id.profilename);
        email=view.findViewById(R.id.profileemail);
        mobile=view.findViewById(R.id.profilemno);


        FirebaseDatabase db=FirebaseDatabase.getInstance();
        FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        String uid=user.getUid();
        DatabaseReference reference=db.getReference().child("Users").child(uid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
            String uimage=snapshot.child("image").getValue(String.class);
            String uname=snapshot.child("name").getValue(String.class);
            String uemail=snapshot.child("email").getValue(String.class);
            String umobile=snapshot.child("mobile").getValue(String.class);

                Glide.with(getContext()).load(uimage).placeholder(R.drawable.profile).into(profile);
              name.setText(uname);
              email.setText(uemail);
              mobile.setText(umobile);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            Toasty.error(getContext(),"database error",Toasty.LENGTH_LONG,true).show();
            }
        });

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withContext(getContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        Intent intent=new Intent(Intent.ACTION_PICK);
                        intent.setType("image/*");
                        startActivityForResult(Intent.createChooser(intent,"Please select image"),101);
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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==101 && resultCode == RESULT_OK){
            filepath=data.getData();
            CropImage.activity(filepath)
                    .start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                ProgressDialog dialog=new ProgressDialog(getContext());
                dialog.setTitle("Profile uploading");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Uri resultUri = result.getUri();

                FirebaseStorage storage=FirebaseStorage.getInstance();
                StorageReference reference=storage.getReference("Profile_image").child("image"+System.currentTimeMillis());
                reference.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                                String uid=user.getUid();
                                DatabaseReference userreference=database.getReference().child("Users");

                               HashMap<String,Object>usermap=new HashMap<>();
                               usermap.put("image",uri.toString());

                               userreference.child(uid).addValueEventListener(new ValueEventListener() {
                                   @Override
                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()){
                                        userreference.child(uid).updateChildren(Collections.unmodifiableMap(usermap));

                                    }
                                    else{
                                        userreference.child(uid).setValue(usermap);
                                    }
                                    Toasty.success(getContext(),"Profile uploading successfully",Toasty.LENGTH_LONG,true).show();
                                     dialog.dismiss();
                                   }

                                   @Override
                                   public void onCancelled(@NonNull DatabaseError error) {

                                   }
                               });

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                           Toasty.error(getContext(),"Profile uploading failed",Toasty.LENGTH_LONG,true).show();
                           dialog.hide();
                            }
                        });
                    }
                }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                     float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                     dialog.setMessage("Uploading:"+(int)percent+"%");
                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
