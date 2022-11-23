package prr.core;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.FileInputStream;

import prr.core.exception.ImportFileException;
import prr.core.exception.MissingFileAssociationException;
import prr.core.exception.UnavailableFileException;
import prr.core.exception.UnrecognizedEntryException;

/**
 * Manage access to network and implement load/save operations.
 */
public class NetworkManager {

  /** The network itself. */
  private Network _network = new Network();
  // FIXME addmore fields if needed

  private String _filename = "";

  public Network getNetwork() {
    return _network;
  }

  public String getFileName() {
    return _filename;
  }

  /**
   * @param filename name of the file containing the serialized application's
   *                 state
   *                 to load.
   * @throws UnavailableFileException if the specified file does not exist or
   *                                  there is
   *                                  an error while processing this file.
   * @throws IOException
   */
  public void load(String filename) throws UnavailableFileException, ClassNotFoundException {
    try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
      _network = (Network) in.readObject();
      _filename = (String) in.readObject();
    } catch (IOException e) {
      throw new UnavailableFileException(filename);
    }
  }

  /**
   * Saves the serialized application's state into the file associated to the
   * current network.
   *
   * @throws FileNotFoundException           if for some reason the file cannot be
   *                                         created or opened.
   * @throws MissingFileAssociationException if the current network does not have
   *                                         a file.
   * @throws IOException                     if there is some error while
   *                                         serializing the state of the network
   *                                         to disk.
   */
  public void save() throws MissingFileAssociationException, FileNotFoundException, IOException {
    try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(_filename))) {
      out.writeObject(_network);
      out.writeObject(_filename);
    }
  }

  /**
   * Saves the serialized application's state into the specified file. The current
   * network is
   * associated to this file.
   *
   * @param filename the name of the file.
   * @throws MissingFileAssociationException if the current network does not have
   *                                         a file.
   * @throws IOException                     if there is some error while
   *                                         serializing the state of the network
   *                                         to disk.
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * Read text input file and create domain entities..
   * 
   * @param filename name of the text input file
   * @throws ImportFileException
   */
  public void importFile(String filename) throws ImportFileException, UnrecognizedEntryException, IOException {
    try {
      _network.importFile(filename);
    } catch (IOException | UnrecognizedEntryException e) {
      throw new ImportFileException(filename, e);
    }
  }
}
