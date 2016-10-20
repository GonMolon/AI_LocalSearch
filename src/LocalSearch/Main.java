package LocalSearch;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

public class Main {
    public static void main(String[] args) {
        int out = Integer.parseInt(args[0]), seed = Integer.parseInt(args[1]),
                n = Integer.parseInt(args[2]), fel = Integer.parseInt(args[4]),
                ope = Integer.parseInt(args[5]),
        double prop = Double.parseDouble([3]),
        boolean mode = true;
        Statement statement;
        State initialState;
        if(mode) {
            statement = new Statement(n, prop, seed, SortMode.OPTIMUM);
            initialState = InitialStateFactory.generateInitialState(statement,  true);
        } else {
            statement = new Statement(n, prop, seed, SortMode.RANDOM);
            initialState = InitialStateFactory.generateInitialState(statement,  false);
        }
        Problem problem = new Problem(initialState, new SuccessorsGenerator(), o -> false, new HeuristicCalculator());
        Search search = new HillClimbingSearch();
        try {
            SearchAgent agent = new SearchAgent(problem, search);
            ((State)search.getGoalState()).print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
