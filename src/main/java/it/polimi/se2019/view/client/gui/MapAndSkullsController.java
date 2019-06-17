package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.network.message.GameConfigMessage;
import it.polimi.se2019.network.message.MessageSubtype;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

import java.util.List;

import static javafx.collections.FXCollections.observableArrayList;

public class MapAndSkullsController {

	private GUIView guiView;
	private ObservableList<String> mapOptions;

	@FXML
	private Button doneButton;
	@FXML
	private ComboBox<String> mapComboBox;
	@FXML
	private ComboBox<String> skullsComboBox;

	public void setGui(GUIView guiView) {
		this.guiView = guiView;
	}

	public void set(List<String> maps, int minSkulls, int maxSkulls) {
		System.out.println(maps);
		mapOptions = observableArrayList();
		mapOptions.addAll(maps);
		mapComboBox.setItems(mapOptions);
		ObservableList<String> skullsOptions = observableArrayList();
		for (int i = minSkulls; i <= maxSkulls; i++)
			skullsOptions.add(Integer.toString(i));
		skullsComboBox.setItems(skullsOptions);

		System.out.println(maps);
	}

	@FXML
	public void done() {
		String chosenMap = mapComboBox.getSelectionModel().getSelectedItem();
		GameConfigMessage gameConfigMessage = new GameConfigMessage(MessageSubtype.ANSWER);
		gameConfigMessage.setMapIndex(mapOptions.indexOf(chosenMap));
		gameConfigMessage.setSkulls(Integer.parseInt(skullsComboBox.getSelectionModel().getSelectedItem()));
		guiView.sendMessage(gameConfigMessage);
	}
}
