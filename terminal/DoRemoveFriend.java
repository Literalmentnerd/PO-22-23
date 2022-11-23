package prr.app.terminal;

import prr.app.exception.UnknownTerminalKeyException;
import prr.core.Network;
import prr.core.Terminal;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Remove friend.
 */
class DoRemoveFriend extends TerminalCommand {

  DoRemoveFriend(Network context, Terminal terminal) {
    super(Label.REMOVE_FRIEND, context, terminal);
    addStringField("Id", Message.terminalKey());
  }
  
  @Override
  protected final void execute() throws CommandException {
    String id = stringField("Id");
    Terminal t =  _network.getTerminalFromId(id);
    if(t == null){
      throw new UnknownTerminalKeyException(id);
    }
    _receiver.removeFriend(t);
  }
}
