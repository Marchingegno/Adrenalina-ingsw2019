package it.polimi.se2019.network.message;

import it.polimi.se2019.model.cards.ammo.AmmoType;

import java.util.List;

public class PaymentMessage extends Message {
    private List<AmmoType> priceToPay;

    public PaymentMessage(List<AmmoType> priceToPay){
        super(MessageType.PAYMENT, MessageSubtype.REQUEST);
        this.priceToPay = priceToPay;
    }

    public List<AmmoType> getPriceToPay() {
        return priceToPay;
    }
}
