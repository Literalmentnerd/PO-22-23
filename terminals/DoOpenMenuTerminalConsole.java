package prr.app.terminals;

import prr.app.terminal.Menu;
import prr.core.Network;
import prr.core.Terminal;

import java.util.Iterator;
import java.util.List;

import prr.app.exception.UnknownTerminalKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Open a specific terminal's menu.
 */
class DoOpenMenuTerminalConsole extends Command<Network> {

  DoOpenMenuTerminalConsole(Network receiver) {
    super(Label.OPEN_MENU_TERMINAL, receiver);
    addStringField("key", Message.terminalKey());
    // FIXME add command fields
  }

  @Override
  protected final void execute() throws CommandException, UnknownTerminalKeyException {
    try {
      String id = stringField("key");
      List<Terminal> list = _receiver.getTerminals();
      Iterator<Terminal> iter = list.iterator();
      boolean terminalFound = false;
      Terminal terminal = null;
      while (iter.hasNext() && !terminalFound) {
        terminal = iter.next();
        if (terminal.getId().equals(id)) {
          terminalFound = true;
        }
      }
      if (terminalFound) {
        prr.app.terminal.Menu terminalConsole = new Menu(_receiver, terminal);
        terminalConsole.open();
      } else {
        throw new UnknownTerminalKeyException(id);
      }
    } catch (CommandException e) {
      throw e;
    }
  }
}
