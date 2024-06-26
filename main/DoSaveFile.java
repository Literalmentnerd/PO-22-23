package prr.app.main;

import java.io.IOException;

import prr.core.NetworkManager;
import prr.core.exception.MissingFileAssociationException;
import pt.tecnico.uilib.forms.Form;
import pt.tecnico.uilib.menus.Command;
//FIXME add more imports if needed
import pt.tecnico.uilib.menus.CommandException;

/**
 * Command to save a file.
 */
class DoSaveFile extends Command<NetworkManager> {

  DoSaveFile(NetworkManager receiver) {
    super(Label.SAVE_FILE, receiver);

  }

  @Override
  protected final void execute() throws CommandException {
    if (_receiver.getFileName().equals("")) {
      Form form = new Form();
      form.addStringField("ficheiro", Message.newSaveAs());
      form.parse(true);
      try {
        _receiver.saveAs(form.stringField("ficheiro"));
      } catch (MissingFileAssociationException | IOException e) {
        _display.addLine(""); // FIXME find exception required
        _display.display();
      }

    } else {
      try {
        _receiver.save();
      } catch (MissingFileAssociationException | IOException e) {
        _display.addLine(""); // FIXME find exception required
        _display.display();
      }
    }
  }
}