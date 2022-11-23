package prr.core;

public class TerminalBasic extends Terminal {

    public TerminalBasic(String id, Client clientOwner, TerminalMode mode) {
        super(id, clientOwner, mode);
    }

    public TerminalBasic(String id, Client clientOwner) {
        this(id, clientOwner, TerminalMode.ON);
    }

    @Override
    public TerminalType getTerminalType() {
        return TerminalType.BASIC;
    }

    @Override
    public String toString() {
        return "BASIC|" + this.getId() + "|" + this.getOwner().getKey() + "|" + this.getModeToString() +
                "|" + this.getPaymentsFromTerminal() + "|" + this.getDebtFromTerminal() + this.getFriendsToString();
    }
    // terminalType|terminalId|clientId|terminalStatus|balance-paid|balance-debts
}
