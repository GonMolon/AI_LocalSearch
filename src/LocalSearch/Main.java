package LocalSearch;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {
    public static void main(String[] args) {
        int out = Integer.parseInt(args[0]), seed = Integer.parseInt(args[1]),
                n = Integer.parseInt(args[2]), fel = Integer.parseInt(args[4]),
                ope = Integer.parseInt(args[5]), alg = Integer.parseInt(args[7]),
                ite = Integer.parseInt(args[8]), ste = Integer.parseInt(args[9]),
                k = Integer.parseInt(args[10]);
        double prop = Double.parseDouble(args[3]), lam = Double.parseDouble(args[11]);
        boolean mode = Boolean.parseBoolean(args[6]);
        Statement statement;
        State initialState;
        State.set_Happiness(fel);
        long start_time = System.currentTimeMillis();
        if(mode) {
            Statement.generateStatement(n, prop, seed, SortMode.OPTIMUM);
            initialState = InitialStateFactory.generateInitialState(Statement.getStatement(),  true);
        } else {
            Statement.generateStatement(n, prop, seed, SortMode.RANDOM);
            initialState = InitialStateFactory.generateInitialState(Statement.getStatement(),  false);
        }
        Search search = null;
        Problem problem = null;
        if(alg == 0) {
            problem = new Problem(initialState, new SuccessorsGenerator(), o -> false, new HeuristicCalculator());
            search = new HillClimbingSearch();
        } else {
            problem = new Problem(initialState, new SuccessorGenerator(), o -> false, new HeuristicCalculator());
            search = new SimulatedAnnealingSearch(ite,ste,k,lam);
        }
        long end_time = System.currentTimeMillis();
        try {
            SearchAgent agent = new SearchAgent(problem, search);
            long total_time = end_time - start_time;
            if(out == 0) ((State)search.getGoalState()).print();
            else System.out.println(total_time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
