package it.polimi.se2019.view.client.gui;

import it.polimi.se2019.model.cards.ammo.AmmoType;
import it.polimi.se2019.model.cards.powerups.PowerupCardRep;
import it.polimi.se2019.model.cards.weapons.WeaponRep;
import it.polimi.se2019.model.player.PlayerRep;
import it.polimi.se2019.utils.Color;
import it.polimi.se2019.utils.Utils;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerInventoryController {
    @FXML
    private ImageView playerBoard;

    @FXML
    private ImageView weapon0;
    @FXML
    private ImageView weapon1;
    @FXML
    private ImageView weapon2;
    @FXML
    private ImageView powerup0;
    @FXML
    private ImageView powerup1;
    @FXML
    private ImageView powerup2;

    @FXML
    private GridPane ammoContainer;
    @FXML
    private ImageView blueAmmo0;
    @FXML
    private ImageView blueAmmo1;
    @FXML
    private ImageView blueAmmo2;
    @FXML
    private ImageView redAmmo0;
    @FXML
    private ImageView redAmmo1;
    @FXML
    private ImageView redAmmo2;
    @FXML
    private ImageView yellowAmmo0;
    @FXML
    private ImageView yellowAmmo1;
    @FXML
    private ImageView yellowAmmo2;


    private List<ImageView> marksTokens = new ArrayList<>();
    @FXML
    private ImageView damageToken0;
    @FXML
    private ImageView damageToken1;
    @FXML
    private ImageView damageToken2;
    @FXML
    private ImageView damageToken3;
    @FXML
    private ImageView damageToken4;
    @FXML
    private ImageView damageToken5;
    @FXML
    private ImageView damageToken6;
    @FXML
    private ImageView damageToken7;
    @FXML
    private ImageView damageToken8;
    @FXML
    private ImageView damageToken9;
    @FXML
    private ImageView damageToken10;
    @FXML
    private ImageView damageToken11;

    private List<ImageView> marks = new ArrayList<>();

    @FXML
    private ImageView marks00;
    @FXML
    private ImageView marks01;
    @FXML
    private ImageView marks02;
    @FXML
    private ImageView marks10;
    @FXML
    private ImageView marks11;
    @FXML
    private ImageView marks12;
    @FXML
    private ImageView marks20;
    @FXML
    private ImageView marks21;
    @FXML
    private ImageView marks22;
    @FXML
    private ImageView marks30;
    @FXML
    private ImageView marks31;
    @FXML
    private ImageView marks32;
    @FXML
    private ImageView marks40;
    @FXML
    private ImageView marks41;
    @FXML
    private ImageView marks42;


    @FXML
    private ImageView skull0;
    @FXML
    private ImageView skull1;
    @FXML
    private ImageView skull2;
    @FXML
    private ImageView skull3;

    @FXML
    private Circle loaded0;
    @FXML
    private Circle loaded1;
    @FXML
    private Circle loaded2;

    @FXML
    private Label nickname;

    @FXML
    private Label points;


    public void setInventory(PlayerRep playerRep) {

        marks.add(marks00);
        marks.add(marks01);
        marks.add(marks02);
        marks.add(marks10);
        marks.add(marks11);
        marks.add(marks12);
        marks.add(marks20);
        marks.add(marks21);
        marks.add(marks22);
        marks.add(marks30);
        marks.add(marks31);
        marks.add(marks32);
        marks.add(marks40);
        marks.add(marks41);
        marks.add(marks42);

        marksTokens.add(damageToken0);
        marksTokens.add(damageToken1);
        marksTokens.add(damageToken2);
        marksTokens.add(damageToken3);
        marksTokens.add(damageToken4);
        marksTokens.add(damageToken5);
        marksTokens.add(damageToken6);
        marksTokens.add(damageToken7);
        marksTokens.add(damageToken8);
        marksTokens.add(damageToken9);
        marksTokens.add(damageToken10);
        marksTokens.add(damageToken11);
        setNickname(playerRep.getPlayerName());
        setPoints(playerRep.getPoints());
        setPlayerBoard(playerRep.getPgName(), false); //TODO set correct player board if in frenzy
        setAmmoContainer(playerRep);
        setDamageToken(playerRep.getDamageBoard());
        setMarks(playerRep.getMarks());

        List<String> weaponsPath = new ArrayList<>();
        for (WeaponRep weaponRep : playerRep.getWeaponReps()) {
            weaponsPath.add("weapons/" + weaponRep.getImagePath());
        }
        setWeapons(playerRep.getWeaponReps());

        List<String> powerupsPath = new ArrayList<>();
        if (!playerRep.isHidden()) {
            for (PowerupCardRep powerupCardRep : playerRep.getPowerupCards()) {
                powerupsPath.add("powerups/" + powerupCardRep.getImagePath());
            }
        }
        setPowerups(powerupsPath);

    }

    private void setNickname(String nickname) {
        this.nickname.setText(nickname);
    }

    private void setPoints(int points) {
        this.points.setText(Integer.toString(points));
    }

    private void setPlayerBoard(String characterName, boolean isFrenzy) {
        String pathImage = "playerBoards/" + characterName + "/" + (isFrenzy ? "frenzy" : "normal") + "Board";
        playerBoard.setImage(loadImage(pathImage));
    }

    public void setWeapons(List<WeaponRep> weaponReps) {
        List<String> weaponsPath = new ArrayList<>();
        for (WeaponRep weaponRep : weaponReps) {
            weaponsPath.add("weapons/" + weaponRep.getImagePath());
        }
        if (!weaponsPath.isEmpty()) {
            weapon0.setImage(loadImage(weaponsPath.get(0)));
            weapon0.setVisible(true);
            loaded0.setVisible(weaponReps.get(0).isLoaded());
        } else {
            weapon0.setVisible(false);
            loaded0.setVisible(false);
        }
        if (weaponsPath.size() >= 2) {
            weapon1.setImage(loadImage(weaponsPath.get(1)));
            weapon1.setVisible(true);
            loaded1.setVisible(weaponReps.get(1).isLoaded());
        } else {
            loaded1.setVisible(false);
            weapon1.setVisible(false);
        }
        if (weaponsPath.size() >= 3) {
            weapon2.setImage(loadImage(weaponsPath.get(2)));
            weapon2.setVisible(true);
            loaded2.setVisible(weaponReps.get(2).isLoaded());
        } else{
            loaded2.setVisible(false);
            weapon2.setVisible(false);
        }
    }

    public void setPowerups(List<String> powerupsPath) {
        if (powerupsPath.size() >= 3) {
            powerup2.setImage(loadImage(powerupsPath.get(2)));
            powerup2.setVisible(true);
        } else powerup2.setVisible(false);
        if (powerupsPath.size() >= 2) {
            powerup1.setImage(loadImage(powerupsPath.get(1)));
            powerup1.setVisible(true);
        } else powerup1.setVisible(false);
        if (!powerupsPath.isEmpty()) {
            powerup0.setImage(loadImage(powerupsPath.get(0)));
            powerup0.setVisible(true);
        } else powerup0.setVisible(false);
    }

    public void setAmmoContainer(PlayerRep playerRep) {
        blueAmmo0.setVisible(playerRep.getAmmo(AmmoType.BLUE_AMMO) > 0);
        blueAmmo1.setVisible(playerRep.getAmmo(AmmoType.BLUE_AMMO) > 1);
        blueAmmo2.setVisible(playerRep.getAmmo(AmmoType.BLUE_AMMO) > 2);
        redAmmo0.setVisible(playerRep.getAmmo(AmmoType.RED_AMMO) > 0);
        redAmmo1.setVisible(playerRep.getAmmo(AmmoType.RED_AMMO) > 1);
        redAmmo2.setVisible(playerRep.getAmmo(AmmoType.RED_AMMO) > 2);
        yellowAmmo0.setVisible(playerRep.getAmmo(AmmoType.YELLOW_AMMO) > 0);
        yellowAmmo1.setVisible(playerRep.getAmmo(AmmoType.YELLOW_AMMO) > 1);
        yellowAmmo2.setVisible(playerRep.getAmmo(AmmoType.YELLOW_AMMO) > 2);
    }

    public void setDamageToken(List<Color.CharacterColorType> damageBoard) {
        Utils.logInfo("PlayerInventoryController -> setDamageToken(): damageBoard size " + damageBoard.size());
        for (int i = 0; i < 12; i++) {
            if (i < damageBoard.size()) {
                (marksTokens.get(i)).setImage(loadImage("playerBoards/" + damageBoard.get(i).getPgName() + "/token"));
                marksTokens.get(i).setVisible(true);
            } else
                marksTokens.get(i).setVisible(false);
        }
    }

    public void setMarks(List<Color.CharacterColorType> markList) {
        Map<String, Integer> marksTokens = new HashMap<>();
        for (ImageView mark : marks) {
            mark.setVisible(false);
        }

        for (Color.CharacterColorType color : markList) {
            if (marksTokens.containsKey(color.getPgName()))
                marksTokens.replace(color.getPgName(), marksTokens.get(color.getPgName()) + 1);
            else
                marksTokens.put(color.getPgName(), 1);
        }

        for (String pgName : marksTokens.keySet()) {
            switch (pgName) {
                case ("sprog"):
                    if (marksTokens.containsKey(pgName)) {
                        if (marksTokens.get(pgName) >= 1) {
                            marks00.setVisible(true);
                            marks00.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                        if (marksTokens.get(pgName) >= 2) {
                            marks01.setVisible(true);
                            marks01.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }

                        if (marksTokens.get(pgName) >= 3) {
                            marks02.setVisible(true);
                            marks02.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                    }
                    break;
                case ("banshee"):
                    if (marksTokens.containsKey(pgName)) {
                        if (marksTokens.get(pgName) >= 1) {
                            marks10.setVisible(true);
                            marks10.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                        if (marksTokens.get(pgName) >= 2) {
                            marks11.setVisible(true);
                            marks11.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }

                        if (marksTokens.get(pgName) >= 3) {
                            marks12.setVisible(true);
                            marks12.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                    }
                    break;
                case ("dozer"):
                    if (marksTokens.containsKey(pgName)) {
                        if (marksTokens.get(pgName) >= 1) {
                            marks20.setVisible(true);
                            marks20.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                        if (marksTokens.get(pgName) >= 2) {
                            marks21.setVisible(true);
                            marks21.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }

                        if (marksTokens.get(pgName) >= 3) {
                            marks22.setVisible(true);
                            marks22.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                    }
                    break;
                case ("destructor"):
                    if (marksTokens.containsKey(pgName)) {
                        if (marksTokens.get(pgName) >= 1) {
                            marks30.setVisible(true);
                            marks30.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                        if (marksTokens.get(pgName) >= 2) {
                            marks31.setVisible(true);
                            marks31.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }

                        if (marksTokens.get(pgName) >= 3) {
                            marks32.setVisible(true);
                            marks32.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                    }
                    break;
                case ("violet"):
                    if (marksTokens.containsKey(pgName)) {
                        if (marksTokens.get(pgName) >= 1) {
                            marks40.setVisible(true);
                            marks40.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                        if (marksTokens.get(pgName) >= 2) {
                            marks41.setVisible(true);
                            marks41.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }

                        if (marksTokens.get(pgName) >= 3) {
                            marks42.setVisible(true);
                            marks42.setImage(loadImage("playerboards/" + pgName + "/token"));
                        }
                    }
                    break;
            }
        }
    }

    public void setSkulls() {

    }

    private Image loadImage(String filePath) {
        System.out.println("Loading: " + "/graphicassets/" + filePath + ".png");
        return new Image(getClass().getResource("/graphicassets/" + filePath + ".png").toString());
    }
}
