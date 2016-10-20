package LocalSearch;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

public class Main {
    public static void main(String[] args) {
        int n = 100;
        double prop = 1.2;
        int seed = 1234;
        boolean mode = true;
        Statement statement;
        State initialState;
        if(mode) {
            Statement.generateStatement(n, prop, seed, SortMode.OPTIMUM);
            initialState = InitialStateFactory.generateInitialState(Statement.getStatement(),  true);
        } else {
            Statement.generateStatement(n, prop, seed, SortMode.RANDOM);
            initialState = InitialStateFactory.generateInitialState(Statement.getStatement(),  false);
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
