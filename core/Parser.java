package prr.core;

import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedReader;

import prr.core.exception.UnrecognizedEntryException;
// import more exception core classes if needed

/* 
 * A concretização desta classe depende da funcionalidade suportada pelas entidades do core:
 *  - adicionar um cliente e terminal a uma rede de terminais;
 *  - indicar o estado de um terminal
 *  - adicionar um amigo a um dado terminal
 * A forma como estas funcionalidades estão concretizaas terão impacto depois na concretização dos
 * métodos parseClient, parseTerminal e parseFriends
 */

public class Parser {
  private Network _network;

  Parser(Network network) {
    _network = network;
  }

  void parseFile(String filename) throws IOException, UnrecognizedEntryException {
    try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
      String line;

      while ((line = reader.readLine()) != null)
        parseLine(line);
    }
  }

  private void parseLine(String line) throws UnrecognizedEntryException {
    String[] components = line.split("\\|");
    if (components[0].equals("CLIENT")) {
      parseClient(components, line);
    } else if (components[0].equals("BASIC") || components[0].equals("FANCY")) {
      parseTerminal(components, line);
    } else if (components[0].equals("FRIENDS")) {
      parseFriends(components, line);
    } else {
      throw new UnrecognizedEntryException("Line with wrong type: " + components[0]);
    }
  }

  private void checkComponentsLength(String[] components, int expectedSize, String line)
      throws UnrecognizedEntryException {
    if (components.length != expectedSize)
      throw new UnrecognizedEntryException("Invalid number of fields in line: " + line);
  }

  // parse a client with format CLIENT|id|nome|taxId
  private void parseClient(String[] components, String line) throws UnrecognizedEntryException {
    checkComponentsLength(components, 4, line);

    try {
      int taxNumber = Integer.parseInt(components[3]);
      _network.registerClient(components[2], taxNumber, components[1]);
    } catch (NumberFormatException nfe) {
      throw new UnrecognizedEntryException("Invalid number in line " + line, nfe);
    }
  }

  // parse a line with format 0-terminal-type|1-idTerminal|2-idClient|3-state
  private void parseTerminal(String[] components, String line) throws UnrecognizedEntryException {
    checkComponentsLength(components, 4, line);

    try {
      Client clientOwner = _network.getClientFromKey(components[2]);
      if (!clientOwner.equals(null)) {
        TerminalMode mode = null;
        if (components[3].equals("SILENCE")) {
          mode = TerminalMode.SILENCE;
        } else if (components[3].equals("OFF")) {
          mode = TerminalMode.OFF;
        }else if (components[3].equals("ON")) {
          mode = TerminalMode.ON;
        } else if (components[3].equals("BUSY")) {
          mode = TerminalMode.OFF;
        } else {
          throw new UnrecognizedEntryException("Invalid specification in line: " + line);
        }
        if (components[0].equals("BASIC")) {
          Terminal t = new TerminalBasic(components[1], clientOwner, mode);
          _network.registerTerminal(t);
        } else if (components[0].equals("FANCY")) {
          Terminal t = new TerminalFancy(components[1], clientOwner, mode);
          _network.registerTerminal(t);
        } else {
          throw new UnrecognizedEntryException("Terminal type does not exist in: " + line);
        }
      } else {
        throw new UnrecognizedEntryException("Client does not exist in line: " + line);
      }

    } catch (UnrecognizedEntryException e) {
      throw new UnrecognizedEntryException("Invalid specification: " + line, e);
    }
  }

  // Parse a line with format FRIENDS|idTerminal|idTerminal1,...,idTerminalN
  private void parseFriends(String[] components, String line) throws UnrecognizedEntryException {
    checkComponentsLength(components, 3, line);

    try {
      String terminal = components[1];
      String[] friends = components[2].split(",");

      for (String friend : friends)
        if (!_network.addFriend(terminal, friend)) {
          if (_network.getTerminalFromId(terminal).getFriends().contains(_network.getTerminalFromId(friend))) {
            throw new UnrecognizedEntryException("Terminals are already friends");
          } else if (_network.getTerminalFromId(terminal).equals(null)) {
            throw new UnrecognizedEntryException("Terminal id does not exist: " + terminal);
          } else {
            throw new UnrecognizedEntryException("Terminal id does not exist: " + friend);
          }

        }
    } catch (UnrecognizedEntryException e) {
      throw new UnrecognizedEntryException("Some message error in line:  " + line, e);
    }
  }
  

}