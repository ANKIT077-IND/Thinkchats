package com.codewithankit.thinkchats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import es.dmoral.toasty.Toasty;

public class account extends AppCompatActivity {
    Toolbar accounttoolbar;
    EditText mail,passwd,name,mno;
    Button create;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        accounttoolbar=findViewById(R.id.account_toolbar);
        setSupportActionBar(accounttoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Account");

        mAuth=FirebaseAuth.getInstance();
        mail=findViewById(R.id.mail);
        passwd=findViewById(R.id.password);
        name=findViewById(R.id.name);
        mno=findViewById(R.id.mno);
        create=findViewById(R.id.create);

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog=new ProgressDialog(account.this);
                dialog.setMessage("Please wait creating a account");
                dialog.setTitle("Creating Account");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();

                String email = mail.getText().toString();
                String password = passwd.getText().toString();
                String uname = name.getText().toString();
                String mobile = mno.getText().toString();


                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                DatabaseReference databaseReference=firebaseDatabase.getReference().child("Users");
                databaseReference.orderByChild("mobile").equalTo(mobile).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.getValue()!=null){
                            Toasty.error(getApplicationContext(),"this number is already register",Toast.LENGTH_SHORT).show();
                        }
                        else if (email.isEmpty()) {
                            Toasty.error(getApplicationContext(), "Please enter the email", Toasty.LENGTH_LONG, true).show();
                        }
                        else if (email.indexOf('@') <= 0) {
                            Toasty.error(getApplicationContext(), "@ invalid  position", Toasty.LENGTH_LONG, true).show();
                        }
                        else if (email.charAt(email.length() - 4) != '.' && email.charAt(email.length() - 3) != '.') {
                            Toasty.error(getApplicationContext(), ". invalid position", Toasty.LENGTH_LONG, true).show();
                        }
                        else if (password.isEmpty()) {
                            Toasty.error(getApplicationContext(), "Please fill Password", Toasty.LENGTH_LONG, true).show();
                        }
                        else if (password.length() <= 5 || password.length() >= 15) {
                            Toasty.error(getApplicationContext(), "Password length min 5 and max 20 character", Toasty.LENGTH_LONG, true).show();
                        }

                        else if (uname.isEmpty()) {
                            Toasty.error(getApplicationContext(), "Please fill the name", Toasty.LENGTH_LONG, true).show();
                        }
                        else if (uname.length() <= 5 || uname.length() >= 20) {
                            Toasty.error(getApplicationContext(), "Name length 5 to 20 character", Toasty.LENGTH_LONG, true).show();
                        }
                        else if (mobile.isEmpty()) {
                            Toasty.error(getApplicationContext(), "Please enter the mobile number", Toasty.LENGTH_LONG, true).show();
                        }
                        else if (mobile.length() != 10) {
                            Toasty.error(getApplicationContext(), "Please enter 10 digit mobile number", Toasty.LENGTH_LONG, true).show();
                        }
                        else{

                            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        FirebaseDatabase db = FirebaseDatabase.getInstance();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        String uid = user.getUid();
                                        DatabaseReference myref = db.getReference().child("Users").child(uid);

                                        HashMap<String, Object> usermap = new HashMap<>();
                                        usermap.put("email", email);
                                        usermap.put("name", uname);
                                        usermap.put("mobile", mobile);
                                        usermap.put("image", "default");

                                        myref.setValue(usermap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    dialog.dismiss();
                                                    mail.setText("");
                                                    passwd.setText("");
                                                    name.setText("");
                                                    mno.setText("");

                                                }
                                                Toasty.success(getApplicationContext(),"Account creating Successfully",Toasty.LENGTH_LONG,true).show();

                                            }
                                        });
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.hide();
                                    Toasty.error(getApplicationContext(), "Account creating failed", Toasty.LENGTH_LONG, true).show();
                                }
                            });
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}
