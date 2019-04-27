package com.jobs.clientchat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class AccordFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_accord, container, false);

        view.findViewById(R.id.ip_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (Memory.socket != null)
                                if (Memory.socket.isConnected()) Memory.socket.close();
                            Memory.socket = new Socket(((EditText) view.findViewById(R.id.ip_txt)).getText().toString(), Memory.port);
                            Memory.mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(Memory.socket.getOutputStream())), true);
                            Memory.mBufferIn = new BufferedReader(new InputStreamReader(Memory.socket.getInputStream()));
                            Memory.SendMessage(android.os.Build.MODEL);
                            view.findViewById(R.id.messages).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) view.findViewById(R.id.messages)).setText("ВЫ: connected" + "\n" + ((TextView) view.findViewById(R.id.messages)).getText().toString());
                                }
                            });

                        } catch (Exception ex) {
                            Log.e("Connect", ex.getMessage());
                        }
                        while (Memory.socket.isConnected()) {
                            final String s = Memory.readString();
                            view.findViewById(R.id.messages).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) view.findViewById(R.id.messages)).setText(s + "\n" + ((TextView) view.findViewById(R.id.messages)).getText().toString());
                                }
                            });
                        }

                    }
                }).start();
            }
        });

        view.findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Memory.SendMessage(((EditText)view.findViewById(R.id.txt_send)).getText().toString());
                ((TextView)view.findViewById(R.id.messages)).setText("ВЫ: " + ((EditText)view.findViewById(R.id.txt_send)).getText().toString() + "\n" + ((TextView) view.findViewById(R.id.messages)).getText().toString());
                ((EditText)view.findViewById(R.id.txt_send)).setText("");
            }
        });
        return view;
    }
}