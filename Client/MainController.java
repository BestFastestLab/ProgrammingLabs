
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class MainController {
    @FXML
    private Button filterButton;
    @FXML
    private Button add;
    @FXML
    private Button update;
    @FXML
    private Button remove;
    @FXML
    private Button clear;
    @FXML
    private Button addMax;
    @FXML
    private Button removeMaxs;
    @FXML
    private Button greaterGenre;
    @FXML
    private Button exit;
    @FXML
    private Button updateSituation;
    @FXML
    private Label login;
    @FXML
    private Label result;
    @FXML
    private Pane graphicPane;
    @FXML
    private Pane tableViewPane;
    @FXML
    private TextField filterTextField;

    private ObservableList<MusicBand> observableList = FXCollections.observableArrayList();
    private TableView<MusicBand> tableView;

    public void start() throws Exception {
        login.setText(login.getText().split("\\s")[0] +" "+ Main.owner);
        Set<MusicBand> musicBands = getCollection();
        observableList.clear();
        for (MusicBand band : musicBands) {
            observableList.add(band);
        }
        tableView = initTableView();
        tableView.getItems().clear();
        tableView.setItems(observableList);
        ImageView image = new ImageView(new Image(String.valueOf(getClass().getResource("scene.jpg"))));
        image.setRotationAxis(Rotate.Z_AXIS);
        image.setRotate(180);
        image.setFitHeight(graphicPane.getHeight());
        image.setFitWidth(graphicPane.getWidth());
        Platform.runLater(() -> {
            graphicPane.getChildren().removeAll();
            graphicPane.getChildren().clear();
            graphicPane.setRotationAxis(Rotate.Z_AXIS);
            graphicPane.setRotate(180);
            graphicPane.setRotationAxis(Rotate.X_AXIS);
            graphicPane.setRotate(180);
            graphicPane.getChildren().add(image);
            setGraphic(musicBands);
            tableViewPane.getChildren().addAll(tableView);
        });
    }

    public Set getCollection() throws Exception {
        Commands commands = new Commands();
        commands.setName("getCollection");
        Main.send(commands, Main.datagramSocket, Main.datagramPacket);
        commands = Main.receive(Main.datagramSocket);
        Set<MusicBand> musicBands = commands.getSet();
        return musicBands;
    }

    public void setGraphic(Set musicBands) {
        ArrayList<MusicBand> arrayList = new ArrayList<>(musicBands);
        for (MusicBand band : arrayList) {
            Canvas canvasOne = new Canvas(50, 50);
            GraphicsContext gc = canvasOne.getGraphicsContext2D();
            gc.setLineWidth(1);
            try {
                gc.setFill(Color.rgb((band.getOwner().hashCode() * 50)%255, 100, 100));
            } catch (IllegalArgumentException e) {
                gc.setFill(Color.PAPAYAWHIP);
            }
            gc.fillOval(0, 0, band.getNumberOfParticipants() / 10, band.getNumberOfParticipants() / 8);
            canvasOne.setLayoutX(band.getCoordinates().getX());
            canvasOne.setLayoutY(band.getCoordinates().getY());
            canvasOne.setOnMouseClicked(event -> {
                findByCoordinates(arrayList, canvasOne.getLayoutX(), (long) canvasOne.getLayoutY());
            });
            if (canvasOne.getLayoutY() > graphicPane.getHeight()) {
                canvasOne.setVisible(false);
            }
            if (canvasOne.getLayoutX() > graphicPane.getWidth()) {
                canvasOne.setVisible(false);
            }
            Duration duration = Duration.millis(2500);
            TranslateTransition transition = new TranslateTransition(duration, canvasOne);
            transition.setByX(400);
            transition.setByY(50);
            transition.setAutoReverse(true);
            transition.setCycleCount(2);
            transition.play();
            graphicPane.getChildren().addAll(canvasOne);
        }
    }

    public void findByCoordinates(ArrayList<MusicBand> bands, double x, long y) {
        for (MusicBand band : bands) {
            if (band.getCoordinates().getX() == x && band.getCoordinates().getY() == y) {
                try {
                    moveToChangeView(band);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }


    public TableView<MusicBand> initTableView() {
        TableView<MusicBand> objectsTableView = new TableView<>();
        objectsTableView.prefHeightProperty().bind(tableViewPane.heightProperty());
        objectsTableView.prefWidthProperty().bind(tableViewPane.widthProperty());
        TableColumn<MusicBand, Integer> id = new TableColumn<>("id");
        TableColumn<MusicBand, String> name = new TableColumn<>("name");
        TableColumn<MusicBand, Double> xCol = new TableColumn<>("x");
        TableColumn<MusicBand, Long> yCol = new TableColumn<>("y");
        TableColumn<MusicBand, LocalDate> date = new TableColumn<>("date");
        TableColumn<MusicBand, Long> participants = new TableColumn<>("participants");
        TableColumn<MusicBand, Long> singles = new TableColumn<>("singles");
        TableColumn<MusicBand, Long> albums = new TableColumn<>("albums");
        TableColumn<MusicBand, String> genre = new TableColumn<>("genre");
        TableColumn<MusicBand, String> best_album = new TableColumn<>("best album");
        TableColumn<MusicBand, String> owner = new TableColumn<>("owner");

        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        xCol.setCellValueFactory(new PropertyValueFactory<>("x"));
        yCol.setCellValueFactory(new PropertyValueFactory<>("y"));
        date.setCellValueFactory(new PropertyValueFactory<>("creationDate"));
        participants.setCellValueFactory(new PropertyValueFactory<>("numberOfParticipants"));
        singles.setCellValueFactory(new PropertyValueFactory<>("singlesCount"));
        albums.setCellValueFactory(new PropertyValueFactory<>("albumsCount"));
        genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        best_album.setCellValueFactory(new PropertyValueFactory<>("albumsName"));
        owner.setCellValueFactory(new PropertyValueFactory<>("owner"));
        objectsTableView.getColumns().addAll(id, name, xCol, yCol, date, participants, singles, albums, genre, best_album, owner);
        return objectsTableView;
    }

    public void addClick() throws Exception {
        AddController.status = "add";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Add.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void updateClick() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Update.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void removeClick() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Remove.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void clearClick() {
        Commands commands = new Commands();
        commands.setName("clear");
        Main.send(commands, Main.datagramSocket, Main.datagramPacket);
        try {
            commands = Main.receive(Main.datagramSocket);
        } catch (Exception e) {
            e.getMessage();
        }
        result.setText(commands.getResult());
    }

    public void addMaxClick() throws Exception {
        AddController.status = "add_if_max";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Add.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void removeMaxsClick() throws Exception {
        AddController.status = "remove_greater";
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Add.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void greaterGenreClick() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Genre.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void exitClick() {
        System.exit(0);
    }

    public void updateSituationClick() throws Exception {
        start();
    }

    public void filterClick() throws Exception {
        if (!filterTextField.getText().equals("")) {
            Set<MusicBand> set = getCollection();
            observableList.clear();

            for (MusicBand band : set) {
                try {
                    Integer.parseInt(filterTextField.getText());
                    if (band.getName().equals(filterTextField.getText()) || band.getOwner().equals(filterTextField.getText()) || band.getId().equals(Integer.parseInt(filterTextField.getText())) | band.getAlbumsCount().equals(Long.parseLong(filterTextField.getText())) | band.getSinglesCount().equals(Long.parseLong(filterTextField.getText())) | band.getNumberOfParticipants().equals(Long.parseLong(filterTextField.getText())) | band.getY().equals(Long.parseLong(filterTextField.getText())) | band.getAlbumsName().equals(filterTextField.getText()))
                        observableList.add(band);
                } catch (Exception e) {
                    try {
                        Long.parseLong(filterTextField.getText());
                        if (band.getName().equals(filterTextField.getText()) || band.getOwner().equals(filterTextField.getText()) | band.getAlbumsCount().equals(Long.parseLong(filterTextField.getText())) | band.getSinglesCount().equals(Long.parseLong(filterTextField.getText())) | band.getNumberOfParticipants().equals(Long.parseLong(filterTextField.getText())) | band.getY().equals(Long.parseLong(filterTextField.getText())) | band.getAlbumsName().equals(filterTextField.getText()))
                            observableList.add(band);
                    } catch (Exception e1) {
                        try {
                            Double.parseDouble(filterTextField.getText());
                            if (band.getName().equals(filterTextField.getText()) || band.getOwner().equals(filterTextField.getText()) || band.getX().equals(Double.parseDouble(filterTextField.getText())) | band.getAlbumsName().equals(filterTextField.getText()))
                            observableList.add(band);
                        } catch (Exception e2) {
                            try {
                                if (band.getCreationDate().equals(LocalDate.parse(filterTextField.getText())))
                                    observableList.add(band);
                            } catch (Exception e3) {
                                if (band.getAlbumsName().equals(filterTextField.getText()) | band.getName().equals(filterTextField.getText()) | band.getOwner().equals(filterTextField.getText()) | band.getGenre().name().equals(filterTextField.getText()))
                                    observableList.add(band);
                            }
                        }
                    }

                }
            }
            tableView = initTableView();
            tableView.getItems().clear();
            tableView.setItems(observableList);
        } else {
            start();
        }
    }

    public void russianClick() throws Exception {
        updateLanguage("Russian");
        start();
    }

    public void romanianClick() throws Exception {
        updateLanguage("Romanian");
        start();
    }

    public void catalanClick() throws Exception {
        updateLanguage("Catalan");
        start();
    }

    public void englishClick() throws Exception {
        updateLanguage("English");
        start();
    }

    public void moveToChangeView(MusicBand band) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Change.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
        ChangeController changeController = loader.getController();
        changeController.setAlbumsTextField(band.getAlbumsCount());
        changeController.setBestAlbumTextField(band.getBestAlbum());
        changeController.setDateTextField(band.getCreationDate());
        changeController.setGenreTextField(band.getGenre());
        changeController.setIdLabel(band.getId());
        changeController.setNameTextField(band.getName());
        changeController.setOwnerLabel(band.getOwner());
        changeController.setParticipantsTextField(band.getNumberOfParticipants());
        changeController.setSinglesTextField(band.getSinglesCount());
        changeController.setXTextField(band.getCoordinates().getX());
        changeController.setYTextField(band.getCoordinates().getY());

    }

    public void updateLanguage(String language) {
        Locale locale;
        switch (language) {
            case "Russian":
                locale = new Locale("ru", "RU");
                break;
            case "Catalan":
                locale = new Locale("ca", "CA");
                break;
            case "Romanian":
                locale = new Locale("ro", "RO");
                break;
            case "English":
                locale = new Locale("en", "ZA");
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + language);
        }
        ResourceBundle resourceBundle = ResourceBundle.getBundle("Locales", locale);
        add.setText(resourceBundle.getString("add"));
        addMax.setText(resourceBundle.getString("add.max"));
        clear.setText(resourceBundle.getString("clear"));
        removeMaxs.setText(resourceBundle.getString("remove.maxs"));
        greaterGenre.setText(resourceBundle.getString("count.greater.than.genre"));
        remove.setText(resourceBundle.getString("remove"));
        update.setText(resourceBundle.getString("update"));
        exit.setText(resourceBundle.getString("exit"));
        updateSituation.setText(resourceBundle.getString("update.situation"));
        filterButton.setText(resourceBundle.getString("filter"));
        login.setText(resourceBundle.getString("login"));
    }
}
