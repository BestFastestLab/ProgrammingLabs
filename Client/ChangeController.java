import javafx.fxml.FXML;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.stage.*;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChangeController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextField xTextField;
    @FXML
    private TextField yTextField;
    @FXML
    private Label dateTextField;
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
    private Button updateButton;
    @FXML
    private Button returnButton;
    @FXML
    private Label idLabel;
    @FXML
    private Label ownerLabel;
    @FXML
    private Label result;


    public void saveChangingClick() throws Exception {
        MusicBand band = new MusicBand();
        Coordinates coordinates = new Coordinates();
        Album album = new Album();
        band.setId(Integer.parseInt(idLabel.getText()));
        band.setCreationDate(LocalDate.parse(dateTextField.getText()));
        boolean flag = true;
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
                album.setName(bestAlbumTextField.getText());
                band.setBestAlbum(album);
            } else {
                result.setText("Некоторые поля заполнены ошибочно!");
                flag = false;
            }
        } catch (Exception e) {
            result.setText("Некоторые поля заполнены ошибочно!");
            flag = false;
        }
        if (flag) {

            Commands commands = new Commands();
            commands.setBand(band);
            commands.setName("clickUpdate");
            Main.send(commands, Main.datagramSocket, Main.datagramPacket);
            commands = Main.receive(Main.datagramSocket);
            Stage stage = (Stage) updateButton.getScene().getWindow();
            stage.close();
        }
    }

    public void returnClick() {
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close();
    }

    public void setNameTextField(String nameTextField) {
        this.nameTextField.setText(nameTextField);
    }

    public void setXTextField(Double x) {
        this.xTextField.setText(x.toString());
    }

    public void setYTextField(Long y) {
        this.yTextField.setText(y.toString());
    }

    public void setDateTextField(LocalDate date) {
        this.dateTextField.setText(date.toString());
    }

    public void setParticipantsTextField(Long participants) {
        this.participantsTextField.setText(participants.toString());
    }

    public void setSinglesTextField(Long singles) {
        this.singlesTextField.setText(singles.toString());
    }

    public void setAlbumsTextField(Long albums) {
        this.albumsTextField.setText(albums.toString());
    }

    public void setGenreTextField(MusicGenre genre) {
        this.genreTextField.setText(genre.toString());
    }

    public void setBestAlbumTextField(Album album) {
        this.bestAlbumTextField.setText(album.getName());
    }

    public void setIdLabel(Integer id) {
        this.idLabel.setText(id.toString());
    }

    public void setOwnerLabel(String owner) {
        this.ownerLabel.setText(owner);
    }
}
