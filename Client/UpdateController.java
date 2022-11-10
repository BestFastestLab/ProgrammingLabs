import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

public class UpdateController {
    @FXML
    private Button okButton;
    @FXML
    private TextField idTextView;
    @FXML
    private Button returnButton;
    @FXML
    private Label result;


    public void okButtonClick() throws Exception{
        Commands commands=new Commands();
        commands.setName("update");
        try {
            commands.setId(Integer.parseInt(idTextView.getText()));
        }catch (Exception e){
            result.setText("Неверный формат ввода.");
        }
        Main.send(commands, Main.datagramSocket, Main.datagramPacket);
        commands=Main.receive(Main.datagramSocket);
        if (commands.getResult().equals("Waiting for name")){
            Stage stage=(Stage) okButton.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("Add.fxml"));
            stage.setTitle("Добавление объекта");
            stage.setScene(new Scene(root));
            stage.show();
            AddController.status="add";
        }
        else {
            result.setText(commands.getResult());
        }
    }

    public void returnButtonClick(){
        Stage stage=(Stage) returnButton.getScene().getWindow();
        stage.close();
    }
}
