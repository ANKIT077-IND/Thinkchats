package com.codewithankit.thinkchats;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

public class ConnectionReceiver extends BroadcastReceiver {
    Context mcontext;
    @Override
    public void onReceive(Context context, Intent intent) {
        mcontext=context;

        if (isConnected(context)){
            Toast.makeText(context, "Internet Connected", Toast.LENGTH_LONG).show();

        }
        else {
            showdialog();
        }

    }
    public boolean isConnected(Context context){
        try {
            ConnectivityManager cm=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info=cm.getActiveNetworkInfo();
            return (info !=null && info.isConnected());
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
            return false;
        }

    }

    public void showdialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(mcontext);
        LayoutInflater inflater=(LayoutInflater) mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view=inflater.inflate(R.layout.activity_internetcheck,null);
        Button connect=view.findViewById(R.id.connect);
        builder.setView(view);

        final Dialog dialog=builder.create();
        dialog.show();

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });



    }

}
