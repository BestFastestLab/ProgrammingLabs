import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;

public class ConnectionController {
    @FXML
    private Button tryButton;

    public void exitClick(){
        System.exit(0);
    }

    public void tryClick() throws Exception{
        Stage stage=(Stage) tryButton.getScene().getWindow();
        stage.close();
        Parent root = FXMLLoader.load(getClass().getResource("Port.fxml"));
        Stage stage1=new Stage();
        stage1.setTitle("Ввод порта");
        stage1.setScene(new Scene(root));
        stage1.show();
    }
}
