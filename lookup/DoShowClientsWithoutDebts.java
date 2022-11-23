package prr.app.lookup;

import prr.core.Network;

import java.util.ArrayList;
import java.util.List;

import prr.core.Notification;

import prr.core.Client;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show clients with positive balance.
 */
class DoShowClientsWithoutDebts extends Command<Network> {

  DoShowClientsWithoutDebts(Network receiver) {
    super(Label.SHOW_CLIENTS_WITHOUT_DEBTS, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
    for (Client c: _receiver.getClients()){
      if (c.getDebt() == 0){
        _display.addLine(c.toString());
        for(Notification n: c.getNotifications()){
          _display.addLine(n.toString());
        }
      }
    }
    _display.display();
  }
}
