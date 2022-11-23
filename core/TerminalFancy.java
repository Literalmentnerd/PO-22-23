package prr.core;

public class TerminalFancy extends Terminal {

    public TerminalFancy(String id, Client clientOwner, TerminalMode mode) {
        super(id, clientOwner, mode);
    }

    public TerminalFancy(String id, Client clientOwner) {
        super(id, clientOwner, TerminalMode.ON);
    }

    @Override
    public TerminalType getTerminalType() {
        return TerminalType.FANCY;
    }

    @Override
    public String toString() {
        return "FANCY|" + this.getId() + "|" + this.getOwner().getKey() + "|" + this.getModeToString() +
                "|" + this.getPaymentsFromTerminal() + "|" + this.getDebtFromTerminal() + this.getFriendsToString();
    }

    public InteractiveCommunication makeVideoCall(int id, TerminalFancy to) {
        to.acceptVideoCall(id, this);
        _previousMode = this.getMode();
        _onGoingCommunication = new VideoCommunication(id, this, to);
        this.switchHasOngoing();
        this.setOnBusy();
        to.setOnBusy();
        return _onGoingCommunication;
    }

    public void acceptVideoCall(int id, Terminal from) {
        this.setOnBusy();
        this.switchHasOngoing();
        _onGoingCommunication = new VoiceCommunication(id, from, this);
    }
}
