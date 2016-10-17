package LocalSearch;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;

public class Main {
    public static void main(String[] args) {
        Statement statement = new Statement(1000, 1.2, -123);
        State initialState = InitialStateFactory.generateInitialState(statement);
        if(initialState.valid) {
            Problem problem = new Problem(initialState, new SuccesorGenerator(), o -> false, new HeuristicCalculator());
            Search search = new HillClimbingSearch();
            try {
                SearchAgent agent = new SearchAgent(problem, search);
                agent.getActions().forEach(System.out::println);
                ((State)search.getGoalState()).print();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Initial solution not found");
        }
    }
}
