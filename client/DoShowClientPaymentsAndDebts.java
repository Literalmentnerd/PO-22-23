package prr.app.client;

import prr.core.Client;
import prr.core.Network;
import prr.app.exception.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show the payments and debts of a client.
 */
class DoShowClientPaymentsAndDebts extends Command<Network> {

  DoShowClientPaymentsAndDebts(Network receiver) {
    super(Label.SHOW_CLIENT_BALANCE, receiver);
    addStringField("clientId", Message.key());
  }

  @Override
  protected final void execute() throws CommandException {
    Client c = _receiver.getClientFromKey(stringField("clientId"));
    if (!c.equals(null)) {
      _display.addLine(Message.clientPaymentsAndDebts(stringField("clientId"), c.getPayments(), c.getDebt()));
      _display.display();
    } else {
      throw new UnknownClientKeyException(stringField("clientId"));
    }
  }
}