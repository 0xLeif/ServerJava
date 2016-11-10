package source;

import java.io.Serializable;
import java.util.Vector;

/**
 * Created by zeriksen on 7/1/2014.
 */
public class WorldState implements Serializable {
    private Vector<State> ws;


    public WorldState(Vector<State> ws) {
        this.ws = ws;
    }
    public Vector<State> getStates() {
        return ws;
    }
    
    @Override
    public String toString() {
    	return ws.toString();
    }

}
