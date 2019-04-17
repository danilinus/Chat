package com.jobs.clientchat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public final class Memory {

    public static Socket socket;
    public static PrintWriter mBufferOut;
    public static BufferedReader mBufferIn;

    public static int port = 8888;

    public static void SendMessage(final String message) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mBufferOut.write(message);
                mBufferOut.flush();
            }
        }).start();
    }

    final public static String readString() {
        int anzahlZeichen = 0;
        String nachricht = "";
        try {
            char[] buffer = new char[128];
            anzahlZeichen = mBufferIn.read(buffer, 0, buffer.length);
            nachricht = new String(buffer, 0, anzahlZeichen);
        } catch (IOException e) {
        }
        return nachricht;
    }
}
