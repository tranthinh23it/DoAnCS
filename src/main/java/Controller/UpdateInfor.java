package Controller;

import DAO.ListUser;
import DAO.UserDAO;
import DTO.Users;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritablePixelFormat;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.List;

public class UpdateInfor {

    @FXML
    private ImageView imgView;
    @FXML
    private  Label email_u;

    @FXML
    private  Label full_name;

    @FXML
    private   Label nick_name;

    @FXML
    private  Label place;

    @FXML
    private  Label user_name;

    @FXML
    private  Label Save;

    @FXML
    private  TextField edit_email;

    @FXML
    private  TextField edit_fulname;

    @FXML
    private  TextField edit_nickname;

    @FXML
    private  TextField edit_place;
    private File selectedFile;
    private FileChooser fileChooser;

    @FXML
    private Label editImg;

    @FXML
    private  TextField edit_username;
    @FXML
    private Button btn;

    private static String username;

    public static void setUsername(String username) {
        UpdateInfor.username = username;
    }

    public void loadInfor(){
        List<Users> list = ListUser.getUSer(username);
        for (Users users : list) {
            if (users.getAvatarUrl() != null) {
                Image img = UserDAO.getImage(users.getUsername());
                imgView.setImage(img);
            }
            nick_name.setText("(" + users.getNickName() + ")");
            full_name.setText("(" + users.getFullName() + ")");
            user_name.setText("(" + users.getUsername() + ")");
            email_u.setText("(" + users.getEmail() + ")");
            place.setText("(" + users.getPlace() + ")");
        }
    }

    @FXML
    void UInformation(MouseEvent event) {
        List<Users> list = ListUser.getUSer(username);
        String userna = edit_username.getText();
        String fulna = edit_fulname.getText();
        String em= edit_email.getText();
        String nick= edit_nickname.getText();
        String place = edit_place.getText();

        for (Users us : list){
            if (userna.equals("")){
                userna = us.getUsername();
            }
            if (fulna.equals("")){
                fulna = us.getFullName();
            }
            if (em.equals("")){
                em = us.getEmail();
            }
            if (nick.equals("")){
                nick = us.getNickName();
            }
            if (place.equals("")){
                place=us.getPlace();
            }
        }
        ListUser.updateInfo(userna, fulna, em, nick, null, place);

        System.out.println(selectedFile!=null);
        if (selectedFile != null) {
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                byte[] fileContent = new byte[(int) selectedFile.length()];
                fis.read(fileContent);
                // Convert byte array to Image
                ByteArrayInputStream bis = new ByteArrayInputStream(fileContent);
                UserDAO.saveImageToDatabase(username, fileContent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("File selection cancelled.");
        }

        edit_username.clear();
        edit_nickname.clear();
        edit_email.clear();
        edit_fulname.clear();
        edit_place.clear();

        Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
        infoAlert.setTitle("Thông báo thông tin");
        infoAlert.setHeaderText(null);
        infoAlert.setContentText("Update Successfull!");
        infoAlert.showAndWait();
    }
    //edit image button
    @FXML
    public void editImage() {
        fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("C:\\"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        fileChooser.setTitle("Choose an image");
        Stage stage = (Stage) editImg.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            System.out.println(selectedFile.getAbsolutePath());
            try (FileInputStream fis = new FileInputStream(selectedFile)) {
                byte[] fileContent = new byte[(int) selectedFile.length()];
                fis.read(fileContent);

                // Convert byte array to Image
                ByteArrayInputStream bis = new ByteArrayInputStream(fileContent);
                Image decodedImage = new Image(bis);
                imgView.setImage(decodedImage);
                imgView.setFitHeight(100);
                imgView.setFitWidth(100);
                imgView.setClip(new Circle(50, 50, 50));

            } catch (IOException e) {
                e.printStackTrace();
                showAler("Error", "Error", "Could not load the image.");
            }
        } else {
            showAler("Error", "Error", "Please choose an image");
        }
    }
    //Show alert
    public static void showAler(String title, String header, String content){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public byte[] getImageData(Image image){
        int width = (int) image.getWidth();
        int height = (int) image.getHeight();
        int[] pixels = new int[width * height];

        PixelReader pixelReader = image.getPixelReader();
        pixelReader.getPixels(0, 0, width, height, WritablePixelFormat.getIntArgbInstance(), pixels, 0, width);

        ByteBuffer buffer = ByteBuffer.allocate(4 * width * height);
        IntBuffer intBuffer = buffer.asIntBuffer();
        intBuffer.put(pixels);

        return buffer.array();
    }

    //main
//    public static void main(String[] args) {
//        List<Users> list = ListUser.searchByUsername("hai");
//        for (Users users : list){
//            System.out.println(users.getUsername());
//        }
//    }
}