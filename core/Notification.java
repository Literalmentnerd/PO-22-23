package prr.core;

import prr.core.Communications;
import prr.core.NotificationType;
import prr.core.TerminalMode;

public class Notification {
    
    private static final long serialVersionUID = 202208091753L;

    private NotificationType _type;
    private Terminal _notifyingTerminal;
    public Terminal getNotifyingTerminal(){
        return _notifyingTerminal;
    }
    public NotificationType getNotificationType(){
        return _type;
    }
    public Notification(TerminalMode modeToSwitch, TerminalMode newMode,Terminal notifyingTerminal){
        if (modeToSwitch.equals(TerminalMode.OFF)){
            if (newMode.equals(TerminalMode.ON)){
                _type = NotificationType.O2I;
            }
            else{
                _type = NotificationType.O2S;
            }
        }
        else if (modeToSwitch.equals(TerminalMode.BUSY)){
            _type = NotificationType.B2I;
        } else {
            _type = NotificationType.S2I;
        }
        _notifyingTerminal = notifyingTerminal;
    }
    public String getNotificationTypeToString(){
        if (_type.equals(NotificationType.O2I)){
            return "O2I";
        }
        else if(_type.equals(NotificationType.O2S)){
            return "O2S";
        }
        else if(_type.equals(NotificationType.B2I)){
            return "B2I";
        }
        else{return "S2I"; }
    }

    public String toString(){
        return this.getNotificationTypeToString() + "|" + _notifyingTerminal.getId();
    }
}
