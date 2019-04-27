package com.jobs.clientchat;

import android.support.v4.view.ViewPager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

final class Memory {

    static MainViewPager viewPager;

    static Socket socket;
    static PrintWriter mBufferOut;
    static BufferedReader mBufferIn;

    static int port = 8888;

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