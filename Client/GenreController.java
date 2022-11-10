import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class GenreController {
    @FXML
    private Button returnButton;
    @FXML
    private TextField textField;
    @FXML
    private Label result;


    public void returnButtonClick(){
        Stage stage=(Stage)returnButton.getScene().getWindow();
        stage.close();
    }

    public void doItButtonClick(){
        Commands commands=new Commands();
        commands.setName("count_greater_than_genre");
        try {
            commands.setGenre(MusicGenre.valueOf(textField.getText()));
            Main.send(commands, Main.datagramSocket, Main.datagramPacket);
            commands=Main.receive(Main.datagramSocket);
            result.setText(commands.getResult());
        }
        catch (Exception e){
            result.setText("Такого жанра нет");
        }
    }
}
