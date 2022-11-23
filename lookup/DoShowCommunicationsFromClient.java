package prr.app.lookup;

import prr.core.Network;
import pt.tecnico.uilib.menus.Command;

import java.util.ArrayList;
import java.util.Collections;

import prr.core.Client;
import prr.core.Communications;
import pt.tecnico.uilib.menus.CommandException;
import prr.core.Terminal;

/**
 * Show communications from a client.
 */
class DoShowCommunicationsFromClient extends Command<Network> {

  DoShowCommunicationsFromClient(Network receiver) {
    super(Label.SHOW_COMMUNICATIONS_FROM_CLIENT, receiver);
    addStringField("clientId", Message.clientKey());
  }

  @Override
  protected final void execute() throws CommandException {
    Client c = _receiver.getClientFromKey(stringField("clientId"));
    ArrayList<Communications> l = new ArrayList<>();
    for(Terminal t : c.getAssociatedTerminals()){
      for (Communications com : t.getCommunicationsMade()){
        l.add(com);
      }
    }
    Collections.sort(l, (c1,c2)->{
      return c1.getId()-c2.getId();
    });
    for (Communications com : l){
      _display.addLine(com.toString());
    }
    _display.display();
  }
}
