package prr.core;

import java.io.Serializable;

import prr.core.ClientLevel;

abstract public class TariffPlan implements Serializable {
    private String _name;

    public TariffPlan(String name){
        _name = name;
    }

    private static final long serialVersionUID = 202208091753L;

    abstract protected double computeCost(Client client, TextCommunication text);

    abstract protected double computeCost(Client client, VoiceCommunication voice);

    abstract protected double computeCost(Client client, VideoCommunication video);

}
