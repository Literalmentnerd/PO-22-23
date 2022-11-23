package prr.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import prr.core.Notification;
import prr.core.TerminalBasic;
import prr.core.TerminalType;

// FIXME add more import if needed (cannot import from pt.tecnico or prr.app)

/**
 * Abstract terminal.
 */
abstract public class Terminal implements Serializable /* FIXME maybe addd more interfaces */ {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202208091753L;

  private String _id;
  private TerminalMode _mode; 
  private double _debt;
  private boolean _hasOngoingCommunication;
  private double _payments;
  protected TerminalMode _previousMode = TerminalMode.ON;
  protected InteractiveCommunication _onGoingCommunication;
  private ArrayList<Terminal> _attemptedTextCommunicationsFromTerminals;
  private ArrayList<Terminal> _attemptedInteractiveCommunicationsFromTerminals;
  private Client _clientOwner;
  private ArrayList<Terminal> _terminaisAmigos;
  private boolean _validTerminal;
  private ArrayList<Notification> _receivedNotifications;
  private ArrayList<Communications> _madeCommunications;
  private ArrayList<Communications> _receivedCommunications;

  public Terminal(String id, Client clientOwner) {
    this(id, clientOwner, TerminalMode.ON);
  }

  public Terminal(String id, Client clientOwner, TerminalMode mode) {
    _id = id;
    _mode = mode;
    _clientOwner = clientOwner;
    _terminaisAmigos = new ArrayList<>(10);
    _validTerminal = validTerminalId();
    _madeCommunications = new ArrayList<>(10);
    _onGoingCommunication = null;
    _attemptedTextCommunicationsFromTerminals = new ArrayList<>(10);
    _attemptedInteractiveCommunicationsFromTerminals = new ArrayList<>(10);
    _receivedNotifications = new ArrayList<>(10);
    _receivedCommunications = new ArrayList<>(10);
  }

  protected void addNotification(Notification notification){
    if (!_receivedNotifications.contains(notification)){
      _receivedNotifications.add(notification);
    }
  }


  public TerminalMode getPreviousMode(){
    return _previousMode;
  }
  public void switchHasOngoing(){
    if (_hasOngoingCommunication){
      _hasOngoingCommunication = false;
    }else{
      _hasOngoingCommunication = true;
    }
  }

  public void clearNotifications(){
    _receivedNotifications.clear();
  }


  public List<Notification> getNofitications(){
    return _receivedNotifications;
  }
  public boolean validTerminalId() {
    if (_id.length() != 6) {
      return false;
    } else {
      for (int i = 0; i < _id.length(); i++) {
        if (!Character.isDigit(_id.charAt(i))) {
          return false;
        }
      }
      return true;
    }
  }

  public boolean hasOngoingCommunication(){
    return _hasOngoingCommunication;
  }

  public List<Terminal> getAttemptTextCom(){
    return _attemptedTextCommunicationsFromTerminals;
  }
  public List<Terminal> getAttemptIntCom(){
    return _attemptedInteractiveCommunicationsFromTerminals;
  }

  public Client getClient() {
    return _clientOwner;
  }

  public abstract TerminalType getTerminalType();

  public boolean isValidTerminal() {
    return _validTerminal;
  }

  public String getId() {
    return _id;
  }

  public List<Terminal> getFriends() {
    return _terminaisAmigos;
  }

  public boolean addFriend(Terminal newTerminal) {
    if (_terminaisAmigos.contains(newTerminal)) {
      return false;
    }
    _terminaisAmigos.add(newTerminal);
    return true;
  }

  public boolean removeFriend(Terminal newTerminal) {
    if (!_terminaisAmigos.contains(newTerminal)) {
      return false;
    }
    _terminaisAmigos.remove(newTerminal);
    return true;
  }

  public int getDebtFromTerminal() {
    int payments = 0;
    for (Communications com : _madeCommunications){
      if (!com.isPaid()){
        payments += Math.round(com.computeCost(new BasicPlan("basic Plan")));
      }
    }
    return payments;
  }

  public int getPaymentsFromTerminal() {
    int payments = 0;
    for (Communications com : _madeCommunications){
      if (com.isPaid()){
        payments += Math.round(com.computeCost(new BasicPlan("basic plan")));
      }
    }
    return payments;
  }

  public Client getOwner() {
    return _clientOwner;
  }

  public TerminalMode getMode() {
    return _mode;
  }

  public String getModeToString() {
    if (_mode.equals(TerminalMode.ON)) {
      return "IDLE";
    } else if (_mode.equals(TerminalMode.BUSY)) {
      return "BUSY";
    } else if (_mode.equals(TerminalMode.SILENCE)) {
      return "SILENCE";
    } else {
      return "OFF";
    }

  }

  public InteractiveCommunication getOngoingCommunication(){
    return _onGoingCommunication;
  }
  public List<Communications> getCommunicationsMade() {
    return _madeCommunications;
  }

  public List<Communications> getComminciationsReceived() {
    return _receivedCommunications;
  }

  public void addCommunicationMade(Communications communication) {
    _madeCommunications.add(communication);
  }

  public void addCommunicationReceived(Communications communication) {
    _receivedCommunications.add(communication);
  }
  
  public boolean setOnIdle() {
    for (Terminal t : _attemptedTextCommunicationsFromTerminals){
      t.addNotification(new Notification(this.getMode() , TerminalMode.ON, this));
    }
    _attemptedTextCommunicationsFromTerminals.clear();
    for (Terminal t : _attemptedInteractiveCommunicationsFromTerminals){
      t.addNotification(new Notification(this.getMode(), TerminalMode.ON, this));
    }
    _attemptedInteractiveCommunicationsFromTerminals.clear();
    
    _mode = TerminalMode.ON;
    return true;
  }
  
