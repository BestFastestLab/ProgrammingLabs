import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

public class RemoveController {
    @FXML
    private Button okButton;
    @FXML
    private Button returnButton;
    @FXML
    private TextField idTextView;
    @FXML
    private Label result;


    public void returnButtonClick() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    public void okButtonClick() {
            Commands commands = new Commands();
            commands.setName("remove_by_id");
            try {
                commands.setId(Integer.parseInt(idTextView.getText()));
            } catch (Exception e) {
                result.setText("Неверный формат ввода.");
            }
            Main.send(commands, Main.datagramSocket, Main.datagramPacket);
            try {
                commands = Main.receive(Main.datagramSocket);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.setText(commands.getResult());
        if (result.getText().equals("Элемент успешно удален!")) {
            Stage stage = (Stage) okButton.getScene().getWindow();
            stage.close();
        }
    }
}
