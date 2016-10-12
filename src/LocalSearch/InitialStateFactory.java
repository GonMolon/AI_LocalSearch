package LocalSearch;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;

import java.util.Comparator;

public class InitialStateFactory {

    public static State generateInitialState(int n, int prop, int seed) {
        Paquetes paquetes = new Paquetes(n, seed);
        paquetes.sort(new Comparator<Paquete>() {
            @Override
            public int compare(Paquete a, Paquete b) {
                if(a.getPrioridad() < b.getPrioridad()) {
                    return 1;
                } else if(a.getPrioridad() > b.getPrioridad()) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });
        Transporte ofertas = new Transporte(paquetes, prop, seed);
        ofertas.sort(new Comparator<Oferta>() {
            @Override
            public int compare(Oferta a, Oferta b) {
                if(a.getDias() < b.getDias()) {
                    return 1;
                } else if(a.getDias() > b.getDias()) {
                    return -1;
                } else {
                    if(a.getPrecio() < b.getPrecio()) {
                        return 1;
                    } else if(a.getPrecio() > b.getPrecio()) {
                        return -1;
                    } else {
                        return 0;
                    }
                }
            }
        });
        int[] distribution = new int[paquetes.size()];
        int[] weights = new int[ofertas.size()];
        for(int i = 0; i < weights.length; ++i) {
            weights[i] = 0;
        }
        return new State();
    }

    private static void i_generate(int i, int j, int[] solution, int[]repart) {
        if(i == solution.length) {
            System.console().printf("hello");
        } else {

        }
    }
}
