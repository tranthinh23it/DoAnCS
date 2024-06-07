package gui;

import Controller.FormChat;
import DTO.Attachments;
import DTO.GroupMessages;
import DTO.Messages;
import DTO.Users;
import com.google.gson.*;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashSet;
import java.util.Set;

public class Server {
    private ServerSocket serverSocket;
    private Set<ClientHandler> clientHandlers = new HashSet<>();
    private Gson gson = new GsonBuilder().create();

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer() {
        System.out.println("Server is running...");
        while (!serverSocket.isClosed()) {
            try {
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clientHandler = new ClientHandler(socket);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            } catch (IOException e) {
                e.printStackTrace();
                closeServerSocket();
            }
        }
    }

    public void broadcastMessage(Messages message) {
        String jsonMessage = gson.toJson(message);
        for (ClientHandler clientHandler : clientHandlers) {
            System.out.println(clientHandler.getUserId());
            System.out.println(message.getReceiverId());
            if (clientHandler.getUserId() == message.getReceiverId()) {
                clientHandler.sendMessage(jsonMessage);
            }
        }
    }
    public void broadcastGroupMessage(GroupMessages groupMessage) {
        String jsonMessageGroup = gson.toJson(groupMessage);
        for (ClientHandler clientHandler : clientHandlers) {
            if (clientHandler.getGroups().contains(groupMessage.getGroupId())) {
                System.out.println(groupMessage.getSenderId());
                System.out.println(clientHandler.userId);
                if (clientHandler.getUserId() != groupMessage.getSenderId()) {
                    clientHandler.sendMessage(jsonMessageGroup);
                }
            }
        }
    }
    public void broadcastIndividualAttachment(Attachments attachmentMessage) {
        String jsonAttachmentMessage = gson.toJson(attachmentMessage);
        for (ClientHandler clientHandler : clientHandlers) {
            System.out.println(clientHandler.userId +" "+ attachmentMessage.getReceiverId());
            if (clientHandler.getUserId() == attachmentMessage.getReceiverId()) {
                clientHandler.sendMessage(jsonAttachmentMessage);
            }
        }
    }

    public void broadcastGroupAttachment(Attachments attachmentMessage) {
        String jsonAttachmentMessage = gson.toJson(attachmentMessage);
        for (ClientHandler clientHandler : clientHandlers) {
            System.out.println(clientHandler.getGroups().contains(attachmentMessage.getGroupId()));
            if (clientHandler.getGroups().contains(attachmentMessage.getGroupId())) {
                System.out.println(clientHandler.getUserId() != attachmentMessage.getSenderId());

                if (clientHandler.getUserId() != attachmentMessage.getSenderId()) {
                    clientHandler.sendMessage(jsonAttachmentMessage);
                    System.out.println("send gr");
                }
            }
        }
    }


    public void removeClientHandler(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
    }

    private void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private BufferedWriter writer;
        private  int userId;

        public ClientHandler(Socket socket) {
            try {
                this.socket = socket;
                this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.userId =0;
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, reader, writer);
            }
        }
        public int getUserId() {
            return userId;
        }

        private Set<Integer> groups = new HashSet<>();
        public Set<Integer> getGroups() {
            return groups;
        }
        @Override
        public void run() {
            String jsonMessage;
            while (socket.isConnected()) {
                try {
                    jsonMessage = reader.readLine();
                    if (jsonMessage != null) {
                        System.out.println("Received: " + jsonMessage);

                        JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();

                        if (jsonObject.has("groupId") && jsonObject.has("dataFile")) {
                            // Xử lý tệp đính kèm từ client gửi cho nhóm
                            System.out.println(jsonObject.has("groupId") && jsonObject.has("dataFile"));
                            Attachments attachmentMessage = gson.fromJson(jsonMessage, Attachments.class);
                            groups.add(attachmentMessage.getGroupId());
                            if (attachmentMessage!= null) {
                                if (this.userId == 0) {
                                    this.userId = attachmentMessage.getSenderId();
                                }
                            }
                            broadcastGroupAttachment(attachmentMessage);
                        } else if (jsonObject.has("receiverId") && jsonObject.has("dataFile")) {
                            // Xử lý tệp đính kèm từ client gửi cho người nhận cá nhân
                            System.out.println(jsonObject.has("receiverId") && jsonObject.has("dataFile"));
                            Attachments attachmentMessage = gson.fromJson(jsonMessage, Attachments.class);
                            if (attachmentMessage != null){
                                if (this.userId == 0){
                                    this.userId = attachmentMessage.getSenderId();
                                }
                            }
                            broadcastIndividualAttachment(attachmentMessage);
                        } else if (jsonObject.has("groupId")) {
                            // Xử lý tin nhắn nhóm
                            GroupMessages groupMessage = gson.fromJson(jsonMessage, GroupMessages.class);
                            groups.add(groupMessage.getGroupId());
                            if (groupMessage!= null) {
                                if (this.userId == 0) {
                                    this.userId = groupMessage.getSenderId();
                                }
                            }
                            broadcastGroupMessage(groupMessage);
                        } else if (jsonObject.has("receiverId")) {
                            // Xử lý tin nhắn cá nhân
                            Messages message = gson.fromJson(jsonMessage, Messages.class);
                            if (message != null) {
                                if (this.userId == 0) {
                                    this.userId = message.getSenderId();
                                }
                            }
                            broadcastMessage(message);
                        } else {
                            System.out.println("Unknown message type: " + jsonMessage);
                        }
                    }
                } catch (JsonSyntaxException e) {
                    System.out.println("JsonSyntaxException: " + e.getMessage());
                } catch (IOException e) {
                    System.out.println("IOException: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        public void sendMessage(String jsonMessage) {
            try {
                writer.write(jsonMessage);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, reader, writer);
            }
        }

        private void closeEverything(Socket socket, BufferedReader reader, BufferedWriter writer) {
            removeClientHandler(this);
            try {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (socket != null) {
                    socket.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(7000);
            Server server = new Server(serverSocket);
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
