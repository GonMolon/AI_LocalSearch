package LocalSearch;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;

public class State {

    private Statement statement;

    static private float HAPPINESS_RELATION = 10; //2€ for each day that the package arrives earlier

    // Contains the offer's index to which a package is assigned
    protected int[] offer_of_package;
    // Contains the sum of weights of the packages assigned to an offer
    protected float[] weight_of_offer;

    private  double cost;

    public boolean valid = false;

    public State(Statement statement) {
        this.statement = statement;
        weight_of_offer = new float[statement.totalOffers()];
        offer_of_package = new int[statement.totalPackages()];
        cost = 0;
        for(int id = 0; id < offer_of_package.length; ++id) { //By default, a package is not assigned
            offer_of_package[id] = -1;
        }
    }

    public State(State from) {
        this.statement = from.statement;
        this.offer_of_package = from.offer_of_package.clone();
        this.weight_of_offer = from.weight_of_offer.clone();
        this.cost = from.cost;
    }

    public boolean movePackage(int packageID, int offerID) {
        Paquete p = statement.getPackage(packageID);
        Oferta o = statement.getOffer(offerID);
        if(o.getDias() <= p.getPrioridad()*2+1 && weight_of_offer[offerID] + p.getPeso() <= o.getPesomax()) {
            if(offer_of_package[packageID] != -1) {
                weight_of_offer[offer_of_package[packageID]] -= p.getPeso();
                cost -= getPrice(statement.getOffer(offer_of_package[packageID])) * p.getPeso();
                updateHappiness(p, statement.getOffer(offer_of_package[packageID]), false);
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
        Paquete p1 = statement.getPackage(packageIDa);
        Paquete p2 = statement.getPackage(packageIDb);
        int offerID1 = offer_of_package[packageIDa];
        int offerID2 = offer_of_package[packageIDb];
        Oferta o1 = statement.getOffer(offerID1);
        Oferta o2 = statement.getOffer(offerID2);
        if(o1.getDias() <= p2.getPrioridad()*2+1 && o2.getDias() <= p1.getPrioridad()*2+1 &&
                weight_of_offer[offerID1] - p1.getPeso() + p2.getPeso() < o1.getPesomax() &&
                weight_of_offer[offerID2] - p2.getPeso() + p1.getPeso() < o2.getPesomax()) {
            weight_of_offer[offerID1] += -p1.getPeso() + p2.getPeso();
            weight_of_offer[offerID2] += -p2.getPeso() + p1.getPeso();
            cost -= p1.getPeso() * getPrice(o1) - p2.getPeso() * getPrice(o2);
            cost += p1.getPeso() * getPrice(o2) + p2.getPeso() * getPrice(o1);
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

    public boolean isSolution(){
        double[] left_weight = new double[statement.totalOffers()];
        for (int i = 0; i < left_weight.length; ++i) {
            left_weight[i] = statement.getOffer(i).getPesomax();
        }
        boolean ok = true;
        for (int i = 0; i < offer_of_package.length && ok; ++i) {
            Paquete paquete = statement.getPackage(i);
            Oferta oferta = statement.getOffer(offer_of_package[i]);
            left_weight[offer_of_package[i]] -= paquete.getPeso();
            ok = (left_weight[offer_of_package[i]] >= 0 && oferta.getDias() <= paquete.getPrioridad()*2+1);
        }
        return ok;
    }

    public void print() {
        System.out.print(getCost());
    }
    private void print1() {
        /*System.out.println("State:");
        System.out.println("Distribution: ");
        for(int i = 0; i < statement.totalPackages(); ++i) {
            System.out.print(i + ": ");
            System.out.println(offer_of_package[i]);
        }
        *//*System.out.println("Summary: ");
        for(int i = 0; i < statement.totalOffers(); ++i) {
            Oferta oferta = statement.getOffer(i);
            System.out.print(i + ": ");
            System.out.println(weight_of_offer[i] + "/" + oferta.getPesomax() + " -> (" + weight_of_offer[i]*100/oferta.getPesomax() + ")");
        }*/
    }

    private void print2() {
        System.out.println("--------STATE---------");
        if (!isSolution()) {
            System.out.println("THIS SOLUTION IS INCORRECT SOMETHING WENT REALLY WRONG OMGOMGOMG");
        }
        System.out.print("Package ID:\t");
        for(int i = 0; i < statement.totalPackages(); ++i) {
            System.out.format("%4d", i);
        }
        System.out.println();
        System.out.print("Offer ID:\t");
        for(int i = 0; i < statement.totalPackages(); ++i) {
            System.out.format("%4d", offer_of_package[i]);
        }
        System.out.println();
        System.out.println();

        for(int i = 0; i < statement.totalOffers(); ++i) {
            Oferta oferta = statement.getOffer(i);
            System.out.print(i + ": ");
            System.out.println(weight_of_offer[i] + "/" + oferta.getPesomax());
        }
        System.out.println();

        System.out.println("Total cost: " + getCost());
        System.out.println("--------/STATE--------");

    }

    public Statement getProblem() {
        return statement;
    }

    public static void set_Happiness(float Happiness) {
        HAPPINESS_RELATION = Happiness;
    }

    public double getCost() {
        return cost;
    }
}
