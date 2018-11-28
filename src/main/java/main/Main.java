package main;

import server.Server;

public class Main {

    public static void main(String[] args){
        try{
            Class.forName("com.mysql.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex){
            ex.printStackTrace();
        }
        new Thread(new Server()).start();
    }
}
