package ru.karelia.rovesnik.firerobotcontroll;

import android.os.AsyncTask;

import java.io.*;
import java.net.*;
import java.util.concurrent.TimeUnit;

interface ScreenData {
    void update(Byte a);
}

public class SocketClient {
    private Socket socket;
    private String address;
    private int port;
    private InputStream is;
    private OutputStream os;
    private boolean isRun;
    private ClentLoop loop;
    public ScreenData updater;

    public SocketClient(String address, int port) {
        this.address = address;
        this.port = port;
        loop = new ClentLoop();
    }

    public void sendData(final byte[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    os.write(data);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void stopClient() {
        isRun = false;
    }

    public void run() {
        isRun = true;
        loop.execute(address);
    }

    public boolean isConnected() {
        return socket != null && socket.isConnected();
    }

    public boolean isRunning() {
        return isRun;
    }

    class ClentLoop extends AsyncTask<String, Byte, Void> {
        protected Void doInBackground(String... params) {
            while (isRun) {
                try {
                    socket = new Socket(address, port);
                    is = socket.getInputStream();
                    os = socket.getOutputStream();
                    //sendMessage("Connect");
                    while (isRun){
                        while (is.available() > 0){
                            publishProgress(Byte.valueOf((byte) is.read()));
                        }
                        //TimeUnit.SECONDS.sleep(5);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        protected void onProgressUpdate(Byte... values) {
            super.onProgressUpdate(values);
            updater.update(values[0]);
        }
    }
}