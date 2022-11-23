package prr.app.exception;

import pt.tecnico.uilib.menus.CommandException;

public class UnknownTerminalType extends CommandException {
    public UnknownTerminalType(String type) {
        super("'" + type + "' is not a supported terminal type(BASIC or FANCY)");
    }
}
