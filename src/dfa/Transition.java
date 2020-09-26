package dfa;

import java.util.HashMap;
import java.util.Map;

public class Transition {

    String name ;
    Map<String,String> transitions = new HashMap<>();

    public  Transition(String name)
    {
        this.name = name;
    }

    public void init(Map<String,String> transitions)
    {
        this.transitions.putAll(transitions);
    }

    public void add(String currentState , String input , String nextState)
    {
        transitions.put(currentState+"-"+ input, nextState);
    }

    public String get(String currentState, String input)
    {
        String  nextState =  transitions.get(currentState+"-"+input);
        return nextState;
    }


}
