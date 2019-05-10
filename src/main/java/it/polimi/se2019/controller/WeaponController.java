package it.polimi.se2019.controller;

import it.polimi.se2019.model.player.Player;
import it.polimi.se2019.utils.Pair;
import it.polimi.se2019.view.server.Event;
import it.polimi.se2019.view.server.VirtualView;


public class WeaponController {

	public void processFiring(Player player, Event event){
		VirtualView virtualView = event.getVirtualView();
		Pair intString = player.getFiringWeapon().handleFire();
		virtualView.askWeapon(intString);
	}
}
