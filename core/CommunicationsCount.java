package prr.core;

public class CommunicationsCount {
    private static int _nextId = 1;

    public static void addOneToCounter(){
        _nextId += 1;
    }
    public static int getNextId(){
        return _nextId;
    }
}
