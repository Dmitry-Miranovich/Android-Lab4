package com.example.lab4.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Server{

    private ServerSocket server;
    private boolean isClosed = true;

    public Server(int port) throws IOException {
        server = new ServerSocket(port);
    }

    public void run() {
        while(isClosed){
            try{
                System.out.println("Ожидание подключения");
                Socket client =  server.accept();
                System.out.println("Просто подключается к " + client.getRemoteSocketAddress());
                DataInputStream in = new DataInputStream(client.getInputStream());
                System.out.println(in.readUTF());
                DataOutputStream out = new DataOutputStream(client.getOutputStream());
                out.writeUTF("Спасибо за подключение к " + server.getLocalSocketAddress()
                        + "\nПока!");
                client.close();
            }catch (SocketTimeoutException ex){
                System.out.println("Время подключения истекло");

            }catch (IOException ex2){
                System.out.println(ex2.getMessage());
            }
        }
    }

    public void disconnect(){
        isClosed = false;
    }
}
