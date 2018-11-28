package server;

import request.Request;
import serializer.Serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Callback {
    private static Serializer serializer = new Serializer();
    public static boolean callback(Socket client, String message){
        int retry = 5;

        while (retry>0){
            //Send message
            if (!sendMessage(client, message)){
                --retry;
            }
            else return true;
        }

        return false;
    }

    private static boolean sendMessage(Socket client, String message){
        try {
            DataOutputStream out = new DataOutputStream(client.getOutputStream());
            if (!serializer.writeMessage(out, message.getBytes(), message.length())) {
                return false;
            }
            return true;
        } catch (IOException e) {
            if (Request.DEV_MODE) e.printStackTrace();
        }
        return false;
    }
}
