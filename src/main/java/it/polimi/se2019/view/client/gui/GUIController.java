package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.gamemap.Coordinates;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.utils.QuestionContainer;
import it.polimi.se2019.utils.Utils;
import it.polimi.se2019.view.client.RemoteView;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GUIController extends RemoteView {
	private LoginController loginController;

	static Parent loadFXML(String fxmlName) {
		try {
			return new FXMLLoader(GUIInitializer.class.getResource("/gui/" + fxmlName + ".fxml")).load();
		} catch (IOException e) {
			Utils.logError("Error while loading FXML.", e);
		}
		return null;
	}

	public void startGUI() {
		this.loginController = new LoginController();
		loginController.startLogin();
	}


	static Stage setSceneTo(String fxmlName, String sceneTitle) {
			Scene scene = new Scene(loadFXML(fxmlName));
			Stage stage = new Stage();
			stage.setScene(scene);
			stage.setTitle(sceneTitle);
//			stage.show();
		return stage;
	}


	@Override
	public void updateDisplay() {

	}

	@Override
	public void askForConnectionAndStartIt() {

	}

	@Override
	public void failedConnectionToServer() {

	}

	@Override
	public void lostConnectionToServer() {

	}

	@Override
	public void askNickname() {

	}

	@Override
	public void askNicknameError() {

	}

	@Override
	public void displayWaitingPlayers(List<String> waitingPlayers) {

	}

	@Override
	public void displayTimerStarted(long delayInMs) {

	}

	@Override
	public void displayTimerStopped() {

	}

	@Override
	public void askMapAndSkullsToUse() {

	}

	@Override
	public void showMapAndSkullsInUse(int skulls, GameConstants.MapType mapType) {

	}

	@Override
	public void askAction(boolean activablePowerups, boolean activableWeapons) {

	}

	@Override
	public void askGrabWeapon(List<Integer> indexesOfTheGrabbableWeapons) {

	}

	@Override
	public void askSwapWeapon(List<Integer> indexesOfTheGrabbableWeapons) {

	}

	@Override
	public void askMove(List<Coordinates> reachableCoordinates) {

	}

	@Override
	public void askShoot(List<Integer> shootableWeapons) {

	}

	@Override
	public void askReload() {

	}

	@Override
	public void askSpawn() {

	}

	@Override
	public void askWeaponChoice(QuestionContainer questionContainer) {

	}

	@Override
	public void askPowerupActivation(List<Integer> activablePowerups) {

	}

	@Override
	public void askPowerupChoice(QuestionContainer questionContainer) {

	}

	@Override
	public void askEnd(boolean activablePowerups) {

	}
}
