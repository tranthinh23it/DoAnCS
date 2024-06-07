package Controller;

import gui.LoginForm;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class StatusController {
    private Stage stage;

    @FXML
    private Pane leftBar;

    @FXML
    private Pane paneleft;

    @FXML
    private Button pro_file;

    @FXML
    private Button reset_password;
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    void resetForm(MouseEvent event) throws IOException {
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
    void form_chat(MouseEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(FormChat.class
                .getResource("form_chat.fxml"));
        Parent root = fxmlLoader.load();

        FormChat formChat = fxmlLoader.getController();
        formChat.setStage(stage);

        Scene scene = new Scene(root,900,600);
        stage.setScene(scene);
        stage.show();
    }
}
