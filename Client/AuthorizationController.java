
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AuthorizationController {
    @FXML
    private Button authorizationButton;
    @FXML
    private TextField login;
    @FXML
    private PasswordField password;
    @FXML
    private Label result;

    public String readLogin(){
        return login.getText();
    }
    public String readPassword(){
        return password.getText();
    }


    public void clickRegistrationButton(){
        Commands commands=new Commands();
        commands.setLogin(readLogin());
        Main.owner=commands.getLogin();
        commands.setPassword(Main.cryptographer(readPassword()));
        commands.setName("checkLogin");
        try {
            commands=sendRecieve(commands);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        result.setText(commands.getResult());
        login.clear();
        password.clear();
    }
    public void clickAuthorizationButton() throws Exception{
        Commands commands=new Commands();
        commands.setPassword(Main.cryptographer(readPassword()));
        Main.owner=login.getText();
        commands.setName("checkUser");
        commands=sendRecieve(commands);
        result.setText(commands.getResult());
        if(result.getText()!=null &&!result.getText().equals("Неверное имя пользователя или пароль. Попробуйте еще раз")){
            Stage stage=(Stage) authorizationButton.getScene().getWindow();
            stage.close();
            Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
            stage.setTitle("Главное окно");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public Commands sendRecieve(Commands commands)throws Exception{
        Main.datagramSocket=new DatagramSocket();
        Main.datagramPacket=new DatagramPacket(Main.c, Main.bufferSize, Main.address, Main.port);
        Main.send(commands, Main.datagramSocket , Main.datagramPacket);
        commands=Main.receive(Main.datagramSocket);
        return commands;
    }

}
