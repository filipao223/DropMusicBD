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

        while (true) {
            System.out.println("What do you want to do(inser the number):\n1.Login\n2.Logout\n3.Register\n4.Edit, add, or remove albuns,artist or musics\n5.Search for musics, artists or albuns\n6.Write a critique to a album\n7.Give editing privileges\n11.Upload a music\n12.Share musics\n13.Download a music");
            readKeyboard = keyboardScanner.nextLine();
            if (readKeyboard.matches(("1"))) {
                String send = "1", username, password;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Insert your password:\n");
                password = keyboardScanner.next();
                send.concat(password).concat("sql");
            }
            else if (readKeyboard.matches("2")) {
                String send = "2", username;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username).concat("sql");
            }
            else if (readKeyboard.matches("3")) {
                String send = "3", username, password;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Insert your password:\n");
                password = keyboardScanner.next();
                send.concat("password").concat("sql");
            }
            else if (readKeyboard.matches("4")){
                String send ="4", username,acao,tipo,name,caracteristica,edit,value,add;
                boolean isBirth = false;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Do you want to alter, remove, or add\n");
                acao = keyboardScanner.nextLine();
                send.concat(acao);
                if(acao.matches("add")){
                    System.out.println("Artist,album or music?\n");
                    tipo = keyboardScanner.nextLine();
                    send.concat(tipo);
                    System.out.println("Insert the name of the artist,album or music\n");
                    name = keyboardScanner.nextLine();
                    send.concat(name);
                    send.concat("sql");
                }
                else if(acao.matches("alter")){
                    System.out.println("Artist,album or music??\n");
                    tipo = keyboardScanner.nextLine();
                    send.concat(tipo);
                    if(tipo.matches("music")){
                        System.out.println("What do you want to alter(name,year,lyrics,artist)?\n");
                        caracteristica = keyboardScanner.nextLine();
                        send.concat(caracteristica);
                        if(caracteristica.matches("year")){
                            isBirth = true;
                        }
                    }
                    else if(tipo.matches("album")){
                        System.out.println("What do you want to alter(name,year,description,artist,editor)?\n");
                        caracteristica = keyboardScanner.nextLine();
                        send.concat(caracteristica);
                        if(caracteristica.matches("year")){
                            isBirth = true;
                        }
                    }
                    else if(tipo.matches("artist")){
                        System.out.println("What do you want to alter(name,year,description?\n");
                        caracteristica = keyboardScanner.nextLine();
                        send.concat(caracteristica);
                        if(caracteristica.matches("year")){
                            isBirth = true;
                        }
                    }

                    System.out.println("Which item is to be edited?:\n");
                    edit = keyboardScanner.nextLine();
                    send.concat(edit);

                    System.out.println("New value?:\n");
                    value = keyboardScanner.nextLine();
                    send.concat(edit);

                    //Birth date needs to be checked for proper format
                    if (isBirth) {
                        if (!value.matches("^\\s*(3[01]|[12][0-9]|0?[1-9])-(1[012]|0?[1-9])-((?:19|20)\\d{2})\\s*$")) {
                            System.out.println("Bad date format, should be d-m-yyyy");
                            break;
                        }
                    }
                }
                else if(acao.matches("remove")){
                    System.out.println("Remove a artist, album, or music?\n");
                    tipo = keyboardScanner.nextLine();
                    send.concat(tipo);
                    System.out.println("Name?:\n");
                    name = keyboardScanner.nextLine();
                    send.concat(name);
                }
                send.concat("sql");
            }
            else if(readKeyboard.matches("5")){
                String send = "5", username,tipo,name;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Artist,album or music??\n");
                tipo = keyboardScanner.nextLine();
                send.concat(tipo);
                System.out.println("Insert the name of what you want to search\n");
                name = keyboardScanner.nextLine();
                send.concat(name);
                send.concat("sql");
            }
            else if(readKeyboard.matches("6")){
                String send = "6", username,name,critique;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Insert the name of the album\n");
                name = keyboardScanner.nextLine();
                send.concat(name);
                System.out.println("What is the critique?\n");
                critique = keyboardScanner.nextLine();
                send.concat(critique);
                send.concat("sql");
            }
            else if(readKeyboard.matches("7")){
                String send = "7", username,target;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Insert the targer username\n");
                target = keyboardScanner.nextLine();
                send.concat(target);
                send.concat("sql");
            }
            else if(readKeyboard.matches("11")){
                String send = "7", username,music;
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
                send.concat("sql");
            }
            else if(readKeyboard.matches("12")){
                String send = "12", username,target;
                System.out.println("Insert your username:\n");
                username = keyboardScanner.nextLine();
                send.concat(username);
                System.out.println("Insert the targer username\n");
                target = keyboardScanner.nextLine();
                send.concat(target);
                send.concat("sql");
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
                send.concat("sql");
            }
        }
    }
}
