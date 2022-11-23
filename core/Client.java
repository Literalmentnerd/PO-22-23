package prr.core;

import java.util.ArrayList;
import java.lang.Math;
import java.io.Serializable;
import java.util.List;
import prr.core.ClientLevel;
import prr.core.Communications;
import prr.core.Notification;

public class Client implements Serializable {
    private static final long serialVersionUID = 202208091753L;
    private String _key;
    private String _name;
    private Integer _nif;
    private ClientLevel _level;
    private int _consecutiveText;
    private int _consecutiveVideo;
    private ArrayList<Communications> _communcations;
    private boolean _receiveNotifications;
    private ArrayList<Terminal> _associatedTerminals;

    public void toNormal(){
        _level = ClientLevel.NORMAL;
    }

    public void toGold(){
        _level = ClientLevel.GOLD;
    }

    public void toPlatinum(){
        _level = ClientLevel.PLATINUM;
    }


    public List<Communications> getCommunications(){
        return _communcations;
    }
    public void addCommunication(Communications com){
        _communcations.add(com);
    }
    public Client(String key, String name, int nif) {
        _key = key;
        _name = name;
        _consecutiveText = 0;
        _consecutiveVideo = 0;
        _nif = nif;
        _level = ClientLevel.NORMAL;
        _communcations = new ArrayList<>();
        _receiveNotifications = true;
        _associatedTerminals = new ArrayList<>();
    }

    public void incrementConsecutiveText(){
        _consecutiveText++;
    }
    public void resetConsecutiveText(){
        _consecutiveText = 0;
    }
    public int getConsecutiveText(){
        return _consecutiveText;
    }

    public void incrementConsecutiveVideo(){
        _consecutiveVideo++;
    }
    public void resetConsecutiveVideo(){
        _consecutiveVideo = 0;
    }
    public int getConsecutiveVideo(){
        return _consecutiveVideo;
    }

    public boolean triedContactingTerminalBefore(List<Notification> l, Notification n){
        for (Notification noti : l){
            if(n.getNotifyingTerminal().getId().equals(noti.getNotifyingTerminal().getId()) 
            && n.getNotificationType().equals(noti.getNotificationType())){
                return true;
            }
        }
        return false;
    }

    public List<Notification> getNotifications(){
        List<Notification> l = new ArrayList<>(10);
        for (Terminal t : _associatedTerminals){
            for (Notification n : t.getNofitications()){
                if (!triedContactingTerminalBefore(l, n)){
                    l.add(n);
                }
            }
            t.clearNotifications();
        }
        return l;
    }


    public ClientLevel getClientLevel(){
        return _level;
    }

    public String getKey() {
        return _key;
    }

    public void switchNotifications() {
        if (_receiveNotifications) {
            _receiveNotifications = false;
        } else {
            _receiveNotifications = true;
        }
    }

    public String getName() {
        return _name;
    }

    public ClientLevel getLevel() {
        return _level;
    }

    public String getClientLevelToString() {
        if (_level.equals(ClientLevel.NORMAL)) {
            return "NORMAL";
        } else if (_level.equals(ClientLevel.GOLD)) {
            return "GOLD";
        } else {
            return "PLATINUM";
        }
    }

    public int getTaxId() {
        return _nif;
    }

    public boolean getNotificationStatus() {
        return _receiveNotifications;
    }

    public List<Terminal> getAssociatedTerminals() {
        return _associatedTerminals;
    }

    public void addTerminal(Terminal terminalToAdd) {
        _associatedTerminals.add(terminalToAdd);
    }

    public int getNumberOfTerminals() {
        int i = 0;
        for (Terminal t : _associatedTerminals) {
            i++;
        }
        return i;
    }

    public long getDebt() {
        double debt = 0;
        for (Terminal t : _associatedTerminals) {
            debt += t.getDebtFromTerminal();
        }
        return Math.round(debt);
    }

    public long getPayments() {
        double payments = 0;
        for (Terminal t : _associatedTerminals) {
            payments += t.getPaymentsFromTerminal();
        }
        return Math.round(payments);
    }

    public String getNotificationsString() {
        if (_receiveNotifications) {
            return "YES";
        } else {
            return "NO";
        }
    }

    public String toString() {
        String message = "CLIENT|" + _key + "|" + _name + "|" + _nif.toString() + "|" + getLevel()
                + "|" + this.getNotificationsString() + "|" + this.getNumberOfTerminals()
                + "|" + this.getPayments() + "|" + this.getDebt();
        return message;
    }
}
