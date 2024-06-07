package Controller;

import DAO.LogIN;
import DTO.Users;
import Security_java.PasswordUtil;
import gui.LoginForm;
import gui.Server;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.security.Security;
import java.util.List;

public class LoginController {
    private Stage stage;
    public static Users currentUser;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    private Button test;

    @FXML
    private TextField acc_field;

    @FXML
    private PasswordField pass_field;
    private String  UususName;

    @FXML
    void nextScene(MouseEvent event) throws IOException {

        String AccoutName = acc_field.getText().trim();
        String PassWord = pass_field.getText().trim();
        String hashedPass = LogIN.getHasedCode(AccoutName);
        List<Users> listCheck = LogIN.checkLogin(AccoutName,PassWord);

        List<Users> getus = LogIN.getInfor(AccoutName);
        for (Users user : getus){
           UususName = user.getUsername();
            currentUser = user;
        }

        if (!listCheck.isEmpty() && PasswordUtil.checkPassword(PassWord,hashedPass)){
            FormChat.setUsername(UususName);
            UpdateInfor.setUsername(UususName);
            FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class
                    .getResource("form-chat.fxml"));
            Parent root = fxmlLoader.load();

            FormChat formChat = fxmlLoader.getController();
            formChat.setStage(stage);

            Scene scene = new Scene(root,900,600);
            stage.setScene(scene);
            stage.show();
        }else {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Thông báo thông tin");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("Your Account not exist or Incorrect password!");
            infoAlert.showAndWait();
        }

    }
    @FXML
    void createAccount(MouseEvent event) throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(LoginForm.class
                .getResource("sign-up.fxml"));
        Parent root = fxmlLoader.load();

        SignUpController sign = fxmlLoader.getController();
        Stage stage1 = new Stage();

        Scene scene = new Scene(root,381,502);
        stage1.setScene(scene);
        stage1.show();
    }
    @FXML
    void Exit(MouseEvent event) {
        // Lấy ra Stage từ Node gốc của sự kiện
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Đóng Stage
        stage.close();
    }
}