package com.codewithankit.thinkchats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.dmoral.toasty.Toasty;

public class home_Activity extends AppCompatActivity {
    BottomNavigationView navigationView;
    FrameLayout frameLayout;
    boolean ispressed=false;
    private FirebaseAuth mAuth;
    Toolbar hometoolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_);
         hometoolbar=findViewById(R.id.home_toolbar);
         setSupportActionBar(hometoolbar);
        getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,new homeFragment()).commit();

        mAuth=FirebaseAuth.getInstance();
        frameLayout=findViewById(R.id.framlayout);
        navigationView=findViewById(R.id.bottomnavigation);
        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment temp=null;
                switch (menuItem.getItemId()){
                    case R.id.home:temp=new homeFragment();
                    break;
                    case R.id.chat:temp=new chatFragment();
                     break;
                    case R.id.profile:temp=new profileFragment();

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.framlayout,temp).commit();
                return true;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.logout:FirebaseAuth.getInstance().signOut();
                Logout();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void Logout(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("are you sure want to Logout");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(home_Activity.this, "Logout Successfully", Toast.LENGTH_LONG).show();
               Intent intent=new Intent(home_Activity.this,MainActivity.class);
               startActivity(intent);
               finish();

            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
             dialog.cancel();
            }
        });

         AlertDialog dialog=builder.create();
         dialog.setTitle("Logout");
         dialog.show();
    }

    @Override
    public boolean isActivityTransitionRunning() {
        return super.isActivityTransitionRunning();
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        if (ispressed){
            finishAffinity();
            System.exit(0);

        }
        else{
        Toast.makeText(getApplicationContext(),"Please click back again to exit",Toast.LENGTH_LONG).show();
        ispressed=true;
        }
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
             ispressed=false;
            }
        };
        new Handler().postDelayed(runnable,2000);

    }
}
