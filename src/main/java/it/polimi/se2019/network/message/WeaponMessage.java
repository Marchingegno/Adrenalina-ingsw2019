package it.polimi.se2019.network.message;

import it.polimi.se2019.utils.Pair;

public class WeaponMessage extends IntMessage {

	private String string;

	public WeaponMessage(Pair intString, MessageSubtype messageSubtype) {
		super((int)intString.getFirst(), MessageType.WEAPON, messageSubtype);
		this.string = (String)intString.getSecond();
	}

	public String getString() {
		return string;
	}
}
