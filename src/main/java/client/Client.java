package client;

import request.Request;
import serializer.Serializer;
import server.Connect;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.*;

public class Client {
    private int PORT = 16667;
    private String address;
    private Serializer serializer = new Serializer();
    private int TIMEOUT = 15000;
    private String username = "";
    private String musicUrl = null;
    private boolean loggedIn = false;
    private boolean uploading = false;
    private boolean downloading = false;
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

            System.out.println("What do you want to do(insert the number):\n1.Login\n2.Logout\n3.Register\n4.Edit or add albuns,artist or musics\n5.Search for musics, artists or albuns\n6.Write a critique to a album\n7.Give editing privileges\n8.Upload a music\n9.Share musics\n10.Download a music\n13.Create a playlist\n14.Remove a playlist\n15.Turn a playlist public/private\n16.Add music to a playlist\n17.Remove a music from a playlist\n18.Add a music to a album\n19.Delete a music from a album\n20.Remove a artist,album,or music");
            readKeyboard = keyboardScanner.nextLine();
            if (readKeyboard.matches(("1"))) {
                if (!loggedIn){
                    String username, password;
                    send = "1|";
                    System.out.println("Insert your username:");
                    username = keyboardScanner.nextLine();
                    this.username = username;
                    send = send.concat(username).concat("|");
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
                    send = "2|";
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
                    send = "3|";
                    System.out.println("Insert your username:");
                    username = keyboardScanner.nextLine();
                    send = send.concat(username).concat("|");
                    System.out.println("Insert your password:");
                    password = keyboardScanner.nextLine();
                    send = send.concat(password).concat("|");
                    System.out.println("First Name:");
                    first = keyboardScanner.nextLine();
                    send = send.concat(first).concat("|");
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
                    send ="4|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("Do you want to alter or add?");
                    acao = keyboardScanner.nextLine();
                    //send = send.concat(acao).concat("_");
                    if(acao.matches("add")){
                        System.out.println("Artist,album or music?");
                        tipo = keyboardScanner.nextLine();
                        System.out.println("What is the name?");
                        name = keyboardScanner.nextLine();
                        if(tipo.matches("artist")){
                            sql = "INSERT INTO artist (a_name) VALUES (\"" + name + "\");";
                        }
                        else if(tipo.matches("album")){
                            System.out.println("What is the name of the artist that the album belongs to?");
                            artist = keyboardScanner.nextLine();
                            sql = "INSERT INTO album (album_name,artist_nartist) VALUES (\"" + name + "\",(SELECT nartist FROM artist WHERE a_name =\"" + artist + "\"));";
                        }
                        else if(tipo.matches("music")){
                            System.out.println("What is the name of the artist that the music belongs to?");
                            artist = keyboardScanner.nextLine();
                            sql = "INSERT INTO music (m_name,artist_nartist,users_user_id) VALUES (\"" + name + "\",(SELECT nartist FROM artist WHERE a_name =\"" + artist + "\"),(SELECT user_id FROM users WHERE username = \"" + this.username +"\"));";
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
                        send = send.concat(tipo).concat("|");
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
                    send = "5|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("Artist,album, music or playlist?");
                    tipo = keyboardScanner.nextLine();
                    System.out.println("Insert the name of what you want to search");
                    name = keyboardScanner.nextLine();
                    if(tipo.matches("artist")){
                        sql = "SELECT * FROM artist WHERE a_name LIKE \'" + name + "%\';";
                    }
                    else if(tipo.matches("album")){
                        sql = "SELECT * FROM album WHERE album_name LIKE \'" + name + "%\';";
                    }
                    else if(tipo.matches("music")){
                        sql = "SELECT * FROM music WHERE m_name LIKE \'" + name + "%\';";
                    }
                    else if (tipo.matches("playlist")){
                        sql = "SELECT * FROM playlist WHERE p_name LIKE \'" + name + "%\';";
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
                    send = "6|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("What is the critique?");
                    critique = keyboardScanner.nextLine();
                    send = send.concat(critique).concat("|");
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
                    send = "7|";
                    send = send.concat(this.username).concat("|");
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
                    send = "8|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("Music name? (include extension):");
                    music = keyboardScanner.next();
                    send = send.concat(music);

                    this.musicUrl = "clientStorage/" + music;
                    this.uploading = true;
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("9")){
                if (loggedIn){
                    String target;
                    send = "9|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("Insert the target username");
                    target = keyboardScanner.nextLine();
                    send = send.concat(target);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("10")){
                if (loggedIn){
                    String music;
                    send = "10|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("Music name?: ");
                    music = keyboardScanner.nextLine();
                    send = send.concat(music);

                    this.downloading = true;
                    this.musicUrl = music + ".mp3";
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("13")){
                if (loggedIn){
                    String name;
                    send = "13|";
                    send = send.concat(this.username).concat("|");
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
                    send = "14|";
                    send = send.concat(this.username).concat("|");
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
                    send = "15|";
                    send = send.concat(this.username).concat("|");
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
                    send = "16|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("What is the playlist name");
                    playlist_name = keyboardScanner.nextLine();
                    send = send.concat(playlist_name).concat("|");
                    System.out.println("What is the music name");
                    music_name = keyboardScanner.nextLine();
                    send = send.concat(music_name);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("17")){
                if (loggedIn){
                    String playlist_name,music_name;
                    send = "17|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("What is the playlist name");
                    playlist_name = keyboardScanner.nextLine();
                    send = send.concat(playlist_name).concat("|");
                    System.out.println("What is the music name");
                    music_name = keyboardScanner.nextLine();
                    send = send.concat(music_name);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("18")){
                if (loggedIn){
                    String album_name,music_name;
                    send = "18|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("What is the album name");
                    album_name = keyboardScanner.nextLine();
                    send = send.concat(album_name).concat("|");
                    System.out.println("What is the music name");
                    music_name = keyboardScanner.nextLine();
                    send = send.concat(music_name);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }
            else if(readKeyboard.matches("19")){
                if (loggedIn){
                    String album_name,music_name;
                    send = "19|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("What is the album name");
                    album_name = keyboardScanner.nextLine();
                    send = send.concat(album_name).concat("|");
                    System.out.println("What is the music name");
                    music_name = keyboardScanner.nextLine();
                    send = send.concat(music_name);
                }
                else{
                    System.out.println(NO_LOGIN);
                    return null;
                }
            }else if(readKeyboard.matches("20")){
                if (loggedIn){
                    String tipe,name;
                    send = "20|";
                    send = send.concat(this.username).concat("|");
                    System.out.println("What is type(artist,album,or music)?");
                    tipe = keyboardScanner.nextLine();
                    send = send.concat(tipe).concat("|");
                    System.out.println("What is the  name");
                    name = keyboardScanner.nextLine();
                    send = send.concat(name);
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
            server.setSoTimeout(TIMEOUT);
            while(true){
                String message = message();
                if (message == null) continue;
                DataOutputStream out = new DataOutputStream(server.getOutputStream());
                if (!serializer.writeMessage(out, message.getBytes(), message.length())) {
                    System.out.println("Error writing message to server.");
                }

                byte[] buffer;
                try{
                    DataInputStream in = new DataInputStream(server.getInputStream());
                    buffer = serializer.readMessage(in);
                } catch (SocketTimeoutException e){
                    System.out.println("A read has timed out");
                    continue;
                }
                if (buffer != null){
                    String callback = new String(buffer);
                    if (callback.matches("Login")) loggedIn = true;
                    if (callback.matches("Logout")) loggedIn = false;
                    if (this.uploading && callback.matches("Upload_ok")){
                        //Upload the music file
                        File file = new File(this.musicUrl);
                        if (file != null){
                            //Convert music file to byte array
                            byte[] musicBuffer = Files.readAllBytes(file.toPath());
                            if (!serializer.writeMessage(out, musicBuffer, musicBuffer.length)){
                                System.out.println("Error writing music file to server.");
                            }
                            uploading = false;
                        }
                        else{
                            System.out.println("File not found");
                        }
                    }
                    if (this.downloading && callback.matches("Download_ok")){
                        try{
                            DataInputStream in = new DataInputStream(server.getInputStream());
                            byte[] musicIn;
                            musicIn = serializer.readMessage(in);
                            if (buffer == null){
                                System.out.println("Error reading music file.");
                            }
                            else{
                                //Convert back to file
                                try (FileOutputStream fos = new FileOutputStream("clientStorage/" + this.musicUrl)) {
                                    fos.write(buffer);
                                    this.downloading = false;
                                } catch (Exception e){
                                    if (Request.DEV_MODE) e.printStackTrace();
                                }
                            }
                        } catch (IOException e) {
                            if (Request.DEV_MODE) e.printStackTrace();
                        }
                    }
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
