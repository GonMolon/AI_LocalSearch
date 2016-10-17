package LocalSearch;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;

import java.util.ArrayList;
import java.util.List;

public class SuccesorGenerator implements SuccessorFunction {

    @Override
    public List getSuccessors(Object o) {
        State state = (State) o;
        Statement statement = state.getProblem();
        ArrayList<Successor> successors = new ArrayList<>();
        State nextState = new State(state);
        for(int fromID = 0; fromID < statement.totalPackages(); ++fromID) {
            for(int days = statement.getPackage(fromID).getPrioridad()*2+1; days >= 1; --days) {
                for(int toID = 0; toID < statement.offers[days-1].length; ++toID) {
                    if(state.offer_of_package[fromID] != toID) {
                        if(nextState.movePackage(fromID, toID)) {
                            successors.add(new Successor("move: " + fromID + " -> " + toID, nextState));
                            nextState = new State(state);
                        }
                    }
                }
            }
        }
        for(int fromID = 0; fromID < statement.totalPackages(); ++fromID) {
            if(state.offer_of_package[fromID] != -1) {
                for(int toID = 0; toID < statement.totalPackages(); ++toID) {
                    if(state.offer_of_package[toID] != -1 && fromID != toID) {
                        if(nextState.swapPackage(fromID, toID)) {
                            successors.add(new Successor("swap: " + fromID + " <-> " + toID, nextState));
                            nextState = new State(state);
                        }
                    }
                }
            }
        }
        return successors;
    }
}
