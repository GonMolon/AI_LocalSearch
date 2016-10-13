package LocalSearch;

import aima.search.informed.HillClimbingSearch;

public class Main {
    public static void main(String[] args) {
        Problem problem = new Problem(15, 1, 10);
        State state = InitialStateFactory.generateInitialState(problem);
        HillClimbingSearch search = new HillClimbingSearch();
        try {
            search.search(new aima.search.framework.Problem(state, new SuccesorGenerator(), new Goal(), new HeuristicCalculator()));
            ((State)search.getGoalState()).print();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
