package prr.app.lookup;

import prr.core.Network;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import prr.core.Client;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show clients with negative balance.
 */
class DoShowClientsWithDebts extends Command<Network> {

  DoShowClientsWithDebts(Network receiver) {
    super(Label.SHOW_CLIENTS_WITH_DEBTS, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
    List<Client> l = new ArrayList<>(10);
    for (Client c : _receiver.getClients()){
      if (c.getDebt() > 0){
        l.add(c);
      }
    }
    Collections.sort(l , (c1,c2) ->
    {
      if (c1.getDebt()>c2.getDebt()){
        return -1;
      }else if (c1.getDebt()<c2.getDebt()){
        return 1;
      }else {return c1.getKey().compareTo(c2.getKey()); }
    });
    for(Client cli: l){
      _display.addLine(cli);
    }
    _display.display();
  }
}