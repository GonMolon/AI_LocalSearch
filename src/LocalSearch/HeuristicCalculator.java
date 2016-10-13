package LocalSearch;

import aima.search.framework.HeuristicFunction;

public class HeuristicCalculator implements HeuristicFunction {

    @Override
    public double getHeuristicValue(Object state) {
        return ((State) state).getCost();
    }
}
