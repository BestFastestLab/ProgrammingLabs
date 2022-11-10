import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.*;

public class AddController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField xTextField;
    @FXML
    private TextField yTextField;
    @FXML
    private TextField participantsTextField;
    @FXML
    private TextField singlesTextField;
    @FXML
    private TextField albumsTextField;
    @FXML
    private TextField genreTextField;
    @FXML
    private TextField bestAlbumTextField;
    @FXML
    private Button addButton;
    @FXML
    private Button returnButton;
    @FXML
    private Label result;

    public static String status;

    public void returnClick(){
        Stage stage=(Stage)returnButton.getScene().getWindow();
        stage.close();
    }
    public void addClick() throws Exception{
        Commands commands=new Commands();
        MusicBand band=new MusicBand();
        Coordinates coordinates=new Coordinates();
        Stage stage=(Stage)addButton.getScene().getWindow();
        boolean flag=true;
        try {
            if (!nameTextField.getText().equals("")) {
                band.setName(nameTextField.getText());
            }
            if (!xTextField.getText().equals(null)) {
                coordinates.setX(Double.parseDouble(xTextField.getText()));
            }
            if (!yTextField.getText().equals(null)) {
                coordinates.setY(Long.parseLong(yTextField.getText()));
            }
            band.setCoordinates(coordinates);
            if (!participantsTextField.getText().equals(null)) {
                band.setNumberOfParticipants(Long.parseLong(participantsTextField.getText()));
            }
            if (!singlesTextField.getText().equals(null)) {
                band.setSinglesCount(Long.parseLong(singlesTextField.getText()));
            }
            if (!albumsTextField.getText().equals(null)) {
                band.setAlbumsCount(Long.parseLong(albumsTextField.getText()));
            }
            if (!genreTextField.getText().equals(null)) {
                band.setGenre(MusicGenre.valueOf(genreTextField.getText()));
            }
            if (!bestAlbumTextField.getText().equals(null)) {
                Album album=new Album();
                album.setName(bestAlbumTextField.getText());
                band.setBestAlbum(album);
            }
            else {
                result.setText("Некоторые поля заполнены ошибочно!");
                flag=false;
            }
        } catch (Exception e){
            result.setText("Некоторые поля заполнены ошибочно!");
            flag=false;
        }
        if (flag) {
            commands.setBand(band);
            band.setOwner(Main.owner);
            commands.setName(status);
            System.out.println(commands);
            Main.send(commands, Main.datagramSocket, Main.datagramPacket);
            commands=Main.receive(Main.datagramSocket);
            stage.close();
        }
    }
}
