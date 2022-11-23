package prr.core;

import prr.core.CommunicationState;
import prr.core.Terminal;

public class VideoCommunication extends InteractiveCommunication {
    public String getTypeString() {
        return "VIDEO";
    }

    public VideoCommunication(int id, int duration,Terminal from, Terminal to) {
        super(id, duration, from, to);
    }


    public VideoCommunication(int id, Terminal from, Terminal to) {
        this(id, 0,from, to);
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
