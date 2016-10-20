package LocalSearch;

import aima.search.framework.Problem;
import aima.search.framework.Search;
import aima.search.framework.SearchAgent;
import aima.search.informed.HillClimbingSearch;
import aima.search.informed.SimulatedAnnealingSearch;

public class Main {
    public static void main(String[] args) {
        int out = Integer.parseInt(args[0]);
        int seed = Integer.parseInt(args[1]);
        int n = Integer.parseInt(args[2]);
        int fel = Integer.parseInt(args[4]);
        int ope = Integer.parseInt(args[5]);
        int alg = Integer.parseInt(args[7]);
        int ite = Integer.parseInt(args[8]);
        int ste = Integer.parseInt(args[9]);
        int k = Integer.parseInt(args[10]);
        double prop = Double.parseDouble(args[3]), lam = Double.parseDouble(args[11]);
        int gen = Integer.parseInt(args[6]);
        Statement statement;
        State initialState;
        State.set_Happiness(fel);
        if(gen == 0) {
            Statement.generateStatement(n, prop, seed, SortMode.OPTIMUM);
            initialState = InitialStateFactory.generateInitialState(Statement.getStatement(),  true);
        } else {
            Statement.generateStatement(n, prop, seed, SortMode.RANDOM);
            initialState = InitialStateFactory.generateInitialState(Statement.getStatement(),  false);
        }
        Search search;
        Problem problem;
        if(alg == 0) {
            problem = new Problem(initialState, new SuccessorsGenerator(ope == 0), o -> false, new HeuristicCalculator());
            search = new HillClimbingSearch();
        } else {
            problem = new Problem(initialState, new SuccessorGenerator(ope == 0), o -> false, new HeuristicCalculator());
            search = new SimulatedAnnealingSearch(ite,ste,k,lam);
        }
        long start_time = System.currentTimeMillis();
        try {
            SearchAgent agent = new SearchAgent(problem, search);
            long total_time = System.currentTimeMillis() - start_time;
            if(out == 0) ((State)search.getGoalState()).print();
            else System.out.println(total_time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
