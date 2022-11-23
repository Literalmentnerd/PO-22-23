package prr.app.terminal;

import prr.core.Network;
import prr.core.Terminal;
import prr.core.Communications;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command for showing the ongoing communication.
 */
class DoShowOngoingCommunication extends TerminalCommand {

  DoShowOngoingCommunication(Network context, Terminal terminal) {
    super(Label.SHOW_ONGOING_COMMUNICATION, context, terminal);
  }
  
  @Override
  protected final void execute() throws CommandException {
    if (_receiver.hasOngoingCommunication()){
      _display.addLine(_receiver.getOngoingCommunication().toString());
      _display.display();
    }else{
      _display.addLine(Message.noOngoingCommunication());
      _display.display();
    }
  }
}
