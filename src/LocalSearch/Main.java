package LocalSearch;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {
    public static void main(String[] args) {
        int n = 100;
        double prop = 1.2;
        int seed = 1234;
        SearchAgent bestAgent = null;
        State bestState = null;
        for(SortMode sortMode : SortMode.values()) {
            Statement statement = new Statement(n, prop, seed, sortMode);
            for(int i = 0; i <= 1; ++i) {
                State initialState = InitialStateFactory.generateInitialState(statement,  i == 0);
                System.out.println(sortMode.name() + " sorting" + " and " + (i == 0 ? "ORIGINAL" : "ALTERNATIVE") + " generation: ");
                if(initialState.valid) {
                    initialState.print();
                    Problem problem = new Problem(initialState, new SuccessorGenerator(), o -> false, new HeuristicCalculator());
                    Search search = new HillClimbingSearch();
                    try {
                        SearchAgent agent = new SearchAgent(problem, search);
                        if(bestState == null || ((State)search.getGoalState()).getCost() < bestState.getCost()) {
                            bestAgent = agent;
                            bestState = ((State)search.getGoalState());
                        }
                        System.out.print(" ---> ");
                        ((State)search.getGoalState()).print();
                        System.out.println();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Initial solution not found for this combination");
                }
            }
        }
        if(bestState != null) {
            //bestAgent.getActions().forEach(System.out::println);
            System.out.println("BEST SOLUTION FOUND: ");
            bestState.print();
        }
    }
}
