package LocalSearch;

import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;

public class InitialStateFactory {

    public static State generate(int n, int prop, int seed) {
        Paquetes paquetes = new Paquetes(n, seed);
        Transporte ofertas = new Transporte(paquetes, prop, seed);
        return new State();
    }
}
