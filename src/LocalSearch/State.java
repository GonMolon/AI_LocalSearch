package LocalSearch;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import aima.search.framework.Successor;

public class State {

    static private Problem problem;

    static private float HAPPINESS_RELATION = 2; //2€ for each day that the package arrives earlier

    // Contains the offer's index to which a package is assigned
    protected int[] offer_of_package;
    // Contains the sum of weights of the packages assigned to an offer
    protected float[] weight_of_offer;

    private  double cost;

    public State(Problem problem) {
        if(State.problem == null) {
            State.problem = problem;
        }
        weight_of_offer = new float[problem.totalOffers()];
        offer_of_package = new int[problem.totalPackages()];
        cost = 0;
        for(int id = 0; id < offer_of_package.length; ++id) { //By default, a package is not assigned
            offer_of_package[id] = -1;
        }
    }

    public State(State from) {
        this.offer_of_package = from.offer_of_package.clone();
        this.weight_of_offer = from.weight_of_offer.clone();
        this.cost = from.cost;
    }

    public boolean movePackage(int packageID, int offerID) {
        Paquete p = problem.getPackage(packageID);
        Oferta o = problem.getOffer(offerID);
        if(o.getDias() <= p.getPrioridad()*2+1 && weight_of_offer[offerID] + p.getPeso() <= o.getPesomax()) {
            if(offer_of_package[packageID] != -1) {
                weight_of_offer[offer_of_package[packageID]] -= p.getPeso();
                cost -= getPrice(problem.getOffer(offer_of_package[packageID])) * p.getPeso();
                updateHappiness(p, problem.getOffer(offer_of_package[packageID]), false);
            }
            cost += getPrice(o) * p.getPeso();
            updateHappiness(p, o, true);
            weight_of_offer[offerID] += p.getPeso();
            offer_of_package[packageID] = offerID;
            return true;
        } else {
            return false;
        }
    }

    public boolean swapPackage(int packageIDa, int packageIDb) {
        Paquete p1 = problem.getPackage(packageIDa);
        Paquete p2 = problem.getPackage(packageIDb);
        int offerID1 = offer_of_package[packageIDa];
        int offerID2 = offer_of_package[packageIDb];
        Oferta o1 = problem.getOffer(offerID1);
        Oferta o2 = problem.getOffer(offerID2);
        if(o1.getDias() <= p2.getPrioridad()*2+1 && o2.getDias() <= p1.getPrioridad()*2+1 &&
                weight_of_offer[offerID1] - p1.getPeso() + p2.getPeso() < o1.getPesomax() &&
                weight_of_offer[offerID2] - p2.getPeso() + p1.getPeso() < o2.getPesomax()) {
            weight_of_offer[offerID1] += -p1.getPeso() + p2.getPeso();
            weight_of_offer[offerID2] += -p2.getPeso() + p1.getPeso();
            cost = cost - p1.getPeso() * getPrice(o1) - p2.getPeso() * getPrice(o2);
            cost = cost + p1.getPeso() * getPrice(o2) + p2.getPeso() * getPrice(o1);
            updateHappiness(p1, o1, false);
            updateHappiness(p2, o2, false);
            updateHappiness(p1, o2, true);
            updateHappiness(p2, o1, true);
            int aux = offer_of_package[packageIDa];
            offer_of_package[packageIDa] = offer_of_package[packageIDb];
            offer_of_package[packageIDb] = aux;
            return true;
        } else {
            return false;
        }
    }

    private double getPrice(Oferta o) {
        if(o.getDias() == 3 || o.getDias() == 4) {
            return o.getPrecio() + 0.25;
        } else if(o.getDias() == 5) {
            return o.getPrecio() + 0.5;
        } else {
            return o.getPrecio();
        }
    }

    private void updateHappiness(Paquete p, Oferta o, boolean add) {
        double happiness = (p.getPrioridad()*2) - o.getDias();
        if(happiness > 0) {
            if(add) {
                cost -= happiness * HAPPINESS_RELATION;
            } else {
                cost += happiness * HAPPINESS_RELATION;
            }
        }
    }

    public void print() {
        System.out.println("State:");
        System.out.println("Distribution: ");
        for(int i = 0; i < problem.totalPackages(); ++i) {
            Paquete paquete = problem.getPackage(i);
            System.out.print(i + ": ");
            System.out.println(offer_of_package[i]);
        }
        System.out.println("Summary: ");
        for(int i = 0; i < problem.totalOffers(); ++i) {
            Oferta oferta = problem.getOffer(i);
            System.out.print(i + ": ");
            System.out.println(weight_of_offer[i] + "/" + oferta.getPesomax() + " -> (" + weight_of_offer[i]*100/oferta.getPesomax() + ")");
        }
        System.out.println("COST = " + getCost());
    }

    public Problem getProblem() {
        return problem;
    }

    public double getCost() {
        return cost;
    }
}
