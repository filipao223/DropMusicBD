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
            System.out.println("Diga o que deseja fazer(insira o número):\n1.Login\n2.Logout\n3.Register\n4.Edit, add, or remove albuns,artist or musics\n5.Search for musics, artists or albuns\n6.See the details of a album\n7.See the details of a artist\n8.Write a critique to a album\n9.Give editing privileges\n13.Upload a music\n14.Share a music\n15.Download a music");
            readKeyboard = keyboardScanner.nextLine();
            if (readKeyboard.matches(("1"))) {
                String send, username, password;
                System.out.println("Insira o seu nome de utilizador:\n");
                username = keyboardScanner.nextLine();
                System.out.println("Insira a sua password:\n");
                password = keyboardScanner.next();
                send = "1".concat(username).concat(password);
            }
            else if (readKeyboard.matches("2")) {
                String send, username;
                System.out.println("Insira o seu nome de utilizador:\n");
                username = keyboardScanner.nextLine();
                send = "2".concat(username);
            }
            else if (readKeyboard.matches("3")) {
                String send, username, password;
                System.out.println("Insira o seu nome de utilizador:\n");
                username = keyboardScanner.nextLine();
                System.out.println("Insira a sua password:\n");
                password = keyboardScanner.next();
                send = "3".concat(username).concat(password);
            }
            else if (readKeyboard.matches("4")){
                String send, username,acao,tipo,name;
                System.out.println("Insira o seu nome de utilizador:\n");
                username = keyboardScanner.nextLine();
                System.out.println("Pretende alterar, remover ou adicionar?\n");
                acao = keyboardScanner.nextLine();
                if(acao.matches("adicionar")){
                    System.out.println("Artista,album ou musica?\n");
                    tipo = keyboardScanner.nextLine();
                    System.out.println("Insira o nome do artista/album/musica\n");
                    name = keyboardScanner.nextLine();
                    send = "4".concat(username).concat(acao).concat(tipo).concat(name);
                }
                else if(acao.matches("alterar")){
                    System.out.println("Artista,album ou musica?\n");
                    tipo = keyboardScanner.nextLine();
                    if(tipo.matches("musica")){

                    }
                }
            }
        }
    }
}
