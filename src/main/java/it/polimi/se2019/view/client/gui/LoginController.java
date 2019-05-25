package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.utils.Utils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static it.polimi.se2019.view.client.gui.GUIController.loadFXML;
import static javafx.collections.FXCollections.observableArrayList;

public class LoginController {

	@FXML
	private ComboBox<String> connectionMenuButton;
	@FXML
	private TextField nicknameTextField;
	@FXML
	private Button loginButton;


	@FXML
	private void handleLoginButtonPress() {
		Utils.logInfo("Pressed Login Button.");
		Utils.logInfo("Nickame: " + this.nicknameTextField.getCharacters().toString());
		Utils.logInfo("Connection type: " + this.connectionMenuButton.getValue());
//		Send nickname and open rmi or socket
//		nicknameTextField.getScene().getWindow().hide();
	}

	public void startLogin() {
		try {
			//Show the login scene.
			Scene scene = new Scene(loadFXML("ConnectionAndNickname"));
			Stage stage = new Stage();
			stage.setTitle("Login");
			stage.setScene(scene);
			ObservableList<String> options = observableArrayList("Mah");
			options.addAll("RMI", "Socket");
			List<String> opt = new ArrayList<>();
			opt.add("RMI");
			opt.add("Socket");
			connectionMenuButton = new ComboBox<>();
			connectionMenuButton.setItems(options);
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
