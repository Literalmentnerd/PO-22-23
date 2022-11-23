package prr.core;

import java.io.Serializable;
import java.io.IOException;

import prr.core.Client;
import prr.core.TerminalMode;
import prr.core.exception.ImportFileException;
import prr.core.exception.UnrecognizedEntryException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Class Store implements a store.
 */
public class Network implements Serializable {

  private static final long serialVersionUID = 202208091753L;

  private ArrayList<Terminal> _associatedTerminals;
  private ArrayList<Client> _associatedClients;
  private ArrayList<Communications> _communicationsRegestry;
  private int _idCommunications;
  public Network() {
    _idCommunications = 1;
    _associatedClients = new ArrayList<>(10);
    _associatedTerminals = new ArrayList<>(10);
    _communicationsRegestry = new ArrayList<>(10);
  }

  /**
   * gets network's existing terminals
   * 
   * @return network's existing terminals
   * 
   */
  public ArrayList<Terminal> getTerminals() {
    return _associatedTerminals;
  }

  /**
   * gets network's existing clients
   * 
   * @return network's existing clients
   * 
   */
  public ArrayList<Client> getClients() {
    return _associatedClients;
  }

  /**
   * gets network's existing communications
   * 
   * @return network's existing communications
   * 
   */
  public ArrayList<Communications> getCommuncations() {
    return _communicationsRegestry;
  }

  public void sendTextCommunication(Terminal from, Terminal to, String message){
    TextCommunication t = from.makeSMS(_idCommunications,to, message);
    _idCommunications++;
    _communicationsRegestry.add(t);
  }

  public void startVoiceCall(Terminal from, Terminal to){
    VoiceCommunication v = (VoiceCommunication)from.makeVoiceCall(_idCommunications, to);
    _idCommunications++;
    _communicationsRegestry.add(v);
  }
  
  public boolean getStreakText(Client c){
    int k = 0;
    for (Communications com : c.getCommunications()){
      if (!com.getTypeString().equals("TEXT")){
        return false;
      }
      k++;
      if(k==2){
        break;
      }
    }
    return true;
  }

  public boolean getStreakVideo(Client c){
    int k = 0;
    for (Communications com : c.getCommunications()){
      if (!com.getTypeString().equals("VIDEO")){
        return false;
      }
      k++;
      if(k==5){
        break;
      }
    }
    return true;
  }

  public void refreshClientLevel(Client client){
    if(client.getLevel().equals(ClientLevel.NORMAL)){
      if (client.getPayments() - 500 >= client.getDebt() ){
        client.toGold();
      }
    }
    else if (client.getLevel().equals(ClientLevel.GOLD)){
      if(client.getPayments()<client.getDebt()){
        client.toNormal();
      }
      else if (client.getConsecutiveVideo() == 5 && client.getPayments() >= client.getDebt()){
        client.resetConsecutiveVideo();
        client.toPlatinum();
      }
    }
    else{
      if (client.getConsecutiveText() == 2 && (client.getPayments() > client.getDebt())){
        client.resetConsecutiveText();
        client.toGold();
      }
      else if (client.getPayments()<client.getDebt()){
        client.toNormal();
      }
    }
  }

  public void startVideoCall(TerminalFancy from, TerminalFancy to){
    VideoCommunication v = (VideoCommunication)from.makeVideoCall(_idCommunications ,to);
    _idCommunications++;
    _communicationsRegestry.add(v);
  }

  /**
   * gets client associated to unique client key
   * 
   * @param id - key of the desired cliet
   * @return the client with the unique key if found
   *         a null client class if client does not exist
   * 
   */
  public Client getClientFromKey(String key) {
    for (Client c : _associatedClients) {
      if (c.getKey().equals(key)) {
        return c;
      }
    }
    return null;
  }

  
  /**
   * gets terminal associated to unique terminal key
   * 
   * @param id - key of the desired terminal
   * @return the terminal with the unique key if found
   *         a null terminal class if the terminal does not exist
   * 
   */
  public Terminal getTerminalFromId(String id) {
    for (Terminal t : _associatedTerminals) {
      if (t.getId().equals(id)) {
        return t;
      }
    }
    return null;
  }

