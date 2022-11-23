package prr.core;

import java.io.Serializable;

abstract public class Communications implements Serializable {
    private int _id;
    private boolean _isPaid;
    private Client _clientStartedCom;
    private ClientLevel _clientLevelAtTime;
    private Terminal _from;
    private Terminal _destination;
    private boolean _isOngoing;

    private static final long serialVersionUID = 202208091753L;

    abstract public String getTypeString();

    abstract public String stateToString();

    public void pay(){
        _isPaid = true;
    }
    public Communications(int id, Terminal from, Terminal destination) {
        _id = id;
        _isPaid = false;
        _from = from;
        _clientStartedCom = from.getClient();
        _clientLevelAtTime = _clientStartedCom.getLevel();
        _destination = destination;
    }    
    
    protected ClientLevel getClientLevelAtTime(){
        return _clientLevelAtTime;
    }
    public Terminal getTerminalFrom() {
        return _from;
    }

    public Terminal getTermianlFor() {
        return _destination;
    }

    public Client getClient() {
        return _clientStartedCom;
    }

    public int getId() {
        return _id;
    }

    public boolean isPaid() {
        return _isPaid;
    }

    public boolean isOngoing() {
        return _isOngoing;
    }

    public String toString(){
        Communications c = this;
        return c.getTypeString() + "|" + c.getId() + "|" + c.getTerminalFrom().getId() + "|" + c.getTermianlFor().getId() +
          "|" + c.getSize() + "|" + (int)c.computeCost(new BasicPlan("basic")) + "|" + c.stateToString();
    }

    abstract protected double computeCost(TariffPlan plan);

    abstract protected int getSize();

}
