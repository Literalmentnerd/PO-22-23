package prr.app.terminals;

import prr.core.TerminalType;

import prr.core.Client;
import prr.core.Network;

import prr.app.exception.DuplicateTerminalKeyException;
import prr.app.exception.InvalidTerminalKeyException;
import prr.app.exception.UnknownClientKeyException;
import prr.app.exception.UnknownTerminalType;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
import pt.tecnico.uilib.forms.Form;

/**
 * Register terminal.
 */
class DoRegisterTerminal extends Command<Network> {

  DoRegisterTerminal(Network receiver) {
    super(Label.REGISTER_TERMINAL, receiver);
  }

  @Override
  protected final void execute()
      throws CommandException, DuplicateTerminalKeyException, InvalidTerminalKeyException, UnknownClientKeyException {
    Form form = new Form();
    form.addStringField("terminalId", Message.terminalKey());
    form.parse();
    String terminalId = form.stringField("terminalId");
    form.clear();
    String terminalType = "";
    while (true) {
      form.addStringField("terminalType", Message.terminalType());
      form.parse();
      if (form.stringField("terminalType").equals("BASIC")) {
        terminalType = form.stringField("terminalType");
        form.clear();
        break;
      } else if (form.stringField("terminalType").equals("FANCY")) {
        terminalType = form.stringField("terminalType");
        form.clear();
        break;
      }
      form.clear();
    }
    form.addStringField("clientId", Message.clientKey());
    form.parse();

    try {
      Boolean invalidId = false;
      for (int i = 0; i < terminalId.length(); i++) {
        if (!Character.isDigit(terminalId.charAt(i))) {
          invalidId = true;
        }
      }
      if (!invalidId) {
        if (Integer.valueOf(terminalId) < 10) {
          while (terminalId.length() < 6) {
            terminalId = "0" + terminalId;
          }
        }
      }
      Client desiredClient = _receiver.getClientFromKey(form.stringField("clientId"));
      if (desiredClient != null) {
        if (terminalType.equals("BASIC")) {
          TerminalType type = TerminalType.BASIC;
          if (!_receiver.registerTerminal(terminalId, desiredClient, type)) {
            if (invalidId) {
              throw new InvalidTerminalKeyException(terminalId);
            }
            if (terminalId.length() != 6) {
              throw new InvalidTerminalKeyException(terminalId);
            } else {
              throw new DuplicateTerminalKeyException(terminalId);
            }
          }
        } else if (terminalType.equals("FANCY")) {
          TerminalType type = TerminalType.FANCY;
          if (!_receiver.registerTerminal(terminalId, desiredClient, type)) {
            if (invalidId) {
              throw new InvalidTerminalKeyException(terminalId);
            }
            if (terminalId.length() != 6) {
              throw new InvalidTerminalKeyException(terminalId);
            } else {
              throw new DuplicateTerminalKeyException(terminalId);
            }
          }
        } else {
          throw new UnknownTerminalType(form.stringField("terminalType"));
        }
      } else {
        throw new UnknownClientKeyException(form.stringField("clientId"));
      }
    } catch (CommandException e) {
      throw e;
    }
  }
}