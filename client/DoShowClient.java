package prr.app.client;

import prr.core.Client;
import prr.core.Network;

import prr.core.Notification;

import prr.app.exception.UnknownClientKeyException;
import pt.tecnico.uilib.menus.Command;

import pt.tecnico.uilib.menus.CommandException;
//FIXME add more imports if needed

/**
 * Show specific client: also show previous notifications.
 */
class DoShowClient extends Command<Network> {

  public DoShowClient(Network receiver) {
    super(Label.SHOW_CLIENT, receiver);
    addStringField("key", Message.key());
  }

  @Override
  protected final void execute() throws CommandException, UnknownClientKeyException {
    try {
      String key = stringField("key");
      Client desiredClient = _receiver.getClientFromKey(key);
      if (desiredClient != null) {
        _display.addLine(desiredClient.toString());
        if(desiredClient.getNotificationStatus()){ 
          for (Notification n : desiredClient.getNotifications()){
            _display.addLine(n.toString());
          }
        }
        _display.display();
      } else {
        throw new UnknownClientKeyException(key);
      }
    } catch (CommandException e) {
      throw e;
    }
  }
}