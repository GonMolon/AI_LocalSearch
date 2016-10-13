package LocalSearch;

import IA.Azamon.Paquete;

public class InitialStateFactory {

    public static State generateInitialState(Problem problem) {
        State state = new State(problem);
        i_generate(0, state, problem);
        return state;
    }

    private static boolean i_generate(int i, State state, Problem problem) {
        if(i == problem.totalPackages()) {
            state.print();
            return true;
        } else {
            Paquete act = problem.getPackage(i);
            for(int days = act.getPrioridad()*2+1; days >= 1; --days) {
                for(int offerID : problem.offers[days-1]) {
                    if(state.movePackage(i, offerID)) {
                        boolean found = i_generate(i+1, state, problem);
                        if(found) {
                            return true;
                        }
                        state.offer_of_package[i] = -1;
                    }
                }
            }
            return false;
        }
    }

    private static boolean i_generate2(int i, State state, Problem problem) {
        if(i == problem.totalPackages()) {
            state.print();
            return true;
        } else {
            Paquete act = problem.getPackage(i);
            for(int days = 1; days <= act.getPrioridad()*2+1; ++days) {
                for(int offerID : problem.offers[days-1]) {
                    if(state.movePackage(i, offerID)) {
                        boolean found = i_generate2(i+1, state, problem);
                        if(found) {
                            return true;
                        }
                        state.offer_of_package[i] = -1;
                    }
                }
            }
            return false;
        }
    }

}
