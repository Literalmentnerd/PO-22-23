package prr.core;

import prr.core.CommunicationState;

abstract public class InteractiveCommunication extends Communications {
    private int _duration;
    private CommunicationState _callState;

    public InteractiveCommunication(int id,int duration, Terminal from, Terminal to) {
        super(id, from, to);
        _callState = CommunicationState.ONGOING;
        _duration = duration;
    }


    public String stateToString(){
        if(_callState.equals(CommunicationState.ONGOING)){
            return "ONGOING";
        }
        return "FINISHED";
    }

    public CommunicationState getState(){
        return _callState;
    }

    public boolean endCommunication() {
        _callState = CommunicationState.FINISHED;
        return true;
    }

    public InteractiveCommunication(int id, Terminal from, Terminal to) {
        super(id, from , to);
        _duration = 0;
    }

    public void setDuration(int duration) {
        _duration = duration;
    }
    
    protected int getSize() {
        return _duration;
    }
}
