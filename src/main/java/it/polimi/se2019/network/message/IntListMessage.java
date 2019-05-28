package it.polimi.se2019.network.message;

import java.util.List;

public class IntListMessage extends Message{
    private List<Integer> integerList;

    public IntListMessage(List<Integer> integerList, MessageType messageType, MessageSubtype messageSubtype){
        super(messageType, messageSubtype);
        this.integerList = integerList;
    }

    public List<Integer> getIntegerList() {
        return integerList;
    }
}
