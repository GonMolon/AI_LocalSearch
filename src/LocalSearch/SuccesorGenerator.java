package LocalSearch;

import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;

import java.util.ArrayList;
import java.util.List;

public class SuccesorGenerator implements SuccessorFunction {

    @Override
    public List getSuccessors(Object o) {
        State state = (State) o;
        Problem problem = state.getProblem();
        ArrayList successors = new ArrayList();
        State nextState = new State(state);
        for(int fromID = 0; fromID < problem.totalPackages(); ++fromID) {
            for(int toID = 0; toID < problem.totalPackages(); ++toID) {
                if(fromID != toID) {
                    if(nextState.swapPackage(fromID, toID)) {
                        successors.add(new Successor("Swap: " + fromID + ", " + toID, nextState));
                        nextState = new State(state);
                    }
                }
            }
        }
        for(int fromID = 0; fromID < problem.totalPackages(); ++fromID) {
            for(int days = problem.getPackage(fromID).getPrioridad()*2+1; days >= 1; --days) {
                for(int toID = 0; toID < problem.offers[days-1].length; ++toID) {
                    if(state.offer_of_package[fromID] != toID) {
                        if(nextState.movePackage(fromID, toID)) {
                            successors.add(new Successor("Swap: " + fromID + ", " + toID, nextState));
                            nextState = new State(state);
                        }
                    }
                }
            }
        }
        return successors;
    }
}
