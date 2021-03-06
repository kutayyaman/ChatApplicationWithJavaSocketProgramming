package Socket;

import Entity.User;
import Repository.Impl.UserRepositoryPostgre;
import Repository.UserRepository;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String clientUsername;
    private UserRepository userRepository;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = bufferedReader.readLine(); //socket araciligiyla ilk basta client baglanirken bu tarafa userName bilgisini yollamasini bekliyor
            clientHandlers.add(this);
            this.userRepository = new UserRepositoryPostgre();
            sendOnlineUsersToAllUsers();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    public String getClientUsername() {
        return clientUsername;
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = bufferedReader.readLine();
                if (messageFromClient.equals("getOnlineUsers")) {
                    sendOnlineUsers();
                } else {
                    String[] splittedMessage = messageFromClient.split(":");
                    String senderUsername = splittedMessage[0];
                    String message = splittedMessage[1];
                    Integer chatId = Integer.valueOf(splittedMessage[2]);

                    if (chatId == 0) {
                        sendMessageToGlobalChat();
                    } else {
                        sendMessageToChat(senderUsername, message, chatId);
                    }
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    private void sendMessageToChat(String senderUsername, String message, Integer chatId) {
        List<User> usersToSendMessage = userRepository.getByChatId(chatId);

        for (ClientHandler clientHandler : clientHandlers) {
            try {
                boolean shouldSend = usersToSendMessage.stream().anyMatch(user -> user.getUserName().equals(clientHandler.clientUsername));
                if (shouldSend) {
                    clientHandler.bufferedWriter.write("sana yeni mesaj geldi chat listeni ve su an acik olan chatini guncelle");
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void sendMessageToGlobalChat() {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                clientHandler.bufferedWriter.write("globalMessage");
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void sendOnlineUsers() {
        String onlineUsers = "onlineUsers: ";
        for (ClientHandler clientHandler : clientHandlers) {
            String clientUsername = clientHandler.getClientUsername();
            onlineUsers += String.format("%s,", clientUsername);
        }
        try {
            bufferedWriter.write(onlineUsers);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }

    public void sendOnlineUsersToAllUsers() {
        String onlineUsers = "onlineUsers: ";
        for (ClientHandler clientHandler : clientHandlers) {
            String clientUsername = clientHandler.getClientUsername();
            onlineUsers += String.format("%s,", clientUsername);
        }
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                clientHandler.bufferedWriter.write(onlineUsers);
                clientHandler.bufferedWriter.newLine();
                clientHandler.bufferedWriter.flush();
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.bufferedWriter.write(messageToSend);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);
        //broadcastMessage("SERVER: " + clientUsername + " has left the chat!");
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler();
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
}
