package com.geekbrains.geek.chat.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    public TextField msgField;
    @FXML
    public TextArea mainArea;

    private Network network;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        network = new Network((args -> {
            mainArea.appendText((String) args[0]);
        })); //В идеале инициализацию с помощью кнопки
    }

    public void sendMsgAction(ActionEvent actionEvent) {
        network.sendMessage(msgField.getText());
        msgField.clear();
        msgField.requestFocus();
    }
}
