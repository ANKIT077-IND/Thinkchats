package com.codewithankit.thinkchats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.dmoral.toasty.Toasty;

public class forgotpassword extends AppCompatActivity {
    Toolbar forgottoolbar;
    EditText forgotmail;
    Button forgotpassword;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);
        forgottoolbar=findViewById(R.id.forgot_toolbar);
        setSupportActionBar(forgottoolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("forgot password");

        mAuth=FirebaseAuth.getInstance();
        forgotmail=findViewById(R.id.forgotemail);
        forgotpassword=findViewById(R.id.forgotpassword);

        forgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String forgotemail=forgotmail.getText().toString();

                if(forgotemail.isEmpty()) {
                    Toasty.error(getApplicationContext(), "Please enter the email", Toasty.LENGTH_LONG, true).show();
                } else if (forgotemail.indexOf('@') <= 0) {
                    Toasty.error(getApplicationContext(), "@ invalid  position", Toasty.LENGTH_LONG, true).show();
                } else if (forgotemail.charAt(forgotemail.length() - 4) != '.' && forgotemail.charAt(forgotemail.length() - 3) != '.') {
                    Toasty.error(getApplicationContext(), ". invalid position", Toasty.LENGTH_LONG, true).show();
                }
                else{
                    mAuth.sendPasswordResetEmail(forgotemail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                       if (task.isSuccessful()){
                           Toasty.success(getApplicationContext(),"Reset password links send to mail",Toasty.LENGTH_LONG,true).show();
                           forgotmail.setText("");
                       }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toasty.error(getApplicationContext(),"Password reset failed try again",Toasty.LENGTH_LONG,true).show();
                        }
                    });

                }

            }
        });


    }
}
