package prr.app.lookup;

import java.util.ArrayList;
import java.util.Collections;

import prr.core.Client;
import prr.core.Communications;
import prr.core.Terminal;
import prr.core.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME more imports if needed

/**
 * Command for showing all communications.
 */
class DoShowAllCommunications extends Command<Network> {

  DoShowAllCommunications(Network receiver) {
    super(Label.SHOW_ALL_COMMUNICATIONS, receiver);
  }

  @Override
  protected final void execute() throws CommandException {
    ArrayList<Communications> ord = new ArrayList<>();
    for (Client c : _receiver.getClients()) {
      for (Terminal t : c.getAssociatedTerminals()) {
        for (Communications com : t.getCommunicationsMade()) {
          ord.add(com);
        }
      }
    }
    Collections.sort( ord ,(c1,c2) ->
    {
      return c1.getId() - c2.getId();
    });
    for(Communications p: ord){
      _display.addLine(p.toString());
    }
    _display.display();
  }
}