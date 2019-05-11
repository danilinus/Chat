package com.jobs.clientchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class AccordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_accord, container, false);
        view.findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!((TextView)view.findViewById(R.id.txt_send)).getText().toString().trim().equals(""))
                    Memory.SendMessage(((TextView)view.findViewById(R.id.txt_send)).getText().toString().trim());
                ((TextView)view.findViewById(R.id.txt_send)).setText("");
            }
        });
        return view;
    }
}