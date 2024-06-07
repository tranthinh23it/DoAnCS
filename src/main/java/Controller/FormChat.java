package Controller;

import DAO.*;
import DTO.*;
import gui.Client;
import gui.LoginForm;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class FormChat implements Initializable{
    private Stage stage;
    private Users selectedUser;
    private Groups selectedGroup;
    private int currentUserId;
    private File selectedFile;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private TextField textMess;
    @FXML
    private Button buttonSend;
    @FXML
    private VBox vbox_message;

    private Client client;
    @FXML
    private ScrollPane sp_main;
    @FXML
    private ScrollPane sp_friends;

    @FXML
    private ScrollPane sp_groups;
    private static String username;
    @FXML
    private  Label User_name;
    @FXML
    private ListView<Users> list_friends;
    @FXML
    private ListView<Groups> list_groups;
    @FXML
    private Label nick_name;



    public static void setUsername(String username) {
        FormChat.username = username;
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
           client = new Client(new Socket("localhost",7000));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error connecting server.");
        }
        currentUserId = getUserIdFromUsername(username);
        vbox_message.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                sp_main.setVvalue((double) newValue);
            }
        });

        // friend
        list_friends.setCellFactory(param -> new ListCell<Users>() {
            @Override
            protected void updateItem(Users user, boolean empty) {
                super.updateItem(user, empty);
                if (empty || user == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setPadding(new Insets(5, 5, 5, 5));

                    //set image nưa
                    ImageView imageView = new ImageView(new Image("file:/C:/Users/my computer/Desktop/Java FX/ChattingApp/src/main/resources/Img/ava.png"));
                    imageView.setFitWidth(36);
                    imageView.setFitHeight(36);
                    imageView.setClip(new Circle(18,18,18));

                    Text text = new Text(user.getUsername());
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("fx-color: rgb(239,242,255 );" +
                            " -fx-padding: 4px;");
                    textFlow.setPadding(new Insets(7,10,7,10));
                    textFlow.setMaxWidth(250); // Set the maximum width for wrapping text
                    textFlow.setLineSpacing(5);
                    text.setFill((Color.rgb(0,0,0)));
                    text.setStyle("-fx-font-size: 14px; -fx-font-family: Berlin Sans FB");

                    hBox.getChildren().add(imageView);
                    hBox.getChildren().add(textFlow);
                    setGraphic(hBox);
                }
            }
        });
        list_friends.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                User_name.setText(newValue.getUsername());
                nick_name.setText(newValue.getNickName());
                selectedUser = newValue;
                list_groups.getSelectionModel().clearSelection();
                // Khi chọn một người dùng mới, cập nhật hiển thị tin nhắn
                vbox_message.getChildren().clear();  // Xóa tin nhắn cũ
                addMessage(currentUserId, selectedUser.getUserId());  // Hiển thị tin nhắn mới
            }
        });
        addFriendList(list_friends);
        addGroupsList(list_groups);
        //groups
        list_groups.setCellFactory(param -> new ListCell<Groups>() {
            @Override
            protected void updateItem(Groups group, boolean empty) {
                super.updateItem(group, empty);
                if (empty || group == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    HBox hBox = new HBox();
                    hBox.setAlignment(Pos.CENTER_LEFT);
                    hBox.setPadding(new Insets(5, 5, 5, 5));

                    ImageView imageView = new ImageView(new Image("file:/C:/Users/my computer/Desktop/Java FX/ChattingApp/src/main/resources/Img/ava.png"));
                    imageView.setFitWidth(36);
                    imageView.setFitHeight(36);
                    imageView.setClip(new Circle(18,18,18));

                    Text text = new Text(group.getGroupName());
                    TextFlow textFlow = new TextFlow(text);
                    textFlow.setStyle("fx-color: rgb(239,242,255 );" +
                            " -fx-padding: 4px;");
                    textFlow.setPadding(new Insets(7,10,7,10));
                    textFlow.setMaxWidth(250); // Set the maximum width for wrapping text
                    textFlow.setLineSpacing(5);
                    text.setFill((Color.rgb(0,0,0)));
                    text.setStyle("-fx-font-size: 14px; -fx-font-family: Berlin Sans FB");

                    hBox.getChildren().add(imageView);
                    hBox.getChildren().add(textFlow);
                    setGraphic(hBox);
                }
            }
        });
        list_groups.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
            if (newValue != null) {
                User_name.setText(newValue.getGroupName());
                selectedGroup = newValue;
                list_friends.getSelectionModel().clearSelection();
                vbox_message.getChildren().clear(); // Xóa tin nhắn cũ
                addGroupMessages(selectedGroup.getGroupId()); // Hiển thị tin nhắn mới của nhóm
            }
        });

        client.receiveMessageFromServer(vbox_message);
        client.receiveMessageGroupFromServer(vbox_message);
        client.receiveAttachmentFromServer(vbox_message);

        buttonSend.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String messageToSend = textMess.getText();
                Users selectedUser = list_friends.getSelectionModel().getSelectedItem();
                Groups selectedGroup = list_groups.getSelectionModel().getSelectedItem();

                if (selectedUser != null) {
                    list_friends.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue != selectedUser) {
                            vbox_message.getChildren().clear();
                        }
                    });

                    if (!messageToSend.isEmpty()) {
                        System.out.println(getFileExtension(messageToSend));
                        if (!getFileExtension(messageToSend).equals("")){
                            addFileRight(messageToSend,vbox_message);
                            client.sendFileToServer(selectedFile,null,selectedUser.getUserId(),currentUserId);

                            Messages messages = new Messages();
                            messages.setMessageContent("isFile");
                            messages.setSenderId(currentUserId);
                            messages.setReceiverId(selectedUser.getUserId());
                            Timestamp timesend = new Timestamp(System.currentTimeMillis());
                            messages.setSentTime(timesend);
                            Message_DAO.SaveMessage(messages);// luu tin nhan

                            try {
                                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                                byte[] fileData = new byte[(int) selectedFile.length()];
                                fileInputStream.read(fileData);
                                fileInputStream.close();

                                Attachments attachments = new Attachments();
                                attachments.setMessageId(messages.getMessageId());
                                attachments.setFileName(selectedFile.getName());
                                attachments.setFileType("txt");
                                attachments.setFileSize(fileData.length);
                                attachments.setFileUrl(selectedFile.getAbsolutePath());
                                attachments.setSenderId(currentUserId);
                                attachments.setReceiverId(selectedUser.getUserId());
                                attachments.setGroupId(-1);
                                attachments.setDataFile(fileData);

                                Attachments_DAO.saveMessFile(attachments);// luu tin kem file
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            textMess.setText(" ");
                        }else {
                            addLabelRight(messageToSend, vbox_message);

                            Messages message = new Messages();
                            message.setMessageContent(messageToSend);
                            message.setSenderId(currentUserId);
                            message.setReceiverId(selectedUser.getUserId());
                            Timestamp timesend = new Timestamp(System.currentTimeMillis());
                            message.setSentTime(timesend);
                            client.sendMessageToServer(message);
//                            Message_DAO.SaveMessage(message);
                            textMess.setText("");
                        }
                    }
                } else if (selectedGroup != null) {
                    list_groups.getSelectionModel().selectedItemProperty().addListener((observableValue, oldValue, newValue) -> {
                        if (newValue != selectedGroup) {
                            vbox_message.getChildren().clear();
                        }
                    });

                    if (!messageToSend.isEmpty()) {
                        if (!getFileExtension(messageToSend).equals("")){
                            addFileRight(messageToSend,vbox_message);
                            int groupid = selectedGroup.getGroupId();
                            client.sendFileToServer(selectedFile,groupid,null,currentUserId);

                            GroupMessages messages = new GroupMessages();
                            messages.setMessageContent("isFile");
                            messages.setSenderId(currentUserId);
                            messages.setGroupId(groupid);
                            Timestamp timesSend = new Timestamp(System.currentTimeMillis());
                            messages.setSentTime(timesSend);


                            try {
                                FileInputStream fileInputStream = new FileInputStream(selectedFile);
                                byte[] fileData = new byte[(int) selectedFile.length()];
                                fileInputStream.read(fileData);
                                fileInputStream.close();

                                Attachments attachments = new Attachments();
                                attachments.setMessageId(messages.getGroupMessageId());
                                attachments.setFileName(selectedFile.getName());
                                attachments.setFileType("txt");
                                attachments.setFileSize(fileData.length);
                                attachments.setFileUrl(selectedFile.getAbsolutePath());
                                attachments.setSenderId(currentUserId);
                                attachments.setReceiverId(-1);
                                attachments.setGroupId(groupid);
                                attachments.setDataFile(fileData);

//                                Attachments_DAO.saveMessFile(attachments);
                            }catch (IOException e){
                                e.printStackTrace();
                            }
                            textMess.setText(" ");

                        }else {

                            addLabelRight(messageToSend, vbox_message);

                            GroupMessages messages = new GroupMessages();
                            messages.setMessageContent(messageToSend);
                            messages.setSenderId(currentUserId);
                            messages.setGroupId(selectedGroup.getGroupId());
                            Timestamp timesSend = new Timestamp(System.currentTimeMillis());
                            messages.setSentTime(timesSend);
                            client.sendMessageGroup(messages);
//                            MessageGroups.SaveMessageGroup(messages);
                            textMess.setText("");
                        }
                    }
                }
            }
        });
    }
    public void addMessage(int sendId,int receiverID){
        List<Messages> messagesList = Message_DAO.loadMessages(sendId,receiverID);
        messagesList.sort(Comparator.comparing(Messages::getSentTime));

        for (Messages message : messagesList){

            if (message.getSenderId() == sendId) {
                if (message.getMessageContent().equals("isFile")){
                    Attachments attachments = Attachments_DAO.loadMessFile(message.getMessageId());
                    addFileRight(attachments.getFileName(),vbox_message);
                }else {
                    addLabelRight(message.getMessageContent(), vbox_message);
                }

            }
            if (message.getSenderId() == receiverID){
                if (message.getMessageContent().equals("isFile")){
                    Attachments attachments = Attachments_DAO.loadMessFile(message.getMessageId());
                    String  usname = ListUser.getUsername(message.getSenderId());
                    addLabelLeft(message.getMessageContent(),usname,vbox_message);
                }else {
                    String  usname = ListUser.getUsername(message.getSenderId());
                    addLabelLeft(message.getMessageContent(),usname,vbox_message);
                }

            }
        }
    }
    public void addGroupMessages(int groupId) {
        List<GroupMessages> groupMessagesList = GroupMessages_DAO.loadGroupMessages(groupId);
        for (GroupMessages message : groupMessagesList){

            if (message.getSenderId() == currentUserId) {
                String  usname = ListUser.getUsername(message.getSenderId());
                addLabelRight(message.getMessageContent(), vbox_message);
            } else {
                String  usname = ListUser.getUsername(message.getSenderId());
                addLabelLeft(message.getMessageContent(),usname, vbox_message);
            }
        }
    }
    private int getUserIdFromUsername(String username){
        return ListUser.getIDUser(username);
    }
    public  void addFriendList(ListView listView) {
        List<Users> listFriend = ListUser.loadName(username);
        Platform.runLater(() -> listView.getItems().addAll(listFriend));
    }
    public void addGroupsList(ListView listView){
        List<Groups> listGroup = GroupMessages_DAO.loadGroup();
        Platform.runLater(()-> listView.getItems().addAll(listGroup));
    }

    public static void addLabelRight(String messageContent, VBox vbox_message){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setPadding(new Insets(1, 1, 5, 10));
        //ảnh taạm thời
        ImageView imageView = new ImageView(UserDAO.getImage(username));
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setClip(new Circle(18,18,18));

        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.CENTER_RIGHT);
        Label label = new Label();
        label.setText("You");

        Text text = new Text(messageContent);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("fx-color: rgb(239,242,255 );" +
                " -fx-background-color: #0099FF;" +
                "-fx-background-radius: 20; -fx-border-radius: 20;  -fx-padding: 8px 12px; ");
        textFlow.setPadding(new Insets(10,10,10,10));
        textFlow.setMaxWidth(250);
        textFlow.setLineSpacing(5);
        text.setFill((Color.color(0.934, 0.945, 0.996)));
        text.setStyle("-fx-font-size: 14px; -fx-font-family: Berlin Sans FB");

        vBox1.getChildren().add(label);
        vBox1.getChildren().add(textFlow);

        hBox.getChildren().add(vBox1);
        hBox.getChildren().add(imageView);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox_message.getChildren().add(hBox);
            }
        });
    }
    public static void addFileRight(String filename,VBox vbox_message){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_RIGHT);
        hBox.setPadding(new Insets(1, 1, 5, 10));

        //anh profile
        ImageView imageView = new ImageView(UserDAO.getImage(username));
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setClip(new Circle(18,18,18));

        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.CENTER_RIGHT);
        Label label = new Label();
        label.setText("You");

        Text text = new Text(filename);
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("fx-color: rgb(239,242,255 );" +
                " -fx-background-color: #0099FF;" +
                "-fx-background-radius: 20; -fx-border-radius: 20;  -fx-padding: 8px 12px; ");
        textFlow.setPadding(new Insets(10,10,10,10));
        textFlow.setMaxWidth(250);
        textFlow.setLineSpacing(5);
        text.setFill((Color.color(0.934, 0.945, 0.996)));
        text.setStyle("-fx-font-size: 14px; -fx-font-family: Berlin Sans FB");

        vBox1.getChildren().add(label);
        vBox1.getChildren().add(textFlow);

        hBox.getChildren().add(vBox1);
        hBox.getChildren().add(imageView);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox_message.getChildren().add(hBox);
            }
        });
    }
    public static void addLabelLeft(String messageFromClient,String usernam, VBox vbox){
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        hBox.setPadding(new Insets(1,1,5,10));

        ImageView imageView = new ImageView(UserDAO.getImage(username));
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setClip(new Circle(18,18,18));
        //set anh
        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label();
        label.setText(usernam);

        Text text = new Text(messageFromClient);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("fx-color: rgb(239,242,255 );" +
                " -fx-background-color: #EEEEEE;" +
                "-fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 8px 12px;");
        textFlow.setPadding(new Insets(7,10,7,10));
        textFlow.setMaxWidth(250); // Set the maximum width for wrapping text
        textFlow.setLineSpacing(5);
        text.setFill((Color.rgb(0,0,0)));
        text.setStyle("-fx-font-size: 14px; -fx-font-family: Berlin Sans FB");

        vBox1.getChildren().add(label);
        vBox1.getChildren().add(textFlow);

        hBox.getChildren().add(imageView);
        hBox.getChildren().add(vBox1);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);
            }
        });
    }
    public static void addFileLeft(String fileName,String usernam, VBox vbox){
        System.out.println(fileName);
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.BOTTOM_LEFT);
        hBox.setPadding(new Insets(1,1,5,10));

        ImageView imageView = new ImageView(UserDAO.getImage(username));
        imageView.setFitWidth(36);
        imageView.setFitHeight(36);
        imageView.setClip(new Circle(18,18,18));
        //set anh
        VBox vBox1 = new VBox();
        vBox1.setAlignment(Pos.CENTER_LEFT);
        Label label = new Label();
        label.setText(usernam);

        Text text = new Text(fileName);
        TextFlow textFlow = new TextFlow(text);
        textFlow.setStyle("fx-color: rgb(239,242,255 );" +
                " -fx-background-color: #EEEEEE;" +
                "-fx-background-radius: 20; -fx-border-radius: 20; -fx-padding: 8px 12px;");
        textFlow.setPadding(new Insets(7,10,7,10));
        textFlow.setMaxWidth(250); // Set the maximum width for wrapping text
        textFlow.setLineSpacing(5);
        text.setFill((Color.rgb(0,0,0)));
        text.setStyle("-fx-font-size: 14px; -fx-font-family: Berlin Sans FB");

        vBox1.getChildren().add(label);
        vBox1.getChildren().add(textFlow);

        hBox.getChildren().add(imageView);
        hBox.getChildren().add(vBox1);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                vbox.getChildren().add(hBox);
            }
        });
    }

    @FXML
    void getFile(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Files", "*.*"));
        fileChooser.setTitle("Choose an image");
        Stage stage = new Stage(); // Tạo một Stage mới
        selectedFile = fileChooser.showOpenDialog(stage);
        textMess.setText(selectedFile.getName());

    }
    @FXML
    void resetForm(MouseEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class
                .getResource("reset-pass.fxml"));
        Parent root = fxmlLoader.load();

        ResetPassword sign = fxmlLoader.getController();
        Stage stage1 = new Stage();

        Scene scene = new Scene(root,380,400);
        stage1.setScene(scene);
        stage1.show();
    }
    @FXML
    void setProFile(MouseEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class
                .getResource("pro-file.fxml"));
        Parent root = fxmlLoader.load();

        SignUpController sign = fxmlLoader.getController();
        Stage stage1 = new Stage();

        Scene scene = new Scene(root,360,510);
        stage1.setScene(scene);
        stage1.show();
    }
    @FXML
    void updataInfor(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class
                .getResource("update-infor.fxml"));
        Parent root = fxmlLoader.load();

        UpdateInfor update = fxmlLoader.getController();
        update.loadInfor();
        Stage stage1 = new Stage();

        Scene scene = new Scene(root,450,530);
        stage1.setScene(scene);
        stage1.show();

    }
    @FXML
    void post_status(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class
                .getResource("post-status.fxml"));
        Parent root = fxmlLoader.load();

        StatusController formChat = fxmlLoader.getController();
        formChat.setStage(stage);

        Scene scene = new Scene(root,900,600);
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    void add_friend(MouseEvent event) throws  IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class
                .getResource("friend-ship.fxml"));
        Parent root = fxmlLoader.load();
        Friends update = fxmlLoader.getController();
        Stage stage1 = new Stage();

        Scene scene = new Scene(root,450,530);
        stage1.setScene(scene);
        stage1.show();
    }
    public static String getFileExtension(String fileName) {
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex != -1 && dotIndex < fileName.length() - 1) {
            return fileName.substring(dotIndex + 1);
        } else {
            return "";
        }
    }

    public static void main(String[] args) {
        System.out.println(getFileExtension("text"));
    }
}
