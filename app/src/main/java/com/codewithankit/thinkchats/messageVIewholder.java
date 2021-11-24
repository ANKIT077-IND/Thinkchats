package com.codewithankit.thinkchats;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class messageVIewholder extends RecyclerView.ViewHolder {
    TextView messagechat,date,time;
    public messageVIewholder(@NonNull View itemView) {
        super(itemView);

        messagechat=itemView.findViewById(R.id.messagechat);
        date=itemView.findViewById(R.id.messagedate);
        time=itemView.findViewById(R.id.messagetime);
    }
}
