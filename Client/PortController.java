
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;


public class PortController {
    @FXML
    private Button button;
    @FXML
    private TextField textField;
    @FXML
    private Label result;


    public Integer readText() {
        int k = 0;
        try {
            k = Integer.parseInt(textField.getText());
        } catch (Exception e) {

        }
        return k;
    }

    public void click() throws Exception {
        result.setText("Попробуйте еще!");
        if (!(readText() < 1024 | readText() > 65000)) {
            Main.port=readText();
            Stage stage=(Stage) button.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("Address.fxml"));
            stage.setTitle("Ввод порта");
            stage.setScene(new Scene(root));
            stage.show();
        }
        result.setVisible(true);
        textField.clear();
    }
}
