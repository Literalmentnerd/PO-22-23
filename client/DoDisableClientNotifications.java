package prr.app.client;

import prr.core.Client;
import prr.core.Network;


import prr.app.exception.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Disable client notifications.
 */
class DoDisableClientNotifications extends Command<Network> {

  DoDisableClientNotifications(Network receiver) {
    super(Label.DISABLE_CLIENT_NOTIFICATIONS, receiver);
    addStringField("clientId", Message.key());
  }

  @Override
  protected final void execute() throws CommandException {
    Client c = _receiver.getClientFromKey(stringField("clientId"));
    if (!c.equals(null)) {
      if (c.getNotificationStatus()) {
        c.switchNotifications();
      }
      else {
        _display.addLine(Message.clientNotificationsAlreadyDisabled());
        _display.display();
      }
    } else {
      throw new UnknownClientKeyException(stringField("clientId"));
    }
  }
}
