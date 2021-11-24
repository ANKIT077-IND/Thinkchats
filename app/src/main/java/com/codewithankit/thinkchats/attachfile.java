package com.codewithankit.thinkchats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class attachfile extends AppCompatActivity {
    RecyclerView attachrecycler;
    ArrayList<attachfilemodel> item1;
    Uri documenturi;
    ArrayList<String> files, status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachfile);
        files = new ArrayList<>();
        status = new ArrayList<>();
        attachrecycler = findViewById(R.id.attachrecycler);
        attachrecycler.setLayoutManager(new LinearLayoutManager(this));
        item1 = new ArrayList<attachfilemodel>();
        item1.add(new attachfilemodel("Document", R.drawable.file));
        item1.add(new attachfilemodel("Camera", R.drawable.camera));
        item1.add(new attachfilemodel("Gallery", R.drawable.attachimg));
        item1.add(new attachfilemodel("Contact", R.drawable.contact));
        item1.add(new attachfilemodel("Location", R.drawable.location));


        GridLayoutManager manager = new GridLayoutManager(this, 3);
        attachrecycler.setLayoutManager(manager);

        RecyclerView.Adapter<attachfileviewholder> adapter1 = new RecyclerView.Adapter<attachfileviewholder>() {
            @NonNull
            @Override
            public attachfileviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attachfilerow, parent, false);
                return new attachfileviewholder(view);
            }

            @Override
            public void onBindViewHolder(@NonNull attachfileviewholder holder, int position) {
                holder.filename.setText(item1.get(position).getFilename());
                holder.image.setImageResource(item1.get(position).getImage());
                holder.image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (position) {
                            case 0:
                                Dexter.withContext(getApplicationContext()).withPermission(Manifest.permission.READ_EXTERNAL_STORAGE).withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                                        String[] mimeTypes =
                                                {"application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", // .doc & .docx
                                                        "application/vnd.ms-powerpoint", "application/vnd.openxmlformats-officedocument.presentationml.presentation", // .ppt & .pptx
                                                        "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", // .xls & .xlsx
                                                        "text/plain",
                                                        "application/pdf",
                                                        "application/zip"};

                                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                            intent.setType(mimeTypes.length == 1 ? mimeTypes[0] : "*/*");
                                            if (mimeTypes.length > 0) {
                                                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
                                            }
                                        } else {
                                            String mimeTypesStr = "";
                                            for (String mimeType : mimeTypes) {
                                                mimeTypesStr += mimeType + "|";
                                            }
                                            intent.setType(mimeTypesStr.substring(0, mimeTypesStr.length() - 1));
                                        }
                                        startActivityForResult(Intent.createChooser(intent, "Please select document"), 101);
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                                        permissionToken.continuePermissionRequest();
                                    }
                                }).check();


                                break;
                            case 1:
                                Toast.makeText(attachfile.this, "this feature coming soon", Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                Toast.makeText(attachfile.this, "this feature coming soon", Toast.LENGTH_LONG).show();
                                break;
                            case 3:
                                Toast.makeText(attachfile.this, "this feature coming soon", Toast.LENGTH_LONG).show();
                                break;
                            case 4:
                                Toast.makeText(attachfile.this, "this feature coming soon", Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });

            }

            @Override
            public int getItemCount() {
                return item1.size();
            }
        };
        attachrecycler.setAdapter(adapter1);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 101 && resultCode == RESULT_OK) {
          ProgressDialog dialog=new ProgressDialog(this);
          dialog.setTitle("Document uploaded");
          dialog.show();

           if (data.getClipData()!=null){
               for (int i=0;i<data.getClipData().getItemCount();i++){
                   documenturi=data.getClipData().getItemAt(i).getUri();
                   String filename=getfilename(documenturi);
                   files.add(filename);
                   status.add("loading");


                   final int index=i;
                   FirebaseStorage storage=FirebaseStorage.getInstance();
                   StorageReference reference=storage.getReference().child("post_document").child(filename);
                   reference.putFile(documenturi).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                         reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                             @Override
                             public void onSuccess(Uri uri) {
                              status.remove(index);
                              status.add(index,"done");



                               FirebaseDatabase database=FirebaseDatabase.getInstance();
                               FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
                               String uid=user.getUid();
                               DatabaseReference databaseReference=database.getReference().child("document").child("document").child(uid);

                                 Calendar calendar=Calendar.getInstance();
                                 SimpleDateFormat dateFormat=new SimpleDateFormat("dd-MM-YYYY");
                                 String date=dateFormat.format(calendar.getTime());
                                 SimpleDateFormat dateformat = new SimpleDateFormat("HH:mm");
                                 String time = dateformat.format(calendar.getTime());

                                 HashMap<String,Object>documentmap=new HashMap<>();
                                 documentmap.put("document",uri);
                                 documentmap.put("time",time);
                                 documentmap.put("date",date);
                                 documentmap.put("type","document");
                                 documentmap.put("posttime",ServerValue.TIMESTAMP);

                                 databaseReference.setValue(documentmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                     @Override
                                     public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                        dialog.dismiss();
                                         Toast.makeText(attachfile.this, "document uploaded successfully", Toast.LENGTH_SHORT).show();
                                     }
                                 });
                             }
                         }) ;
                       }
                   }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                       @Override
                       public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                           float percent=(100*snapshot.getBytesTransferred())/snapshot.getTotalByteCount();
                           dialog.setMessage("Uploaded:-"+(int)percent+"%");
                       }
                   }).addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                       Toasty.error(getApplicationContext(),"document uploaded failed",Toasty.LENGTH_SHORT,true).show();
                       }
                   });
               }

           }



        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getfilename(Uri documenturi) {
        String result = null;
        if (documenturi.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(documenturi, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = documenturi.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
