package prr.app.lookup;

import prr.core.Network;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

import java.util.ArrayList;

import prr.core.Client;
import java.util.Collections;
import prr.core.Communications;
import prr.core.Terminal;
//FIXME add more imports if needed

/**
 * Show communications to a client.
 */
class DoShowCommunicationsToClient extends Command<Network> {

  DoShowCommunicationsToClient(Network receiver) {
    super(Label.SHOW_COMMUNICATIONS_TO_CLIENT, receiver);
    addStringField("clientId", Message.clientKey());
  }

  @Override
  protected final void execute() throws CommandException {
    ArrayList<Communications> l = new ArrayList<>();
    Client c = _receiver.getClientFromKey(stringField("clientId"));
    for(Terminal t : c.getAssociatedTerminals()){
      for (Communications com : t.getComminciationsReceived()){
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

