
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

import java.net.InetAddress;


public class AddressController {
    @FXML
    private Button button;
    @FXML
    private TextField textField;
    @FXML
    private Label result;
    @FXML
    private Label message;
    @FXML
    private Label advice;


    public boolean readText() {
        boolean k;
        try {
            Main.address= InetAddress.getByName(textField.getText());
            k=true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            k=false;
        }
        return k;
    }

    public void click() throws Exception {
        advice.setText("("+InetAddress.getLocalHost().getHostName()+"-наш совет)");
        result.setText("Попробуйте еще!");
        if (readText()) {
            Stage stage=(Stage) button.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("Authorization.fxml"));
            stage.setTitle("Вход");
            stage.setScene(new Scene(root));
            stage.show();
        }
        result.setVisible(true);
        textField.clear();
    }
}
