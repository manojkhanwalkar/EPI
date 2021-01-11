package actorframework.example2;

import actorframework.AktorRef;

public class Registration {

    public enum Action { Register , Cancel}
    AktorRef<Object> service;
    Action action;
}
