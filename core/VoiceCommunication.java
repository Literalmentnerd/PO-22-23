package prr.core;

import prr.core.Terminal;

public class VoiceCommunication extends InteractiveCommunication {

    public String getTypeString() {
        return "VOICE";
    }

    public VoiceCommunication(int id, int duration, Terminal from, Terminal to) {
        super(id, duration, from, to);
    }

    public VoiceCommunication(int id, Terminal from, Terminal to) {
        this(id, 0, from, to);
    }

    protected double computeCost(TariffPlan plan) {
        if (this.getState().equals(CommunicationState.ONGOING)){
            return 0;
        }
        else{
            if (this.getTerminalFrom().getFriends().contains(this.getTermianlFor())){
                return plan.computeCost(this.getClient(), this) / 2;
            }
            else{
                return plan.computeCost(this.getClient(), this);
            }
        }
    }
}