  /**
   * Allows to add a registed terminal friend to a already resgisted terminal
   * 
   * 
   * @param id - id of the terminal associated to
   *           idFriend - id of the terminal friend to add
   * @return if the friend addition was successfull
   * 
   * 
   */
  public boolean addFriend(String id, String idFriend) {
    if (!this.getTerminalFromId(id).equals(null) && !this.getTerminalFromId(idFriend).equals(null)) {
      if (this.getTerminalFromId(id).getFriends().contains(getTerminalFromId(idFriend))) {
        return false;
      }
      Terminal t = this.getTerminalFromId(id);
      t.addFriend(this.getTerminalFromId(idFriend));
      return true;
    }
    return false;
  }

  /**
   * Adds client to the network's registedClients
   * 
   * @param name - client's name
   *             taxNumber - client's NIF
   *             key - unique client's id
   * @return if the creation of the new client was succesfull
   */
  public boolean registerClient(String name, int taxNumber, String key) {
    Client c = new Client(key, name, taxNumber);
    for (Client client : _associatedClients) {
      if (client.getKey().equals(key)) {
        return false;
      }
    }
    _associatedClients.add(c);
    Collections.sort(_associatedClients, (c1, c2) -> {
      return c1.getKey().toLowerCase().compareTo(c2.getKey().toLowerCase());

    });
    return true;
  }

  /**
   * Adds terminal to the network's registedTerminals
   * 
   * @param newTerminal - generated terminal to be added to network
   * @return if the creation of the new terminal was succesfull
   */
  public boolean registerTerminal(Terminal newTerminal) {
    if (this.getClients().contains(newTerminal.getClient())) {
      for (Terminal t : _associatedTerminals) {
        if (t.getId().equals(newTerminal.getId())) {
          return false;
        }
      }
      if (!newTerminal.isValidTerminal()) {
        return false;
      }
      _associatedTerminals.add(newTerminal);
      newTerminal.getClient().addTerminal(newTerminal);
      Collections.sort(_associatedTerminals, (t1, t2) -> {
        return Integer.valueOf(t1.getId()) - Integer.valueOf(t2.getId());
      });
      return true;
    }
    return false;
  }

  /**
   * Adds terminal to the network's registedTerminals
   * 
   * @param id - id of the new desired termianl
   *           clientOwner - client that the new terminal should be owned by
   *           type - type of the new terminal (BASIC or FANCY)
   * @return if the creation of the new terminal was succesfull
   */
  public boolean registerTerminal(String id, Client clientOwner, TerminalType type) {
    for (Terminal t : _associatedTerminals) {
      if (t.getId().equals(id)) {
        return false;
      }
    }
    if (this.getClients().contains(clientOwner)) {
      if (type == TerminalType.BASIC) {
        TerminalBasic tb = new TerminalBasic(id, clientOwner);

        if (!tb.isValidTerminal()) {
          return false;
        }
        _associatedTerminals.add(tb);
        clientOwner.addTerminal(tb);
        return true;
      } else {
        TerminalFancy tf = new TerminalFancy(id, clientOwner);
        if (!tf.isValidTerminal()) {
          return false;
        }
        _associatedTerminals.add(tf);
        Collections.sort(_associatedTerminals, (t1, t2) -> {
          return Integer.valueOf(t1.getId()) - Integer.valueOf(t2.getId());
        });
        clientOwner.addTerminal(tf);
        return true;
      }
    }
    return false;
  }

  /**
   * Read text input file and create corresponding domain entities.
   * 
   * @param filename name of the text input file
   * @throws UnrecognizedEntryException if some entry is not correct
   * @throws ImportFileException
   * @throws IOException                if there is an IO error while processing
   *                                    the text file
   */
  public void importFile(String filename) throws IOException, UnrecognizedEntryException {
    Parser p = new Parser(this);
    p.parseFile(filename);
  }
}


