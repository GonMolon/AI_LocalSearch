package LocalSearch;

import aima.search.framework.Successor;
import aima.search.framework.SuccessorFunction;
import aima.search.framework.Successor;

import java.util.ArrayList;
import java.util.List;

public class SuccesorGenerator implements SuccessorFunction {

    @Override
    public List getSuccessors(Object o) {
        State state = (State) o;
<<<<<<< HEAD
        Statement statement = state.getProblem();
        ArrayList<Successor> successors = new ArrayList<>();
=======
        Problem problem = state.getProblem();
        ArrayList successors = new ArrayList();
>>>>>>> 9aec9d2c7be0264853ad15970aa5ecc73bf6c37d
        State nextState = new State(state);
        for(int fromID = 0; fromID < statement.totalPackages(); ++fromID) {
            for(int toID = 0; toID < statement.totalPackages(); ++toID) {
                if(fromID != toID) {
                    if(nextState.swapPackage(fromID, toID)) {
<<<<<<< HEAD
                        successors.add(new Successor("swap: " + fromID + " <-> " + toID, nextState));
=======
                        successors.add(new Successor("Swap: " + fromID + ", " + toID, nextState));
>>>>>>> 9aec9d2c7be0264853ad15970aa5ecc73bf6c37d
                        nextState = new State(state);
                    }
                }
            }
        }
        for(int fromID = 0; fromID < statement.totalPackages(); ++fromID) {
            for(int days = statement.getPackage(fromID).getPrioridad()*2+1; days >= 1; --days) {
                for(int toID = 0; toID < statement.offers[days-1].length; ++toID) {
                    if(state.offer_of_package[fromID] != toID) {
                        if(nextState.movePackage(fromID, toID)) {
<<<<<<< HEAD
                            successors.add(new Successor("move: " + fromID + " -> " + toID, nextState));
=======
                            successors.add(new Successor("Swap: " + fromID + ", " + toID, nextState));
>>>>>>> 9aec9d2c7be0264853ad15970aa5ecc73bf6c37d
                            nextState = new State(state);
                        }
                    }
                }
            }
        }
        return successors;
    }
}
