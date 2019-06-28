package it.polimi.se2019.controller;

import it.polimi.se2019.model.Model;
import it.polimi.se2019.utils.GameConstants;
import it.polimi.se2019.view.server.VirtualView;

import java.util.Collection;

public class ControllerDriver extends Controller {

	public ControllerDriver(GameConstants.MapType mapType, Collection<VirtualView> virtualViews, int skulls) {
		super(mapType, virtualViews, skulls);
	}

	public Model getModel() {
		return super.getModel();
	}
}
