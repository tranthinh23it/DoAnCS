package gui;

import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.IOException;
import java.net.Socket;

public class MainClient extends Application {
    private Client client;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Client Attachment Test");

        try {
            // Kết nối tới máy chủ

            Socket socket = new Socket("localhost", 7000);
            client = new Client(socket);

            // Chọn tệp để gửi
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File("C:\\"));
            fileChooser.setTitle("Choose a file to send");
            File selectedFile = fileChooser.showOpenDialog(primaryStage);

            if (selectedFile != null) {
                // Gửi tệp tới máy chủ
                client.sendFileToServer(selectedFile, null, 1, 1); // Điều chỉnh groupId và receiverId theo nhu cầu
            }

            // Bắt đầu nhận tệp đính kèm từ server
            client.receiveAttachmentFromServer(new VBox());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
