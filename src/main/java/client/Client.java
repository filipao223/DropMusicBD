package client;

import serializer.Serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.*;

public class Client {
    private int PORT = 16667;
    private String address;
    private Serializer serializer = new Serializer();
    private String username = "";
    private boolean loggedIn = false;
    private String NO_LOGIN = "Not logged in";
    private String ALREADY_LOGIN = "Already logged in";

    Client(){
        startClient();
    }

    public static void main(String[] args){
        new Client();
    }


    private String message(){

        String readKeyboard;
        Scanner keyboardScanner = new Scanner(System.in);
        //String user_name = "";
        String send = "";

            System.out.println("What do you want to do(insert the number):\n1.Login\n2.Logout\n3.Register\n4.Edit, add, or remove albuns,artist or musics\n5.Search for musics, artists or albuns\n6.Write a critique to a album\n7.Give editing privileges\n8.Upload a music\n9.Share musics\n10.Download a music\n13.Create a playlist\n14.Remove a playlist\n15.Turn a playlist public/private\n16.Add music to a playlist");
            readKeyboard = keyboardScanner.nextLine();
            if (readKeyboard.matches(("1"))) {
                if (!loggedIn){
                    String username, password;
                    send = "1_";
                    System.out.println("Insert your username:");
                    username = keyboardScanner.nextLine();
                    this.username = username;
                    send = send.concat(username).concat("_");
                    System.out.println("Insert your password:");
                    password = keyboardScanner.next();
                    send = send.concat(password);
                    System.out.println(send);
                }
                else{
                    System.out.println(ALREADY_LOGIN);
                    return null;
                }
            }
            else if (readKeyboard.matches("2")) {
                if (loggedIn){
                    send = "2_";
                    send = send.concat(this.username);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if (readKeyboard.matches("3")) {
                if (!loggedIn){
                    String username, password,first,last;
                    send = "3_";
                    System.out.println("Insert your username:");
                    username = keyboardScanner.nextLine();
                    send = send.concat(username).concat("_");
                    System.out.println("Insert your password:");
                    password = keyboardScanner.nextLine();
                    send = send.concat(password).concat("_");
                    System.out.println("First Name:");
                    first = keyboardScanner.nextLine();
                    send = send.concat(first).concat("_");
                    System.out.println("Last Name:");
                    last = keyboardScanner.nextLine();
                    send = send.concat(last);
                }
                else{
                    System.out.println("You are logged in, please log out to create new user.");
                    return null;
                }
            }
            else if (readKeyboard.matches("4")){
                if (loggedIn){
                    String acao,tipo,name,caracteristica,edit,value,add,sql = "",artist;
                    send ="4_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("Do you want to alter, remove, or add?");
                    acao = keyboardScanner.nextLine();
                    //send = send.concat(acao).concat("_");
                    if(acao.matches("add")){
                        System.out.println("Artist,album or music?");
                        tipo = keyboardScanner.nextLine();
                        System.out.println("What is the name?");
                        name = keyboardScanner.nextLine();
                        if(tipo.matches("artist")){
                            sql = "INSERT INTO artist (name) VALUES (\"" + name + "\");";
                        }
                        else if(tipo.matches("album")){
                            System.out.println("What is the name of the artist that the album belongs to?");
                            artist = keyboardScanner.nextLine();
                            sql = "INSERT INTO album (name,artist_nartist) VALUES (\"" + name + "\",(SELECT nartist FROM artist WHERE a_name =\"" + name + "\"));";
                        }
                        else if(tipo.matches("music")){
                            System.out.println("What is the name of the artist that the album belongs to?");
                            artist = keyboardScanner.nextLine();
                            sql = "INSERT INTO album (name,artist_nartist,users_user_id) VALUES (\"" + name + "\",(SELECT nartist FROM artist WHERE a_name =\"" + name + "\"),(SELECT user_id FROM users WHERE username = \"" + this.username +"\");";
                        }
                        send = send.concat(sql);
                        System.out.println(send);
                    }
                    else if(acao.matches("alter")){
                        System.out.println("Artist,album or music?");
                        tipo = keyboardScanner.nextLine();
                        if(tipo.matches("music")){
                            System.out.println("What do you want to alter(name,year,lyrics)?");
                            caracteristica = keyboardScanner.nextLine();
                            if(caracteristica.matches("year")){
                                System.out.println("Which item is to be edited(name)?");
                                edit = keyboardScanner.nextLine();
                                System.out.println("New value?:");
                                value = keyboardScanner.nextLine();
                                if (!value.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])-(1[012]|0?[1-9])-((?:19|20)\\d{2})\\s*$")) {
                                    System.out.println("Bad date format, should be d-m-yyyy");
                                    send = "";
                                }
                                else{
                                    sql = "UPDATE music SET day_of_creation = \"" + value + "\" WHERE m_name = \"" + edit + "\"";
                                }
                            }
                            else if(caracteristica.matches("name")){
                                System.out.println("Which item is to be edited(name)?");
                                edit = keyboardScanner.nextLine();
                                System.out.println("New value?:");
                                value = keyboardScanner.nextLine();
                                sql = "UPDATE music SET m_name = \"" + value + "\" WHERE m_name = \"" + edit + "\"";
                            }
                            else if(caracteristica.matches("lyrics")){
                                System.out.println("Which item is to be edited(name)?");
                                edit = keyboardScanner.nextLine();
                                System.out.println("New value?:");
                                value = keyboardScanner.nextLine();
                                sql = "UPDATE music SET lyrics = \"" + value + "\" WHERE m_name = \"" + edit + "\"";
                            }
                        }
                        else if(tipo.matches("album")){
                            System.out.println("What do you want to alter(name,year,description)?");
                            caracteristica = keyboardScanner.nextLine();
                            if(caracteristica.matches("year")){
                                System.out.println("Which item is to be edited(name)?");
                                edit = keyboardScanner.nextLine();
                                System.out.println("New value?:");
                                value = keyboardScanner.nextLine();
                                if (!value.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])-(1[012]|0?[1-9])-((?:19|20)\\d{2})\\s*$")) {
                                    System.out.println("Bad date format, should be d-m-yyyy");
                                    send = "";
                                }
                                else{
                                    sql = "UPDATE album SET day_of_creation = \"" + value + "\" WHERE album_name = \"" + edit + "\"";
                                }
                            }
                            else if(caracteristica.matches("name")){
                                System.out.println("Which item is to be edited(name)?");
                                edit = keyboardScanner.nextLine();
                                System.out.println("New value?:");
                                value = keyboardScanner.nextLine();
                                sql = "UPDATE album SET album_name = \"" + value + "\" WHERE album_name = \"" + edit + "\"";
                            }
                            else if(caracteristica.matches("description")){
                                System.out.println("Which item is to be edited(name)?");
                                edit = keyboardScanner.nextLine();
                                System.out.println("New value?:");
                                value = keyboardScanner.nextLine();
                                sql = "UPDATE album SET a_description = \"" + value + "\" WHERE album_name = \"" + edit + "\"";
                            }
                        }
                        else if(tipo.matches("artist")){
                            System.out.println("What do you want to alter(name,year,description)?");
                            caracteristica = keyboardScanner.nextLine();
                            if(caracteristica.matches("year")){
                                System.out.println("Which item is to be edited(name)?");
                                edit = keyboardScanner.nextLine();
                                System.out.println("New value?:");
                                value = keyboardScanner.nextLine();
                                if (!value.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])-(1[012]|0?[1-9])-((?:19|20)\\d{2})\\s*$")) {
                                    System.out.println("Bad date format, should be d-m-yyyy");
                                    send = "";
                                }
                                else{
                                    sql = "UPDATE artist SET date_of_birth = \"" + value + "\" WHERE a_name = \"" + edit + "\"";
                                }
                            }
                            else if(caracteristica.matches("name")){
                                System.out.println("Which item is to be edited(name)?");
                                edit = keyboardScanner.nextLine();
                                System.out.println("New value?:");
                                value = keyboardScanner.nextLine();
                                sql = "UPDATE artist SET a_name = \"" + value + "\" WHERE a_name = \"" + edit + "\"";
                            }
                            else if(caracteristica.matches("description")){
                                System.out.println("Which item is to be edited(name)?");
                                edit = keyboardScanner.nextLine();
                                System.out.println("New value?:");
                                value = keyboardScanner.nextLine();
                                sql = "UPDATE artist SET a_description = \"" + value + "\" WHERE a_name = \"" + edit + "\"";
                            }
                        }
                        send = send.concat(sql);
                        System.out.println(send);
                    }
                    else if(acao.matches("remove")){
                        System.out.println("Remove a artist, album, or music?");
                        tipo = keyboardScanner.nextLine();
                        send = send.concat(tipo).concat("_");
                        System.out.println("Name?:");
                        name = keyboardScanner.nextLine();
                        send.concat(name);
                    }
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("5")){
                if (loggedIn){
                    String tipo,name,sql = "";
                    send = "5_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("Artist,album or music?");
                    tipo = keyboardScanner.nextLine();
                    System.out.println("Insert the name of what you want to search");
                    name = keyboardScanner.nextLine();
                    if(tipo.matches("artist")){
                        sql = "Select * FROM artist WHERE a_name =\"" + name + "\";";
                    }
                    else if(tipo.matches("album")){
                        sql = "Select * FROM album WHERE album_name =\"" + name + "\";";
                    }
                    else if(tipo.matches("music")){
                        sql = "Select * FROM music WHERE m_name =\"" + name + "\";";
                    }
                    send = send.concat(sql);
                    System.out.println(send);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("6")){
                if (loggedIn){
                    String name,critique,rating;
                    send = "6_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("What is the critique?");
                    critique = keyboardScanner.nextLine();
                    send = send.concat(critique).concat("_");
                    System.out.println("What is the rating?");
                    rating = keyboardScanner.nextLine();
                    send = send.concat(rating);
                    System.out.println("Insert the name of the album");
                    name = keyboardScanner.nextLine();
                    send.concat(name);
                    System.out.println(send);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("7")){
                if (loggedIn){
                    String target;
                    send = "7_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("Insert the targer username\n");
                    target = keyboardScanner.nextLine();
                    send = send.concat(target);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("8")){
                if (loggedIn){
                    String music;
                    send = "8_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("Music name?: ");
                    music = keyboardScanner.nextLine();
                    send.concat(music).concat("_");
                    InetAddress ip = null;
                    try {
                        ip = InetAddress.getLocalHost();
                    } catch (java.net.UnknownHostException e) {
                        e.printStackTrace();
                    }
                    String address = ip.getHostAddress();
                    send.concat(address);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("9")){
                if (loggedIn){
                    String target;
                    send = "9_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("Insert the target username");
                    target = keyboardScanner.nextLine();
                    send.concat(target);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("10")){
                if (loggedIn){
                    String music;
                    send = "10_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("Music name?: ");
                    music = keyboardScanner.nextLine();
                    send.concat(music).concat("_");
                    InetAddress ip = null;
                    try {
                        ip = InetAddress.getLocalHost();
                    } catch (java.net.UnknownHostException e) {
                        e.printStackTrace();
                    }
                    String address = ip.getHostAddress();
                    send.concat(address);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("13")){
                if (loggedIn){
                    String name;
                    send = "13_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("What is the playlist name");
                    name = keyboardScanner.nextLine();
                    send = send.concat(name);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("14")){
                if (loggedIn){
                    String name;
                    send = "14_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("What is the playlist name");
                    name = keyboardScanner.nextLine();
                    send = send.concat(name);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("15")){
                if (loggedIn){
                    String name;
                    send = "15_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("What is the playlist name");
                    name = keyboardScanner.nextLine();
                    send = send.concat(name);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }else if(readKeyboard.matches("16")){
                if (loggedIn){
                    String playlist_name,music_name;
                    send = "16_";
                    send = send.concat(this.username).concat("_");
                    System.out.println("What is the playlist name");
                    playlist_name = keyboardScanner.nextLine();
                    send = send.concat(playlist_name).concat("_");
                    System.out.println("What is the music name");
                    music_name = keyboardScanner.nextLine();
                    send = send.concat(music_name);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            return send;
    }
    private void startClient() {
        //Request connection to database server
        try{
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Failed getting host address");
            return;
        }

        try{
            Socket server = new Socket("localhost", PORT);
            while(true){
                String message = message();
                if (message == null) continue;
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                if (!serializer.writeMessage(out, message.getBytes(), message.length())) {
                    System.out.println("Error writing message to server");
                }

                // TODO get callback
                // TODO depending on callback, set loggedIn to true

                byte[] buffer;
                DataInputStream in = new DataInputStream(server.getInputStream());
                buffer = serializer.readMessage(in);
                if (buffer != null){
                    String callback = new String(buffer);
                    if (callback.matches("Login")) loggedIn = true;
                    if (callback.matches("Logout")) loggedIn = false;
                    System.out.println(callback);
                }
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
