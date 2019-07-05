package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
import it.polimi.se2019.network.message.IntMessage;
import it.polimi.se2019.network.message.MessageSubtype;
import it.polimi.se2019.network.message.MessageType;
import it.polimi.se2019.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.util.List;

/**
 * FXML controller to display
 *
 * @author MarcerAndrea
 */
public class PowerupChoiceController {
    @FXML
    private ImageView powerup0;
    @FXML
    private ImageView powerup1;
    @FXML
    private ImageView powerup2;
    @FXML
    private ImageView powerup3;
    @FXML
    private Button powerupButton0;
    @FXML
    private Button powerupButton1;
    @FXML
    private Button powerupButton2;
    @FXML
    private Button powerupButton3;
    @FXML
    private Label title;
    @FXML
    private Button button;

    private Request request;
    private GUIView guiView;
    private Stage stage;
    private int answer;

    public void setPowerups(List<PowerupCardRep> powerups) {
        if (!powerups.isEmpty()) {
            powerup0.setImage(loadImage(powerups.get(0).getImagePath()));
            powerup0.setVisible(true);
        } else {
            powerup0.setVisible(false);
        }

        if (powerups.size() >= 2) {
            powerup1.setImage(loadImage(powerups.get(1).getImagePath()));
            powerup1.setVisible(true);
        } else {
            powerup1.setVisible(false);
        }

        if (powerups.size() >= 3) {
            powerup2.setImage(loadImage(powerups.get(2).getImagePath()));
            powerup2.setVisible(true);
        } else {
            powerup2.setVisible(false);
        }

        if (powerups.size() >= 4) {
            powerup3.setImage(loadImage(powerups.get(3).getImagePath()));
            powerup3.setVisible(true);
        } else {
            powerup3.setVisible(false);
        }
    }

    void setTitle(String title) {
        this.title.setText(title);
    }

    void activatePowerupsButtons(List<Integer> powerupsToActivate) {
        if (powerupsToActivate.contains(0)) {
            powerupButton0.setVisible(true);
            powerupButton0.setDisable(false);
        } else {
            powerupButton0.setVisible(false);
            powerupButton0.setDisable(true);
        }

        if (powerupsToActivate.contains(1)) {
            powerupButton1.setVisible(true);
            powerupButton1.setDisable(false);
        } else {
            powerupButton1.setVisible(false);
            powerupButton1.setDisable(true);
        }
        if (powerupsToActivate.contains(2)) {
            powerupButton2.setVisible(true);
            powerupButton2.setDisable(false);
        } else {
            powerupButton2.setVisible(false);
            powerupButton2.setDisable(true);
        }
        if (powerupsToActivate.contains(3)) {
            powerupButton3.setVisible(true);
            powerupButton3.setDisable(false);
        } else {
            powerupButton3.setVisible(false);
        }

    }

    @FXML
    public void buttonPressed() {
        button.setDisable(true);
        button.setVisible(false);
        answer = -1;
        stage.close();
    }

    @FXML
    public void powerupButton0Pressed() {
        if (request == Request.CHOOSE_INT)
            answer = 0;
        else
            guiView.sendMessage(new IntMessage(0, MessageType.SPAWN, MessageSubtype.ANSWER));
        stage.close();
    }

    @FXML
    public void powerupButton1Pressed() {
        if (request == Request.CHOOSE_INT)
            answer = 1;
        else
            guiView.sendMessage(new IntMessage(1, MessageType.SPAWN, MessageSubtype.ANSWER));
        stage.close();
    }

    @FXML
    public void powerupButton2Pressed() {
        if (request == Request.CHOOSE_INT)
            answer = 2;
        else
            guiView.sendMessage(new IntMessage(2, MessageType.SPAWN, MessageSubtype.ANSWER));
        stage.close();
    }

    @FXML
    void powerupButton3Pressed() {
        if (request == Request.CHOOSE_INT)
            answer = 3;
        else
            guiView.sendMessage(new IntMessage(2, MessageType.SPAWN, MessageSubtype.ANSWER));
        stage.close();
    }

    void setGuiAndStage(GUIView guiView, Stage stage) {
        this.stage = stage;
        this.guiView = guiView;
    }

    void activateNoPowerupButton(boolean active) {
        button.setVisible(active);
        button.setDisable(!active);
    }

    int askChoice() {
        this.request = Request.CHOOSE_INT;
        stage.showAndWait();
        this.request = null;
        return answer;
    }

    private Image loadImage(String filePath) {
        Utils.logInfo(getClass().getName() + "-> loadImage(): /graphicassets/" + filePath + ".png");
        return new Image(getClass().getResource("/graphicassets/powerups/" + filePath + ".png").toString());
    }
}
