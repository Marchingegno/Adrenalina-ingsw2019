package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.NicknameMessage;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML controller to display the request of the nickname.
 *
 * @author MarcerAndrea
 */
public class LoginController {

	private static GUIView guiView;
	@FXML
	private Button startButton;
	@FXML
	private TextField nicknameTextField;
	@FXML
	private Label nameLabel;

	public void setGui(GUIView guiView) {
		LoginController.guiView = guiView;
	}

	@FXML
	public void sendNickname() {
		nameLabel.setVisible(false);
		guiView.sendMessage(new NicknameMessage(nicknameTextField.getCharacters().toString(), MessageSubtype.ANSWER));
	}

	void nicknameAlreadyChoosen() {
		nameLabel.setVisible(true);
	}
}
