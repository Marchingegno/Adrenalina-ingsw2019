package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.NicknameMessage;
import it.polimi.se2019.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class LoginController {

	private static GUIView guiView;
	@FXML
	private Button startButton;
	@FXML
	private TextField nicknameTextField;

	public void setGui(GUIView guiView) {
		LoginController.guiView = guiView;
	}

	@FXML
	public void sendNickname() {
		Utils.logInfo("Nickname: " + this.nicknameTextField.getCharacters().toString());
		guiView.sendMessage(new NicknameMessage(nicknameTextField.getCharacters().toString(), MessageSubtype.ANSWER));
	}

}
