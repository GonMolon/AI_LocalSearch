package LocalSearch;

import IA.Azamon.Paquete;

public class InitialStateFactory {

    public static State generateInitialState(Statement statement, boolean originalFunc) {
        State state = new State(statement);
        if(originalFunc) {
            i_generate(0, state, statement);
        } else {
            i_generate_alt(0, state, statement);
        }
        return state;
    }

    private static boolean i_generate(int i, State state, Statement statement) {
        if(i == statement.totalPackages()) {
            state.valid = true;
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

    private static boolean i_generate_alt(int i, State state, Statement statement) {
        if(i == statement.totalPackages()) {
            state.valid = true;
            return true;
        } else {
            Paquete act = statement.getPackage(i);
            for(int days = 1; days <= act.getPrioridad()*2+1; ++days) {
                for(int offerID : statement.offers[days-1]) {
                    if(state.movePackage(i, offerID)) {
                        boolean found = i_generate_alt(i+1, state, statement);
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
