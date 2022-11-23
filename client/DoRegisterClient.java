package prr.app.client;

import prr.core.Network;


import prr.app.exception.DuplicateClientKeyException;
import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;

/**
 * Register new client.
 */
class DoRegisterClient extends Command<Network> {

  DoRegisterClient(Network receiver) {
    super(Label.REGISTER_CLIENT, receiver);
    addStringField("key", Message.key());;
    addStringField("name", Message.name());
    addIntegerField("taxId", Message.taxId());

  }
  
  @Override
  protected final void execute() throws CommandException, DuplicateClientKeyException {
    String key = stringField("key");
    String name = stringField("name");
    int taxId = integerField("taxId");
      
    try {
      if (!_receiver.registerClient(name, taxId, key)){
        throw new DuplicateClientKeyException(key);
      }
    } catch (CommandException e){throw e;}
  }
}
