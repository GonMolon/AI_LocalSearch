package LocalSearch;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.TreeSearch;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.search.uninformed.BreadthFirstSearch;

public class Main {
    public static void main(String[] args) {
        Statement statement = new Statement(10, 1.2, 10);
        State initialState = InitialStateFactory.generateInitialState(statement);
        Problem problem = new Problem(initialState, new SuccesorGenerator(), o -> false, new HeuristicCalculator());
        Search search = new HillClimbingSearch();
        try {
            SearchAgent agent = new SearchAgent(problem, search);
            agent.getActions().forEach(System.out::println);
            ((State)search.getGoalState()).print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
