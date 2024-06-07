package gui;

import Controller.LoginController;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class LoginForm extends Application {
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        LoginForm.stage = stage;
    }

    @Override
    public void start(Stage Primarystage) throws IOException {

        Socket client = new Socket("localhost",7000);
        this.stage = Primarystage;
        Thread messageTh = new Thread(new MessageHandler(client));
        messageTh.start();

    }
    static class MessageHandler implements Runnable{
        private Socket socket;

        public MessageHandler(Socket socket) {
            this.socket = socket;
        }
        public void run() {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                Scanner scanner = new Scanner(System.in);

                Platform.runLater(() -> {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class.getResource("hello-view.fxml"));
                        Stage primaryStage = LoginForm.getStage();
                        primaryStage.setTitle("Hello!");
                        Scene scene = new Scene(fxmlLoader.load(), 900, 600);
                        //stylecss
                        LoginController controller = fxmlLoader.getController();
                        controller.setStage(primaryStage);
//                        primaryStage.initStyle(StageStyle.UNDECORATED);
                        primaryStage.setScene(scene);
                        primaryStage.show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                Thread receiveThread = new Thread(()->{
                   try {
                       String message;
                       while ((message = reader.readLine())!= null){

                       }
                   }catch (IOException e){
                       e.printStackTrace();
                   }
                });
                receiveThread.start();

                while (true){
                    String messages = scanner.nextLine();
                    writer.write(messages+"\n");
                    writer.flush();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }

    }
    public static void main(String[] args) {
        launch();
    }
}