package prr.core;

import prr.core.ClientLevel;

public class BasicPlan extends TariffPlan {

    public BasicPlan(String name){
        super(name);
    }

    @Override
    protected double computeCost(Client client, TextCommunication text) {
        if(text.getClientLevelAtTime().equals(ClientLevel.NORMAL)){
            if (text.getSize() >= 100) {
                return 2 * text.getSize();
            } else if (text.getSize() < 50) {
                return 10;
            } else {
                return 16;
            }    
        } else if (text.getClientLevelAtTime().equals(ClientLevel.GOLD)){
            if (text.getSize() >= 100) {
                return 2 * text.getSize();
            } else if (text.getSize() < 50) {
                return 10;
            } else {
                return 10;
            }
        } else {
            if (text.getSize() < 50){
                return 0;
            } else {
                return 4;
            }
        }
    }

    @Override
    protected double computeCost(Client client, VideoCommunication video) {
        if (video.getClientLevelAtTime().equals(ClientLevel.NORMAL)){
            return 30 * video.getSize();
        } else if (video.getClientLevelAtTime().equals(ClientLevel.GOLD)) {return 20 * video.getSize();}
        else {return 10;}
    }

    @Override
    protected double computeCost(Client client, VoiceCommunication voice) {
        if (voice.getClientLevelAtTime().equals(ClientLevel.NORMAL)){
            return 20 * voice.getSize();
        } else {return 10 * voice.getSize();}
    }
}
