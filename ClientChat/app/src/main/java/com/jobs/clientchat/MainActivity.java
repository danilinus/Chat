package com.jobs.clientchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Memory.mainViewPager = findViewById(R.id.main_viewpager);
        Memory.mainViewPager.setScrollDurationFactor(0.5f);
        Memory.mainViewPager.setOffscreenPageLimit(2);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MessagesFragment());
        adapter.addFragment(new AccordFragment());
        Memory.mainViewPager.setAdapter(adapter);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (Memory.socket != null)
                        if (Memory.socket.isConnected()) Memory.socket.close();
                    Memory.socket = new Socket();
                    Memory.socket.setSoTimeout(500);
                    Memory.socket.connect(new InetSocketAddress(Memory.host, Memory.port));
                    Memory.mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(Memory.socket.getOutputStream())), true);
                    Memory.mBufferIn = new BufferedReader(new InputStreamReader(Memory.socket.getInputStream()));
                    Memory.SendMessage(android.os.Build.MODEL);
                    Memory.connected = true;
                } catch (Exception e) {
                    Log.e("Start", e.getMessage());
                }
            }
        }).start();


    }
}