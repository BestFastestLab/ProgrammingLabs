
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.stage.*;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main extends Application {
    public static InetAddress address;
    public static int port;
    public static String owner;
    final static int bufferSize = 32768;
    static byte[] b = new byte[bufferSize];
    static byte[] c = new byte[bufferSize];
    final static int timeout = 5000;
    public static DatagramSocket datagramSocket;
    public static DatagramPacket datagramPacket;
    private static ExecutorService cashedThread= Executors.newCachedThreadPool();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Port.fxml"));
        primaryStage.setTitle("Ввод порта");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    public static void send(Commands command, DatagramSocket datagramSocket, DatagramPacket datagramPacket) {
        cashedThread.submit(()->{
            try {
                command.setLogin(owner);
                b = serialize(command);
                System.arraycopy(b, 0, c, 0, b.length);
                datagramSocket.send(datagramPacket);
            } catch (Exception e){
                System.out.println("Неверный адрес");
            }
        });
    }

    public static Commands receive(DatagramSocket datagramSocket) throws Exception {
        datagramSocket.setSoTimeout(timeout);
        //отправили, стараемся получить назад
        Commands commands = new Commands();
        try {
            DatagramPacket received = new DatagramPacket(c, c.length);
            datagramSocket.receive(received);
            commands = (Commands) deserialize(c);
            Arrays.fill(c, (byte) 0);
        } catch (SocketTimeoutException e) {
            Parent root = FXMLLoader.load(Main.class.getResource("Connection.fxml"));
            Stage stage1=new Stage();
            stage1.setTitle("Connection failed");
            stage1.setScene(new Scene(root));
            stage1.show();
        }
        return commands;
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream b = new ByteArrayInputStream(bytes)) {
            try (ObjectInputStream o = new ObjectInputStream(b)) {
                return o.readObject();
            }
        }
    }

    public static byte[] serialize(Commands command) throws IOException {
        try (ByteArrayOutputStream b = new ByteArrayOutputStream()) {
            try (ObjectOutputStream o = new ObjectOutputStream(b)) {
                o.writeObject(command);
            }
            return b.toByteArray();
        }
    }

    public static byte[] cryptographer(String password) {
        byte[] encryptedPassword = new byte[10000];
        try {
            MessageDigest digest = MessageDigest.getInstance("MD2");
            digest.update(password.getBytes(StandardCharsets.UTF_8));
            encryptedPassword = digest.digest();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return encryptedPassword;
    }
    /*public static Commands creationTime(Commands command, DatagramSocket datagramSocket, DatagramPacket datagramPacket) throws Exception {
        if (command.getResult() != null && command.getResult().equals("It's creation time!")) {
            command.setBand(UsersInput.creatingNewBand(command.getBandName()));
            send(command, datagramSocket, datagramPacket);
            command = receive(datagramSocket);
        }
        return command;
    }

    public static Commands resultTreatment(Commands command, DatagramSocket datagramSocket, DatagramPacket datagramPacket) throws Exception {
        command = creationTime(command, datagramSocket, datagramPacket);
        if (command.getResult() != null && command.getResult().equals("exit")) {
            System.exit(0);
        }
        if (command.getMasOfCommands().size() != 0) {
            for (Commands s : command.getMasOfCommands()) {
                if (s.getName() != null) {
                    s.setResult(null);
                    send(s, datagramSocket, datagramPacket);
                    command = resultTreatment(receive(datagramSocket), datagramSocket, datagramPacket);
                    System.out.println(command.getResult());
                    System.out.println(" ");
                } else {
                    System.out.println(s.getResult());
                }
            }
            command.setResult("Скрипт выполнен!");
        }
        if (command.getResult().equals("Waiting for name")) {
            System.out.println("Введите имя объекта");
            while (command.getBandName() == null) {
                command.setBandName(inputString());
            }
            command.setName("add");
            send(command, datagramSocket, datagramPacket);
            command = receive(datagramSocket);
            command = creationTime(command, datagramSocket, datagramPacket);
        }
        return command;
    }*/


}
