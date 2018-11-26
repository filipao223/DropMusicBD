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

    Client() {
        startClient();
    }

    public static void main(String[] args) {
        new Client();
    }

    private void startClient() {
        //Request connection to database server
        try {
            address = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            System.out.println("Failed getting host address");
            return;
        }

        try {
            Socket server = new Socket("localhost", PORT);
            String message = "2_user1";
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            if (!serializer.writeMessage(out, message.getBytes(), message.length())) {
                System.out.println("Error writing message to server");
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String readKeyboard = "";
        Scanner keyboardScanner = new Scanner(System.in);
        String user_name = "";

        while (true) {
            System.out.println("What do you want to do(insert the number):\n1.Login\n2.Logout\n3.Register\n4.Edit, add, or remove albuns,artist or musics\n5.Search for musics, artists or albuns\n6.Write a critique to a album\n7.Give editing privileges\n11.Upload a music\n12.Share musics\n13.Download a music\n20.Create a playlist\n21.Remove a playlist\n22.Turn a playlist public/private\n23.Add music to a playlist");
            readKeyboard = keyboardScanner.nextLine();
            if (readKeyboard.matches(("1"))) {
                String send = "1_", username, password;
                System.out.println("Insert your username:");
                username = keyboardScanner.nextLine();
                user_name = username;
                send = send.concat(username).concat("_");
                System.out.println("Insert your password:");
                password = keyboardScanner.next();
                send = send.concat(password);
                System.out.println(send);
            }
            else if (readKeyboard.matches("2")) {
                String send = "2_", username;
                System.out.println("Insert your username:");
                username = keyboardScanner.nextLine();
                send = send.concat(username);
            }
            else if (readKeyboard.matches("3")) {
                String send = "3_", username, password,first,last;
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
            else if (readKeyboard.matches("4")){
                String send ="4_",acao,tipo,name,caracteristica,edit,value,add;
                boolean isBirth = false;
                System.out.println("Do you want to alter, remove, or add?");
                acao = keyboardScanner.nextLine();
                send = send.concat(acao).concat("_");
                if(acao.matches("add")){
                    System.out.println("Artist,album or music?");
                    tipo = keyboardScanner.nextLine();
                    send = send.concat(tipo).concat("_");
                    System.out.println("Insert the name of the artist,album or music");
                    name = keyboardScanner.nextLine();
                    send = send.concat(name);
                }
                else if(acao.matches("alter")){
                    System.out.println("Artist,album or music?");
                    tipo = keyboardScanner.nextLine();
                    send = send.concat(tipo).concat("_");
                    if(tipo.matches("music")){
                        System.out.println("What do you want to alter(name,year,lyrics,artist)?");
                        caracteristica = keyboardScanner.nextLine();
                        send = send.concat(caracteristica).concat("_");
                        if(caracteristica.matches("year")){
                            isBirth = true;
                        }
                    }
                    else if(tipo.matches("album")){
                        System.out.println("What do you want to alter(name,year,description,artist,editor)?");
                        caracteristica = keyboardScanner.nextLine();
                        send = send.concat(caracteristica).concat("_");
                        if(caracteristica.matches("year")){
                            isBirth = true;
                        }
                    }
                    else if(tipo.matches("artist")){
                        System.out.println("What do you want to alter(name,year,description?");
                        caracteristica = keyboardScanner.nextLine();
                        send = send.concat(caracteristica).concat("_");
                        if(caracteristica.matches("year")){
                            isBirth = true;
                        }
                    }

                    System.out.println("Which item is to be edited?:");
                    edit = keyboardScanner.nextLine();
                    send = send.concat(edit).concat("_");

                    System.out.println("New value?:");
                    value = keyboardScanner.nextLine();
                    send = send.concat(value);

                    //Birth date needs to be checked for proper format
                    if (isBirth) {
                        if (!value.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])-(1[012]|0?[1-9])-((?:19|20)\\d{2})\\s*$")) {
                            System.out.println("Bad date format, should be d-m-yyyy");
                            break;
                        }
                    }
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
            else if(readKeyboard.matches("5")){
                String send = "5_",tipo,name;
                System.out.println("Artist,album or music?");
                tipo = keyboardScanner.nextLine();
                send = send.concat(tipo).concat("_");
                System.out.println("Insert the name of what you want to search");
                name = keyboardScanner.nextLine();
                send = send.concat(name);
            }
            else if(readKeyboard.matches("6")){
                String send = "6_",name,critique,rating;
                System.out.println("What is the critique?");
                critique = keyboardScanner.nextLine();
                send = send.concat(critique).concat("_");
                System.out.println("What is the rating?");
                rating = keyboardScanner.nextLine();
                send = send.concat(rating).concat("_");
                System.out.println("Insert the name of the album");
                name = keyboardScanner.nextLine();
                send.concat(name);
            }
            else if(readKeyboard.matches("7")){
                String send = "7_",target;
                System.out.println("Insert the targer username\n");
                target = keyboardScanner.nextLine();
                send = send.concat(target);
            }
            else if(readKeyboard.matches("11")){
                String send = "11", username,music;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Music name?: ");
                music = keyboardScanner.nextLine();
                send.concat(music);
                InetAddress ip = null;
                try {
                    ip = InetAddress.getLocalHost();
                } catch (java.net.UnknownHostException e) {
                    e.printStackTrace();
                }
                String address = ip.getHostAddress();
                send.concat(address);
            }
            else if(readKeyboard.matches("12")){
                String send = "12", username,target;
                System.out.println("Insert your username:");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Insert the targer username");
                target = keyboardScanner.nextLine();
                send.concat(target);
            }
            else if(readKeyboard.matches("13")){
                String send = "13", username,music;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Music name?: ");
                music = keyboardScanner.nextLine();
                send.concat(music);
                InetAddress ip = null;
                try {
                    ip = InetAddress.getLocalHost();
                } catch (java.net.UnknownHostException e) {
                    e.printStackTrace();
                }
                String address = ip.getHostAddress();
                send.concat(address);
            }
            else if(readKeyboard.matches("20")){
                String send = "20_",name;
                System.out.println("What is the playlist name");
                name = keyboardScanner.nextLine();
                send = send.concat(name);
            }
            else if(readKeyboard.matches("21")){
                String send = "21_",name;
                System.out.println("What is the playlist name");
                name = keyboardScanner.nextLine();
                send = send.concat(name);
            }
            else if(readKeyboard.matches("22")){
                String send = "22_",name;
                System.out.println("What is the playlist name");
                name = keyboardScanner.nextLine();
                send = send.concat(name);
            }else if(readKeyboard.matches("22")){
                String send = "22_",playlist_name,music_name;
                System.out.println("What is the playlist name");
                playlist_name = keyboardScanner.nextLine();
                send = send.concat(playlist_name).concat("_");
                System.out.println("What is the music name");
                music_name = keyboardScanner.nextLine();
                send = send.concat(music_name);
            }
        }
    }
}
