package com.example.lab4.client;

import android.database.sqlite.SQLiteDatabase;

import com.example.lab4.db.User;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {

    private User user;
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private boolean isClosed = false;

    public Client( User user){
        this.user = user;
    }

    public void connect(String serverName, int port){
        try{
            socket = new Socket(serverName, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            System.out.println("Пользователь " + user.getName() + " подключился к серверу по имени" + socket.getRemoteSocketAddress());
            out = new PrintWriter(socket.getOutputStream());
            Thread thread = new Thread(this::listenerToMessage);
            thread.start();
        }catch(IOException ex){
            ex.printStackTrace();
        }

    }
    public void listenerToMessage(){
        try{
            while(!isClosed){
                String server_message = in.readLine();
                System.out.println(server_message);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public void send(String message){
        System.out.println("Пользователь " + user.getName());
        out.println(message);
        out.flush();
    }

    public void disconnect(){
        try {
            socket.close();
            isClosed = true;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

}
