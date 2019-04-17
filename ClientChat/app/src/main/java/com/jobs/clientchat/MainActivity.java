package com.jobs.clientchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.ip_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if (Memory.socket != null)
                                if (Memory.socket.isConnected()) Memory.socket.close();
                            Memory.socket = new Socket(((EditText) findViewById(R.id.ip_txt)).getText().toString(), Memory.port);
                            Memory.mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(Memory.socket.getOutputStream())), true);
                            Memory.mBufferIn = new BufferedReader(new InputStreamReader(Memory.socket.getInputStream()));
                            Memory.SendMessage(android.os.Build.MODEL);
                            findViewById(R.id.messages).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) findViewById(R.id.messages)).setText("ВЫ: connected" + "\n" + ((TextView) findViewById(R.id.messages)).getText().toString());
                                }
                            });

                        } catch (Exception ex) {
                            Log.e("Connect", ex.getMessage());
                        }
                        while (Memory.socket.isConnected()) {
                            final String s = Memory.readString();
                            findViewById(R.id.messages).post(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) findViewById(R.id.messages)).setText(s + "\n" + ((TextView) findViewById(R.id.messages)).getText().toString());
                                }
                            });
                        }

                    }
                }).start();
            }
        });

        findViewById(R.id.send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Memory.SendMessage(((EditText)findViewById(R.id.txt_send)).getText().toString());
                ((TextView) findViewById(R.id.messages)).setText("ВЫ: " + ((EditText)findViewById(R.id.txt_send)).getText().toString() + "\n" + ((TextView) findViewById(R.id.messages)).getText().toString());
                ((EditText)findViewById(R.id.txt_send)).setText("");
            }
        });
    }
}
