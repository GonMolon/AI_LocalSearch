package LocalSearch;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.framework.TreeSearch;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;
import aima.search.uninformed.BreadthFirstSearch;

import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        int seed = ThreadLocalRandom.current().nextInt();
        System.out.println("Seed: " + seed);
        Statement statement = new Statement(10, 1.2, seed);
        State initialState = InitialStateFactory.generateInitialState(statement);

        initialState.print();
        Problem problem = new Problem(initialState, new SuccesorGenerator(), o -> false, new HeuristicCalculator());

        Search search = new HillClimbingSearch();
        try {
            System.out.println("########trace1");
            SearchAgent agent = new SearchAgent(problem, search);
            System.out.println("########trace2");
            agent.getActions().forEach(System.out::println);
            ((State)search.getGoalState()).print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
