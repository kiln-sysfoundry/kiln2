package org.sysfoundry.kiln.health;

import java.util.concurrent.atomic.AtomicReference;

public class Status {

    public enum State{
        OK,
        WARNING,
        FAILED
    }

    private AtomicReference<State> currentHealth  = new AtomicReference<>();

    public Status(State initial){
        currentHealth.set(initial);
    }

    public void compareAndSet(State expectedState,State newState){
        currentHealth.compareAndSet(expectedState,newState);
    }

    public State get(){
        return currentHealth.get();
    }


    public void set(State newState){
        currentHealth.set(newState);
    }

}
