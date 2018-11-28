package server;

import request.Request;
import serializer.Serializer;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class responsible for sending messages back to the user
 */
public class Callback {
    private static Serializer serializer = new Serializer();

    /**
     * Using {@link #sendMessage(Socket, String)}, sends a message back to the user. If send fails for whatever reason,
     * the operation retries up to a max of 5 times.
     * @param client the client's socket.
     * @param message the message that is to be sent.
     * @return true/false
     */
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

    /**
     * Sends a message back to the user over TCP
     * @param client the client's socket.
     * @param message the message that is to be sent.
     * @return true/false
     */
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
