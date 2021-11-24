package com.codewithankit.thinkchats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity {
    BroadcastReceiver broadcastReceiver;
    private FirebaseAuth mAuth;
    EditText mail,pass;
    TextView forgotpassword;
    Button login,createnewaccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth=FirebaseAuth.getInstance();
        broadcastReceiver=new ConnectionReceiver();
        Restorenetwork();
        mail=findViewById(R.id.email);
        pass=findViewById(R.id.password);
        forgotpassword=findViewById(R.id.forgotpassword);
        login=findViewById(R.id.login);
        createnewaccount=findViewById(R.id.createnewaccount);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog dialog=new ProgressDialog(MainActivity.this);
                dialog.setCanceledOnTouchOutside(false);
                dialog.setTitle("Login");
                dialog.setMessage("Login Please wait");
                dialog.show();



                String gmail = mail.getText().toString();
                String password = pass.getText().toString();

                if (gmail.isEmpty()) {
                    Toasty.error(getApplicationContext(), "Please enter the email", Toasty.LENGTH_LONG, true).show();
                } else if (gmail.indexOf('@') <= 0) {
                    Toasty.error(getApplicationContext(), "@ invalid  position", Toasty.LENGTH_LONG, true).show();
                } else if (gmail.charAt(gmail.length() - 4) != '.' && gmail.charAt(gmail.length() - 3) != '.') {
                    Toasty.error(getApplicationContext(), ". invalid position", Toasty.LENGTH_LONG, true).show();
                } else if (password.isEmpty()) {
                    Toasty.error(getApplicationContext(), "Please fill Password", Toasty.LENGTH_LONG, true).show();
                } else if (password.length() <= 5 || password.length() >= 15) {
                    Toasty.error(getApplicationContext(), "Password length min 5 and max 20 character", Toasty.LENGTH_LONG, true).show();
                } else {

                    mAuth.signInWithEmailAndPassword(gmail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                Toasty.success(getApplicationContext(), "Login Successfully", Toasty.LENGTH_LONG, true).show();
                                mail.setText("");
                                pass.setText("");
                                Intent intent=new Intent(MainActivity.this,home_Activity.class);
                                startActivity(intent);
                                finish();
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.hide();
                            Toasty.error(getApplicationContext(), "Login failed try again", Toasty.LENGTH_LONG, true).show();
                        }
                    });

                }
            }
        });


        createnewaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,account.class);
                startActivity(intent);

            }
        });

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,forgotpassword.class);
                startActivity(intent);
            }
        });

    }
    protected void Restorenetwork(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            registerReceiver(broadcastReceiver,new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        }
    }
    protected void unRegisterNetwork(){
        try {
            unregisterReceiver(broadcastReceiver);
        }
        catch(IllegalArgumentException e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterNetwork();
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user=mAuth.getCurrentUser();
        if(user!=null){
            Intent intent=new Intent(MainActivity.this,home_Activity.class);
            startActivity(intent);

        }
    }



}
