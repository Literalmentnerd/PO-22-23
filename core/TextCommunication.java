package prr.core;

import prr.core.Terminal;

public class TextCommunication extends Communications {
    private String _message;

    public String getTypeString() {
        return "TEXT";
    }

    public String stateToString() {
        return "FINISHED";
    }

    public TextCommunication(int id,String message, Terminal from, Terminal to) {
        super(id, from, to);
        _message = message;
    }

    @Override
    protected double computeCost(TariffPlan plan) {
        return plan.computeCost(this.getClient(), this);
    }
    @Override
    protected int getSize() {
        return _message.length();
    }
}
