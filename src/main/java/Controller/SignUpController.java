package Controller;

import DAO.SignUp;
import DTO.Users;
import Mail.GenerateOtp;
import Mail.OtpMessage;
import Security_java.PasswordUtil;
import gui.LoginForm;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class SignUpController {
    private Stage stage;
    @FXML
    private TextField email_field;

    @FXML
    private TextField pass_word;

    @FXML
    private TextField user_name;
    @FXML
    private Button btn_otp;
    @FXML
    private Button btn_signup;
    @FXML
    private TextField txt_otp;

    private String otp = GenerateOtp.getOtp();

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    void Loginhere(MouseEvent event) throws IOException {
        // Lấy ra Stage từ Node gốc của sự kiện
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        // Đóng Stage
        stage.close();

    }

    @FXML
    void SignUp(MouseEvent event) {
        if (user_name.getText().isEmpty() || pass_word.getText().isEmpty() || email_field.getText().isEmpty() || txt_otp.getText().isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Please fill in all fields!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Please fill in all fields!");
            errorAlert.showAndWait();
            return;
        }
        String userName = user_name.getText();
        String passWord = pass_word.getText();
        String email = email_field.getText();
        String hashedPassword = PasswordUtil.hashPassword(passWord);

        List<Users> listCheck = SignUp.CheckSignUp(userName);
        if (listCheck.isEmpty()){
            if (otp.equals(txt_otp.getText())){
                SignUp.SignUpAccount(userName,passWord,email,hashedPassword);
                Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                infoAlert.setTitle("Notification");
                infoAlert.setHeaderText(null);
                infoAlert.setContentText("Sign Up Successfull!");
                infoAlert.showAndWait();
                // Lấy ra Stage từ Node gốc của sự kiện
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                // Đóng Stage
                stage.close();

            }else {
                Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                errorAlert.setTitle("Error");
                errorAlert.setHeaderText(null);
                errorAlert.setContentText("OTP is incorrect!");
                errorAlert.showAndWait();
            }
        }else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("UserName has already exist!");
            errorAlert.showAndWait();
        }
    }
    @FXML
    void sendOTP() {
        if (email_field.getText().isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Please fill in email fields!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Please fill in email fields!");
            errorAlert.showAndWait();
            return;
        }
        String email = email_field.getText();
        OtpMessage otpMessage = new OtpMessage(email,otp);
        otpMessage.send();

        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Notification");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText("OTP has been sent to your email!");
        infoAlert.showAndWait();
    }


}
