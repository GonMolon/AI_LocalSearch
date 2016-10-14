package LocalSearch;

import IA.Azamon.Paquete;

public class InitialStateFactory {

    public static State generateInitialState(Statement statement) {
        State state = new State(statement);
        i_generate2(0, state, statement);
        return state;
    }

    private static boolean i_generate(int i, State state, Statement statement) {
        if(i == statement.totalPackages()) {
            state.print();
            return true;
        } else {
            Paquete act = statement.getPackage(i);
            for(int days = act.getPrioridad()*2+1; days >= 1; --days) {
                for(int offerID : statement.offers[days-1]) {
                    if(state.movePackage(i, offerID)) {
                        boolean found = i_generate(i+1, state, statement);
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

    private static boolean i_generate2(int i, State state, Statement statement) {
        if(i == statement.totalPackages()) {
            state.print();
            return true;
        } else {
            Paquete act = statement.getPackage(i);
            for(int days = 1; days <= act.getPrioridad()*2+1; ++days) {
                for(int offerID : statement.offers[days-1]) {
                    if(state.movePackage(i, offerID)) {
                        boolean found = i_generate2(i+1, state, statement);
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