  public boolean setMode(TerminalMode modeToChange, TerminalMode mode1, TerminalMode mode2) {
    if (_mode == mode1 || _mode == mode2) {
      _mode = modeToChange;
      return true;
    }
    return false;
  }

  public void setModeAdmin(TerminalMode modeToChange){
    if (modeToChange.equals(TerminalMode.ON)){
      for (Terminal t : _attemptedTextCommunicationsFromTerminals){
        t.addNotification(new Notification(this.getMode() , TerminalMode.ON, this));
      }
      _attemptedTextCommunicationsFromTerminals.clear();
      for (Terminal t : _attemptedInteractiveCommunicationsFromTerminals){
        t.addNotification(new Notification(this.getMode(), TerminalMode.ON, this));
      }
      _attemptedInteractiveCommunicationsFromTerminals.clear();
    }
    else if(modeToChange.equals(TerminalMode.SILENCE)){
      for (Terminal t : _attemptedTextCommunicationsFromTerminals){
        t.addNotification(new Notification(this.getMode() , TerminalMode.ON, this));
      }
      _attemptedTextCommunicationsFromTerminals.clear();
    }
    _mode = modeToChange;
  }
  
  public boolean setOnBusy() {
    return setMode(TerminalMode.BUSY, TerminalMode.ON, TerminalMode.SILENCE);
  }
  
  public boolean setOnSilence() {
    for (Terminal t : _attemptedTextCommunicationsFromTerminals){
      t.addNotification(new Notification(this.getMode(), TerminalMode.SILENCE, this));
    }
    _attemptedTextCommunicationsFromTerminals.clear();
    _mode = TerminalMode.SILENCE;
    return true;
  }

  public boolean turnOff() {
    return setMode(TerminalMode.OFF, TerminalMode.ON, TerminalMode.SILENCE);
  }

  public String getFriendsToString() {
    String friends = "|";
    Collections.sort(_terminaisAmigos ,(c1,c2) ->
    {
      return c1.getId().compareTo(c2.getId());
    });
    Iterator<Terminal> iter = _terminaisAmigos.iterator();
    if (_terminaisAmigos.isEmpty()) {
      return "";
    }
    while (iter.hasNext()) {
      Terminal t = iter.next();
      if (iter.hasNext()) {
        friends += t.getId() + ", ";
      } else {
        friends += t.getId();
      }
    }

    return friends;
  }

  public void addAttemptedText(Terminal from){
    _attemptedTextCommunicationsFromTerminals.add(from);
  }

  public void addAttemptedInter(Terminal from){
    _attemptedInteractiveCommunicationsFromTerminals.add(from);
  }

  public int getTerminalBalance(){
    return this.getPaymentsFromTerminal() - this.getDebtFromTerminal();
  }

  public TextCommunication makeSMS(int id, Terminal to, String message) {
    TextCommunication text = new TextCommunication(id, message, this, to);
    _madeCommunications.add(text);
    this.getClient().addCommunication(text);
    _debt += text.computeCost(new BasicPlan("basic")); 
    to.acceptSMS(text);
    return text;
  }

  protected void acceptSMS(TextCommunication text) {
    _receivedCommunications.add(text);
  }

  public InteractiveCommunication makeVoiceCall(int id, Terminal to) {
    to.acceptVoiceCall(id, this);
    _onGoingCommunication = new VoiceCommunication(id, this, to);
    _previousMode = this.getMode();
    this.switchHasOngoing();
    this.setOnBusy();
    to.setOnBusy();
    return _onGoingCommunication;
  }

  public Communications getCommunicationsFromKey(int key){
    for (Communications c : _madeCommunications){
      if (c.getId() == key){
        return c;
      }
    }
    return null;
  }
  
  protected void acceptVoiceCall(int id, Terminal from) {
    this.setModeAdmin(TerminalMode.BUSY);
    this.switchHasOngoing();
    _onGoingCommunication = new VoiceCommunication(id, from, this);
  }
  
  public int endOngoingCommunication(int size) {
    InteractiveCommunication iter = _onGoingCommunication;
    iter.setDuration(size);
    iter.endCommunication();
    this.addCommunicationMade(iter);
    this.getClient().addCommunication(iter);
    iter.getTermianlFor().addCommunicationReceived(iter);
    double newDebt = iter.computeCost(new BasicPlan("basic"));
    _debt += newDebt;
    TerminalMode forMode = iter.getTermianlFor().getPreviousMode();
    iter.getTermianlFor().setModeAdmin(forMode);
    iter.getTerminalFrom().setModeAdmin(_previousMode);
    iter.getTerminalFrom().switchHasOngoing();
    iter.getTermianlFor().switchHasOngoing();
    _onGoingCommunication = null;
    return (int) Math.round(newDebt);
  }
  

  abstract public String toString();

  /**
   * Checks if this terminal can end the current interactive communication.
   *
   * @return true if this terminal is busy (i.e., it has an active interactive
   *         communication) and
   *         it was the originator of this communication.
   **/

  public boolean canEndCurrentCommunication() {
    if (_mode == TerminalMode.BUSY) {
      return true;
    }
    return false;
  }

  /**
   * Checks if this terminal can start a new communication.
   *
   * @return true if this terminal is neither off neither busy, false otherwise.
   **/
  public boolean canStartCommunication() {
    if (this.getMode() != TerminalMode.OFF && this.getMode() != TerminalMode.BUSY) {
      return true;
    }
    return false;
  }
}
