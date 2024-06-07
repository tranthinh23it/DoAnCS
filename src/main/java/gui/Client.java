package gui;

import Controller.FormChat;
import DAO.ListUser;
import DTO.Attachments;
import DTO.GroupMessages;
import DTO.Messages;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.scene.layout.VBox;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Client {
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private static final Gson gson = new Gson();
    private ExecutorService executorService;


    public Client(Socket socket) {
       try {
           this.socket = socket;
           this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
           this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
           this.executorService = Executors.newSingleThreadExecutor();
       }catch (IOException e){
           e.printStackTrace();
           closeEverything(socket,reader,writer);
       }
    }
    public  void sendMessageToServer(Messages messageToServer){
        try {
           if (messageToServer != null){
               String jsonMessage = gson.toJson(messageToServer);
               writer.write(jsonMessage);
               writer.newLine();
               writer.flush();
           }
        }
        catch (IOException e){
            e.printStackTrace();
            closeEverything(socket,reader,writer);
        }
    }
    //gui tin nhan nhom tu server
    public void sendMessageGroup(GroupMessages messages){
        try {
            if (messages != null){
                String jsonMessageGroup = gson.toJson(messages);
                writer.write(jsonMessageGroup);
                writer.newLine();
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
            closeEverything(socket,reader,writer);
        }
    }
    public void receiveMessageFromServer(VBox vBox) {
        executorService.execute(() -> {
            try {
                String jsonMessage;
                while ((jsonMessage = reader.readLine()) != null) {
                    Messages receivedMessage = gson.fromJson(jsonMessage, Messages.class);
                    if (receivedMessage != null) {
                        System.out.println(receivedMessage.getMessageContent());
                        String  usname = ListUser.getUsername(receivedMessage.getSenderId());
                        FormChat.addLabelLeft(receivedMessage.getMessageContent(),usname ,vBox);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket,reader,writer);
            }
        });
    }
    // nhan tin nhan nhom tu server
    public void receiveMessageGroupFromServer(VBox vBox){
        executorService.execute(()->{
            try {
                String jsonMessageGroup;
                while ((jsonMessageGroup = reader.readLine()) != null){
                    GroupMessages   receiveMessageGroup = gson.fromJson(jsonMessageGroup,GroupMessages.class);
                    if (receiveMessageGroup != null){
                        System.out.println(receiveMessageGroup.getMessageContent());
                        String  usname = ListUser.getUsername(receiveMessageGroup.getSenderId());
                        FormChat.addLabelLeft(receiveMessageGroup.getMessageContent(),usname ,vBox);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket,reader,writer);
            }
        });
    }
    public void sendFileToServer(File file, Integer groupId, Integer receiverId,int senderId) {
        try {
            // Đọc dữ liệu từ file và chuyển đổi thành mảng byte
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] fileData = new byte[(int) file.length()];
            fileInputStream.read(fileData);
            fileInputStream.close();

            // Tạo đối tượng Attachments và gán dữ liệu file vào thuộc tính dataFile
            Attachments attachment = new Attachments();
            attachment.setFileName(file.getName());
            attachment.setFileType("txt"); // Đặt loại file tùy thuộc vào định dạng của file
            attachment.setFileSize(fileData.length);
            attachment.setFileUrl(file.getAbsolutePath());
            attachment.setGroupId(groupId);
            attachment.setReceiverId(receiverId);
            attachment.setDataFile(fileData);
            attachment.setSenderId(senderId);

            // Chuyển đối tượng Attachments thành JSON
            String jsonAttachment = gson.toJson(attachment);

            // Gửi đối tượng Attachments dưới dạng JSON đến Server
            writer.write(jsonAttachment);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void receiveAttachmentFromServer(VBox vBox) {
        executorService.execute(() -> {
            try {
                String jsonMessage;
                while ((jsonMessage = reader.readLine()) != null) {
                    JsonObject jsonObject = JsonParser.parseString(jsonMessage).getAsJsonObject();
                    if (jsonObject.has("fileName")) {
                        Attachments receivedAttachment = gson.fromJson(jsonMessage, Attachments.class);
                        if (receivedAttachment != null) {
                            System.out.println("Received Attachment: " + receivedAttachment.getFileName());
                            FormChat.addFileLeft(receivedAttachment.getFileName(),"tester",vBox);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                closeEverything(socket, reader, writer);
            }
        });
    }


    public void closeEverything(Socket socket,BufferedReader reader,BufferedWriter writer) {
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
            if (executorService != null && !executorService.isShutdown()) {
                executorService.shutdownNow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
