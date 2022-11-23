package prr.app.terminal;

import prr.core.Network;
import prr.core.Terminal;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for ending communication.
 */
class DoEndInteractiveCommunication extends TerminalCommand {

  DoEndInteractiveCommunication(Network context, Terminal terminal) {
    super(Label.END_INTERACTIVE_COMMUNICATION, context, terminal, x -> {return (terminal.canEndCurrentCommunication() 
        && terminal.getOngoingCommunication().getTerminalFrom().getId().equals(terminal.getId()));});
    addIntegerField("duracao chamada", Message.duration());
  }
  
  @Override
  protected final void execute() throws CommandException {
    if(_receiver.getOngoingCommunication().getTypeString().equals("VIDEO")){
      _receiver.getClient().resetConsecutiveText();
      _receiver.getClient().incrementConsecutiveVideo();
    }
    int price = _receiver.endOngoingCommunication(integerField("duracao chamada"));
    _network.refreshClientLevel(_receiver.getClient());
    _display.addLine(Message.communicationCost(price));
    _display.display();
  }
}
