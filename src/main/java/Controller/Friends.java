package Controller;

import DAO.FriendDAO;
import DAO.ListUser;
import DTO.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import org.hibernate.tool.schema.Action;

import java.util.List;

public class Friends {

    @FXML
    private Button btn;

    @FXML
    private TextField txt;

    @FXML
    private VBox vbox_request;

    @FXML
    private VBox vbox_search;

//  private static int sendID = LoginController.currentUser.getUserId();
    private static int sendID =20;


    @FXML
    public void save(ActionEvent event){
        txt.getText();
        if (!(txt.getText().isEmpty())){
            List<Users> list = ListUser.searchByUsername(txt.getText());
            for (Users user : list){
                addLabel(user, vbox_search);
            }
        }
    }

    public static void addLabel(Users user, VBox vBox){
        HBox hBox = new HBox();

        Text text = new Text(user.getUsername());
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("fx-color: rgb(239,242,255 );" +
                " -fx-background-color: #0099FF;" +
                " -fx-padding: 8px 12px; ");
        textFlow.setPadding(new Insets(10,10,10,10));
        textFlow.setPrefWidth(308);
        textFlow.setMaxWidth(308);
        textFlow.setLineSpacing(5);
        text.setFill((Color.color(0.934, 0.945, 0.996)));
        text.setStyle("-fx-font-size: 14px; -fx-font-family: Arial; -fx-text-alignment: left;-fx-font-weight: bold");

        hBox.getChildren().add((textFlow));
        vBox.getChildren().add(hBox);

        hBox.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("Send friend request to " + user.getUsername()+"?");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")){
                //send request
                FriendDAO.sendRequest(sendID, user.getUserId());
            }
        });
    }
    public static void addLabelRequest(Users user, VBox vBox){
        HBox hBox = new HBox();

        Text text = new Text(user.getUsername());
        TextFlow textFlow = new TextFlow(text);

        textFlow.setStyle("fx-color: rgb(239,242,255 );" +
                " -fx-background-color: #0099FF;" +
                " -fx-padding: 8px 12px; ");
        textFlow.setPadding(new Insets(10,10,10,10));
        textFlow.setPrefWidth(308);
        textFlow.setMaxWidth(308);
        textFlow.setLineSpacing(5);
        text.setFill((Color.color(0.934, 0.945, 0.996)));
        text.setStyle("-fx-font-size: 14px; -fx-font-family: Arial; -fx-text-alignment: left;-fx-font-weight: bold");

        hBox.getChildren().add((textFlow));
        vBox.getChildren().add(hBox);

        hBox.setOnMouseClicked(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setHeaderText(null);
            alert.setContentText("Accept request " + user.getUsername()+"?");
            alert.showAndWait();
            if (alert.getResult().getText().equals("OK")){
                //Accept request
                FriendDAO.acceptRequest(sendID, user.getUserId());
                vBox.getChildren().remove(hBox);
            }
        });
    }

    @FXML
    public void initialize(){
        List<DTO.Friends> listRequest = FriendDAO.receivedRequest(sendID);
        List<Users> listUser = FriendDAO.convert(listRequest);
        for (DTO.Users user : listUser){
            addLabelRequest(user, vbox_request);
        }
    }
}
