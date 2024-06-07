package Controller;

import Mail.GenerateOtp;
import Mail.OtpMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ResetPassword {
    private Stage stage;
    @FXML
    private Button btn_confirm;

    @FXML
    private Button btn_send;

    @FXML
    private TextField txt_email;

    @FXML
    private TextField txt_otp;

    @FXML
    private TextField txt_password;
    @FXML
    private TextField txt_user;

    private String otp = GenerateOtp.getOtp();

    @FXML
    void sendOTP() {
        if (txt_email.getText().isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Please fill in email fields!");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Please fill in email fields!");
            errorAlert.showAndWait();
            return;
        }
        String email = txt_email.getText();
        OtpMessage otpMessage = new OtpMessage(email,otp);
        otpMessage.send();

        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Notification");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText("OTP has been sent to your email!");
        infoAlert.showAndWait();
    }

    @FXML
    void changePass() {

        if (txt_email.getText().isEmpty() || txt_user.getText().isEmpty() || txt_password.getText().isEmpty() || txt_otp.getText().isEmpty()){
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("Please fill in all fields!");
            errorAlert.showAndWait();
            return;
        }
        String email = txt_email.getText();
        String username = txt_user.getText();
        String password = txt_password.getText();
        String otpInput = txt_otp.getText();

        if (txt_otp.getText().equals(otp)) {
            DAO.ResetPassword.changePassword(username, email, password);
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Notification");
            infoAlert.setHeaderText(null);
            infoAlert.setContentText("Password has been changed successfully!");
            infoAlert.showAndWait();
        } else {
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("OTP is incorrect!");
            errorAlert.showAndWait();
        }

    }


}
