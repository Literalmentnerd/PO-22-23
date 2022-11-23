package prr.app.terminal;

import prr.app.exception.UnknownTerminalKeyException;
import prr.core.Network;
import prr.core.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Add a friend.
 */
class DoAddFriend extends TerminalCommand {

  DoAddFriend(Network context, Terminal terminal) {
    super(Label.ADD_FRIEND, context, terminal);
    addStringField("Id", Message.terminalKey());
  }
  
  @Override
  protected final void execute() throws CommandException {
    String id = stringField("Id");
    Terminal t = _network.getTerminalFromId(id);
    if(t == null){
      throw new UnknownTerminalKeyException(id);
    } else if( !(t.getId().equals(_receiver.getId()))){
      _receiver.addFriend(t);    
    }
    
  }
}
