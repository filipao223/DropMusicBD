package server;

import request.Request;
import serializer.Serializer;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class responsible for handling client requests.
 * <p>
 * A string message is read from the client over TCP, and the content is tokenized using {@link #tokenizer(String)}.<br>
 * After tokenizing input, using {@link #handleRequest(String[])}, the requested feature is extracted and the appropriate class is called.
 * <p>
 * To manage database connections, {@link server.Connect} is used, handles connects and disconnects while also holding the connection
 * object used to access the database.
 * <p>
 * To check which feature user wants, the received integer code from the client is compared against the integer constants in {@link request.Request}.
 * <p>
 * To send a message back to the user, {@link server.Callback#callback(Socket, String)} is used, receiving a string created in {@link #handleRequest(String[])}.
 * <p>
 * The following features are available (and the methods that handle that feature):<br>
 * <b>Login/Logout</b>: to login and logout, {@link server.LoginLogout#login(String, String, String)} and {@link server.LoginLogout#logout(String, String)}<br>
 * <b>Register</b>: to register, {@link server.Register#register(String, String, String, String, String)}<br>
 * <b>Make editor</b>: to make someone an editor, {@link server.Manage#makeEditor(String, String, String)}<br>
 * <b>Search</b>: to run a search query (generated by the client), {@link server.Search#search(String, String)}<br>
 * <b>Add critique</b>: to critique an album, {@link server.Critique#critique(String, String, int, String, String)}<br>
 * <b>Add playlist</b>: to add a playlist, {@link server.Playlist#createPlaylist(String, String, String)}<br>
 * <b>Remove playlist</b>: to remove a playlist, {@link server.Playlist#removePlaylist(String, String, String)}<br>
 * <b>Add music to playlist</b>: to add an existing music to a playlist, {@link server.Playlist#addMusicToPlaylist(String, String, String, String)}<br>
 * <b>Remove music from playlist</b>: to remove an existing music from a playlist, {@link server.Playlist#removeMusicFromPlaylist(String, String, String, String)}<br>
 * <b>Share playlist</b>: to share a playlist, {@link server.Playlist#sharePlaylist(String, String, String)}<br>
 * <b>Upload music</b>: to upload music to the server, {@link server.Upload#upload(Socket, String, String, String)}<br>
 * <b>Manage</b>: to manage existing artists, albums and music, {@link server.RunSQL#runSQL(String, String)} runs a client-created SQL query, performing any permission or existence checks.
 *
 * @see server.LoginLogout
 * @see server.Register
 * @see server.Manage
 * @see server.Search
 * @see server.Critique
 * @see server.Playlist
 * @see server.Upload
 * @see server.RunSQL
 */
public class Process implements Runnable {
    private Socket client;
    private Serializer serializer = new Serializer();
    private String clientData;

    /**
     * Constructor of Process
     * @param client the new client's socket.
     */
    Process(Socket client){
        this.client = client;
    }

    @Override
    public void run() {
        //Log some info
        clientData = "IP: " + client.getInetAddress().getHostAddress()
                + " | Port: " + client.getLocalPort();
        System.out.println("New client being processed, " + clientData);

        //Read the data sent
        byte[] buffer;
        try{
            while (true){
                DataInputStream in = new DataInputStream(client.getInputStream());
                buffer = serializer.readMessage(in);
                if (buffer == null){
                    System.out.println(clientData + " | Error reading message.");
                    return;
                }

                System.out.println(clientData + " | Received: " + new String(buffer));
                //Decode feature requested
                String[] tokens = tokenizer(new String(buffer));

                handleRequest(tokens);
            }

        } catch (IOException e) {
            if (Request.DEV_MODE) e.printStackTrace();
            System.out.println("IOException: " + e.getMessage());
        }
    }

    /**
     * After receiving the tokenized user input, using a switch case, determines which feature was requested.<br>
     * Feature codes are in {@link request.Request}.
     * @param tokens tokenized user input, containg username, requested feature integer, music name, ...
     * @return simple true/false.
     */
    private boolean handleRequest(String[] tokens){
        //Format: feature_username_sql
        //if login: feature_username_password
        // TODO validate user data
        String callbackMessage = "Error";

        try{
            switch(Integer.parseInt(tokens[0])){
                case Request.LOGIN:
                    if (LoginLogout.login(tokens[1], tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | Logged in.");
                        callbackMessage = "Login";
                    }
                    break;
                case Request.LOGOUT:
                    if (LoginLogout.logout(tokens[1], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | Logged out.");
                        callbackMessage = "Logout";
                    }
                    break;
                case Request.REGISTER:
                    if (Register.register(tokens[1], tokens[2], tokens[3], tokens[4], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | Registered.");
                        callbackMessage = "Registered";
                    }
                    break;
                case Request.MAKE_EDITOR:
                    if (Manage.makeEditor(tokens[1], tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | Made \"" + tokens[2] + "\" an editor.");
                        callbackMessage = "Made \"" + tokens[2] + "\" an editor.";
                    }
                    break;
                case Request.SEARCH:
                    if (Search.search(tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | Successful query.");
                        callbackMessage = "Successful query";
                    }
                    break;
                case Request.CRITIQUE:
                    if (Critique.critique(tokens[1], tokens[2], Integer.parseInt(tokens[3]), tokens[4], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[3] + " | Uploaded critique.");
                        callbackMessage = "Uploaded critique";
                    }
                    break;
                case Request.ADD_PLAYLIST:
                    if (Playlist.createPlaylist(tokens[1], tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | Created playlist.");
                        callbackMessage = "Created playlist";
                    }
                    break;
                case Request.ADD_MUSIC_PLAYLIST:
                    if (Playlist.addMusicToPlaylist(tokens[1], tokens[2], tokens[3], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2] + " | " + tokens[3] + " | Added music to playlist.");
                        callbackMessage = "Added music to playlist";
                    }
                    break;
                case Request.DEL_MUSIC_PLAYLIST:
                    if (Playlist.removeMusicFromPlaylist(tokens[1], tokens[2], tokens[3], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2] + " | " + tokens[3] + " | Removed music from playlist.");
                        callbackMessage = "Removed music from playlist";
                    }
                    break;
                case Request.REMOVE_PLAYLIST:
                    if (Playlist.removePlaylist(tokens[1], tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2] + " | Removed playlist.");
                        callbackMessage = "Removed playlist";
                    }
                    break;
                case Request.SHARE_PLAYLIST:
                    if (Playlist.sharePlaylist(tokens[1], tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2] + " | Shared playlist.");
                        callbackMessage = "Shared playlist";
                    }
                    break;
                case Request.UPLOAD:
                    if (Upload.upload(client, tokens[1], tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2].split("\\.")[0] + " | Music uploaded.");
                        callbackMessage = "Music uploaded";
                    }
                    break;
                case Request.MANAGE:
                    if (RunSQL.runSQL(tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | Successful manage.");
                        callbackMessage = "Successful manage";
                    }
                    break;
                case Request.DOWNLOAD:
                    if (Download.download(client, tokens[1], tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2] + " | Successful download.");
                        callbackMessage = "Music downloaded";
                    }
                    break;
                case Request.ADD_MUSIC_ALBUM:
                    if (Manage.addMusicToAlbum(tokens[1], tokens[2], tokens[3], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2] + " | " + tokens[3] + " | Added music to album.");
                        callbackMessage = "Added music to album";
                    }
                    break;
                case Request.DEL_MUSIC_ALBUM:
                    if (Manage.removeMusicFromAlbum(tokens[1], tokens[2], tokens[3], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2] + " | " + tokens[3] + " | Removed music from album.");
                        callbackMessage = "Removed music from album";
                    }
                    break;
                case Request.REMOVE_ITEM:
                    if (Manage.deleteItem(tokens[1], tokens[2], tokens[3], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2] + " | " + tokens[3] + " | Removed item from database.");
                        callbackMessage = "Removed item from database";
                    }
                    break;
                case Request.SHARE:
                    if (Manage.share(tokens[1], tokens[2], clientData)){
                        System.out.println(clientData + " | " + tokens[1] + " | " + tokens[2] + " | Shared with user.");
                        callbackMessage = "Shared with user";
                    }
                    break;
            }
            if (!Callback.callback(client, callbackMessage))
                System.out.println(clientData + " | Error sending callback message.");

            Connect.disconnect(clientData);
        } catch (Exception e){
            if (Request.DEV_MODE) e.printStackTrace();
            System.out.println(clientData + " | Failed to handle request.");
        }

        return false;
    }

    /**
     * Tokenizes user input.
     * @param main message read from the client.
     * @return the tokenized message.
     */
    private String[] tokenizer(String main){
        return main.split("\\|");
    }
}
