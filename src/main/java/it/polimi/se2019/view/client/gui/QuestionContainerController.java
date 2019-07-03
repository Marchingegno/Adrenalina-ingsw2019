package it.polimi.se2019.view.client.gui;


import it.polimi.se2019.network.message.IntMessage;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.QuestionContainer;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import static javafx.collections.FXCollections.observableArrayList;

/**
 * FXML controller to display a question container.
 *
 * @author MarcerAndrea
 */
public class QuestionContainerController {
	@FXML
	private Label string;
	@FXML
	private ComboBox<String> choiceComboBox;
	@FXML
	private Button doneButton;

	private GUIView guiView;
	private Stage stage;
	private MessageType answerMessageType;
	private ObservableList<String> options;

	@FXML
	private void pressedDone() {
		stage.close();
		guiView.sendMessage(new IntMessage(options.indexOf(choiceComboBox.getSelectionModel().getSelectedItem()), answerMessageType, MessageSubtype.ANSWER));
	}

	void setAskString(QuestionContainer questionContainer, MessageType messageType) {
		answerMessageType = messageType;
		string.setText(questionContainer.getQuestion());
		options = observableArrayList();
		options.addAll(questionContainer.getOptions());
		choiceComboBox.setItems(options);
		choiceComboBox.setValue(options.get(0));
	}

	void setGuiAndStage(GUIView guiView, Stage stage) {
		this.guiView = guiView;
		this.stage = stage;
	}
}
