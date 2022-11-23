package prr.app.main;

import prr.core.Network;
import prr.core.Terminal;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Show global balance.
 */
class DoShowGlobalBalance extends Command<Network> {

  DoShowGlobalBalance(Network receiver) {
    super(Label.SHOW_GLOBAL_BALANCE, receiver);
  }
  
  @Override
  protected final void execute() throws CommandException {
    int payments = 0;
    int debt = 0;
    for (Terminal t: _receiver.getTerminals()){
      payments += t.getPaymentsFromTerminal();
      debt += t.getDebtFromTerminal();
    }
    _display.addLine(Message.globalPaymentsAndDebts(payments, debt));
    _display.display();
  }
}