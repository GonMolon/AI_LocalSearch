package LocalSearch;

import aima.search.framework.Successor;

import java.util.ArrayList;
import java.util.List;

public class SuccessorGenerator extends SuccessorsGenerator {

    public SuccessorGenerator(int operatorSet) {
        super(operatorSet);
    }

    @Override
    public List getSuccessors(Object o) {
        List successors = super.getSuccessors(o);
        if(successors.size() == 0) {
            return successors;
        } else {
            ArrayList<Successor> successor = new ArrayList<>();
            int successorID = (int)(Math.random() * successors.size());
            successor.add((Successor) successors.get(successorID));
            System.out.println(((State)successor.get(0).getState()).getCost());
            return successor;
        }
    }
}
