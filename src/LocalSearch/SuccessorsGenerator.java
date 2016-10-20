package LocalSearch;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class SuccessorsGenerator implements SuccessorFunction {

    public void operatorMove(State state, List successors) {
        State nextState = new State(state);
        for(int fromID = 0; fromID < Statement.getStatement().totalPackages(); ++fromID) {
            for(int days = Statement.getStatement().getPackage(fromID).getPrioridad()*2+1; days >= 1; --days) {
                for(int toID = 0; toID < Statement.getStatement().offers[days-1].length; ++toID) {
                    if(state.offer_of_package[fromID] != toID) {
                        if(nextState.movePackage(fromID, toID)) {
                            successors.add(new Successor("move: " + fromID + " -> " + toID, nextState));
                            nextState = new State(state);
                        }
                    }
                }
            }
        }
    }

    public void operatorSwap(State state, List successors) {
        State nextState = new State(state);
        for(int fromID = 0; fromID < Statement.getStatement().totalPackages(); ++fromID) {
            if(state.offer_of_package[fromID] != -1) {
                for(int toID = 0; toID < Statement.getStatement().totalPackages(); ++toID) {
                    if(state.offer_of_package[toID] != -1 && fromID != toID) {
                        if(nextState.swapPackage(fromID, toID)) {
                            successors.add(new Successor("swap: " + fromID + " <-> " + toID, nextState));
                            nextState = new State(state);
                        }
                    }
                }
            }
        }
    }

    @Override
    public List getSuccessors(Object o) {
        State state = (State) o;
        ArrayList<Successor> successors = new ArrayList<>();
        operatorMove(state, successors);
        operatorSwap(state, successors);
        return successors;
    }
}
