package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{
    private int PORT = 16667;
    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public Server(){
        System.out.println("server.Server is running.");
    }

    @Override
    public void run() {
        try{
            ServerSocket serverSocket = new ServerSocket(PORT);

            //Wait for clients to connect
            while (true){
                Socket client = serverSocket.accept();
                executor.submit(new Process(client));
            }
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
