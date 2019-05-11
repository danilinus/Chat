package com.jobs.clientchat;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

final class Memory {

    static MainViewPager mainViewPager;
    static MessagesViewPager messagesViewPager;

    static Socket socket;
    static PrintWriter mBufferOut;
    static BufferedReader mBufferIn;

    static GradientDrawable CirclePoint;

    static boolean connected = false;

    static String host = "192.168.1.7";
    static int port = 8888;

    static boolean TryConnected() {
        try {
            if (socket != null)
                if (socket.isConnected()) socket.close();
            socket = new Socket(host, port);
            mBufferOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
            mBufferIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            SendMessage(android.os.Build.MODEL);
            connected = true;
        } catch (Exception ex) {
            connected = false;
        }
        return connected;
    }

    static void SendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBufferOut.write(message);
                mBufferOut.flush();
            }
        }).start();
    }

    static String readString() {
        int anzahlZeichen = 0;
        String nachricht = "";
        try {
            char[] buffer = new char[128];
            anzahlZeichen = mBufferIn.read(buffer, 0, buffer.length);
            nachricht = new String(buffer, 0, anzahlZeichen);
        } catch (IOException e) {
            Log.e("ReadStringError", e.getMessage());
        }
        return nachricht;
    }
}