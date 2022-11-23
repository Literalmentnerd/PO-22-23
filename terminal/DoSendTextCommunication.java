package prr.app.terminal;

import prr.core.Network;
import prr.core.Terminal;
import prr.app.exception.UnknownTerminalKeyException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
import prr.core.TerminalMode;
//FIXME add more imports if needed

/**
 * Command for sending a text communication.
 */
class DoSendTextCommunication extends TerminalCommand {

  DoSendTextCommunication(Network context, Terminal terminal) {
    super(Label.SEND_TEXT_COMMUNICATION, context, terminal, x -> {return (terminal.getMode().equals(TerminalMode.ON) || terminal.getMode().equals(TerminalMode.SILENCE));});
    addStringField("terminalId", Message.terminalKey());
    addStringField("text",Message.textMessage());
  }
  
  @Override
  protected final void execute() throws CommandException {
    Terminal to = _network.getTerminalFromId(stringField("terminalId"));
    if (to == null){
      throw new UnknownTerminalKeyException(stringField("terminalId"));
    }
    else if (to.getMode().equals(TerminalMode.OFF)){
      if (!to.getAttemptIntCom().contains(_receiver)){
        to.addAttemptedText(_receiver);
      }
      _display.addLine(Message.destinationIsOff(stringField("terminalId")));
      _display.display();
    }
    else if (_receiver.getMode().equals(TerminalMode.OFF)){
      _display.addLine(Message.originIsOff(_receiver.getId()));
      _display.display();
    }
    else if(_receiver.getMode().equals(TerminalMode.BUSY)){
      _display.addLine(Message.originIsBusy(_receiver.getId()));
      _display.display();
    }
    else{
      _network.sendTextCommunication(_receiver, to, stringField("text"));
      _receiver.getClient().incrementConsecutiveText();
      _receiver.getClient().resetConsecutiveVideo();
      _network.refreshClientLevel(_receiver.getClient());
    }
  }
} 
