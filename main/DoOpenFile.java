package prr.app.main;

import prr.core.NetworkManager;
import prr.core.exception.UnavailableFileException;

import pt.tecnico.uilib.menus.Command;
import pt.tecnico.uilib.menus.CommandException;
//Add more imports if needed

/**
 * Command to open a file.
 */
class DoOpenFile extends Command<NetworkManager> {

  DoOpenFile(NetworkManager receiver) {
    super(Label.OPEN_FILE, receiver);
    addStringField("file", Message.openFile());
  }

  @Override
  protected final void execute() throws CommandException {

    try {
      _receiver.load(stringField("file"));
    } catch (UnavailableFileException | ClassNotFoundException e) {
      _display.addLine("Abrir: Operação inválida: Problema ao abrir ficheiro: Erro a processar ficheiro " + stringField("file"));
      _display.display();
    }
  }
}
