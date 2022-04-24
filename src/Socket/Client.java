package Socket;

import GUIS.ClientChatGUI;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private ClientChatGUI clientChatGUI;

    public Client(Socket socket, String username, ClientChatGUI clientChatGUI) {
        try {
            this.socket = socket;
            this.username = username;
            this.clientChatGUI = clientChatGUI;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter.write(username); // we write this to announce when CliendHander is created
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(String message, Integer chatId) {
        try {
            if (socket.isConnected()) {
                bufferedWriter.write(username + ":" + message + ":" + chatId);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendGetOnlineUserRequestToServer() {
        try {
            if (socket.isConnected()) {
                bufferedWriter.write("getOnlineUsers");
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String msgFromServer;

                while (socket.isConnected()) {
                    try {
                        msgFromServer = bufferedReader.readLine();
                        System.out.println(msgFromServer); // bu clienta mesaj geldiyse hem aktif sohbeti hem chat listesini guncellemeli yani buraya ClientChatGUI'dan methodlar cagrilcak
                        if (msgFromServer.contains("onlineUsers: ")) {
                            clientChatGUI.updateOnlineUsersTextAreaOfGlobalChatGUI(msgFromServer);
                        } else if (msgFromServer.toLowerCase().contains("globalmessage")) {
                            clientChatGUI.updateGlobalChat();
                        } else {
                            clientChatGUI.updateChatListAndSelectedChatTextArea();
                        }

                    } catch (IOException e) {
                        closeEverything(socket, bufferedReader, bufferedWriter);
                    }
                }
            }
        }).start();
    }

    private void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username for the group chat: ");
        String username = scanner.nextLine();
        Socket socket = new Socket("localhost", 1234);
        /*Client client = new Client(socket, username);
        client.listenForMessage();
        client.sendMessage("4 numarali chatteki marala deneme mesaji",5);*/
    }
}
