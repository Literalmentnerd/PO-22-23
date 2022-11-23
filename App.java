package prr.app;

import java.io.IOException;

import prr.core.NetworkManager;
import prr.core.exception.ImportFileException;
import prr.core.exception.UnrecognizedEntryException;
import pt.tecnico.uilib.Dialog;

/**
 * Application entry-point.
 */
public class App {

  public static void main(String[] args) {
    try (var ui = Dialog.UI) {
      var receiver = new NetworkManager();

      String datafile = System.getProperty("import");
      if (datafile != null) {
        try {
          receiver.importFile(datafile);
        } catch (ImportFileException | UnrecognizedEntryException | IOException e) {
          // no behavior described: just present the problem
          e.printStackTrace();
        }
      }

      (new prr.app.main.Menu(receiver)).open();
    }
  }
}
