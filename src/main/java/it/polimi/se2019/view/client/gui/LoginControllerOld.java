package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.utils.Utils;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.UUID;

import static it.polimi.se2019.view.client.gui.GUIController.loadFXML;
import static javafx.collections.FXCollections.observableArrayList;

public class LoginControllerOld implements Initializable {

	private Stage window;

	@FXML
	private ComboBox<String> connectionMenuButton;
	@FXML
	private TextField nicknameTextField;
	@FXML
	private Button loginButton;

	@FXML
	private ListView<String> nicknames;

	@FXML
	private TextArea textArea;
	@FXML
	private TextFlow textFlow;

	@FXML
	private Label label;

	@FXML
	private ObservableList<String> obsnicknames;


	@FXML
	private void handleLoginButtonPress() {
		Utils.logInfo("Pressed Login Button.");
		Utils.logInfo("Nickame: " + this.nicknameTextField.getCharacters().toString());
		Utils.logInfo("Connection type: " + this.connectionMenuButton.getValue());
//		Send nickname and open rmi or socket
//		nicknameTextField.getScene().getWindow().hide();

//		Stage stage = GUIController.setSceneTo("Lobby", "Lobby");

		Scene scene = new Scene(loadFXML("Lobby"));
		Stage stage = new Stage();
		stage.setScene(scene);
		ObservableList<String> randomNick = observableArrayList();
		for (int i = 0; i < 4; i++) {
			randomNick.add(UUID.randomUUID().toString().substring(3, 6));
		}
		//Show nicknames stage.
	}

	public void startLogin(Stage window) {
		//Show the login scene.
		Scene scene = new Scene(loadFXML("ConnectionAndNickname"));
		window.setTitle("Adrenalina");
		window.setScene(scene);
		ObservableList<String> options = observableArrayList();
		options.addAll("RMI", "Socket");
		connectionMenuButton = new ComboBox<>();
		connectionMenuButton.setItems(options);
		window.show();
	}


	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}
}
