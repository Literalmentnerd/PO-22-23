package prr.app.terminal;

import prr.core.Network;
import prr.core.Terminal;
import prr.core.TerminalFancy;
import prr.core.TerminalMode;
import prr.core.TerminalType;

import java.net.DatagramSocketImplFactory;


import prr.app.exception.UnknownTerminalKeyException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Command for starting communication.
 */
class DoStartInteractiveCommunication extends TerminalCommand {

  DoStartInteractiveCommunication(Network context, Terminal terminal) {
    super(Label.START_INTERACTIVE_COMMUNICATION, context, terminal, x -> {return terminal.canStartCommunication();});
    addStringField("terminalId", Message.terminalKey());
  }
  
  @Override
  protected final void execute() throws CommandException {
    Form form = new Form();
    String comTypeString;
    while (true) {
      form.addStringField("comKey", Message.commType());
      form.parse();
      if (form.stringField("comKey").equals("VOICE")) {
        comTypeString = form.stringField("comKey");
        form.clear();
        break;
      } else if (form.stringField("comKey").equals("VIDEO")) {
        comTypeString = form.stringField("comKey");
        form.clear();
        break;
      }
      form.clear();
    }
    Terminal t = _network.getTerminalFromId(stringField("terminalId"));
    if (t == null){
      throw new UnknownTerminalKeyException(stringField("terminalId"));
    }
    else{
      if(_receiver.getMode().equals(TerminalMode.OFF)){
        _display.addLine(Message.originIsOff(_receiver.getId()));
        _display.display();
      }
      else if (_receiver.getMode().equals(TerminalMode.BUSY)){
        _display.addLine(Message.originIsBusy(_receiver.getId()));
        _display.display();
      }
      else if (comTypeString.equals("VOICE")){
        if(t.getMode().equals(TerminalMode.OFF)){
          _display.addLine(Message.destinationIsOff(stringField("terminalId")));
          _display.display();
          t.addAttemptedInter(_receiver);
        }
        else if (t.getMode().equals(TerminalMode.BUSY)){
          _display.addLine(Message.destinationIsBusy(stringField("terminalId")));
          _display.display();
          t.addAttemptedInter(_receiver);   
        }
        else if (t.getMode().equals(TerminalMode.SILENCE)){
          _display.addLine(Message.destinationIsSilent(stringField("terminalId")));
          _display.display();
          t.addAttemptedInter(_receiver);
        }
        else{
          _network.startVoiceCall(_receiver, t);
        }
      }
      else{
        if (!_receiver.getTerminalType().equals(TerminalType.FANCY)){
          _display.addLine(Message.unsupportedAtOrigin(_receiver.getId(), "VIDEO"));
          _display.display();
        }
        else if (!t.getTerminalType().equals(TerminalType.FANCY)){
          _display.addLine(Message.unsupportedAtDestination(t.getId(), "VIDEO"));
          _display.display();
        }
        else{
          TerminalFancy receiver = (TerminalFancy)_receiver;
          TerminalFancy terminal = (TerminalFancy)_network.getTerminalFromId(stringField("terminalId"));
          if(terminal.getMode().equals(TerminalMode.OFF)){
            _display.addLine(Message.destinationIsOff(terminal.getId()));
            _display.display();
            t.addAttemptedInter(receiver);   
            
          }
          else if (terminal.getMode().equals(TerminalMode.BUSY)){
            _display.addLine(Message.destinationIsBusy(terminal.getId()));
            _display.display();
            t.addAttemptedInter(receiver);
          }
          else if (terminal.getMode().equals(TerminalMode.SILENCE)){
            _display.addLine(Message.destinationIsSilent(terminal.getId()));
            _display.display();
            t.addAttemptedInter(receiver);
          }
          else {
            _network.startVideoCall(receiver, terminal);
          }
        }
      }
    
    }
  }
}
