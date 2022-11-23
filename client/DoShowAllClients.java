package prr.app.client;

import prr.core.Network;

import java.util.ArrayList;
import java.util.List;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import prr.core.Client;

class DoShowAllClients extends Command<Network> {

  DoShowAllClients(Network receiver) {
    super(Label.SHOW_ALL_CLIENTS, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
    List<Client> list = new ArrayList<>(_receiver.getClients());
    for (Client c : list) {
      _display.addLine(c.toString());
    }
    _display.display();
  }
}
