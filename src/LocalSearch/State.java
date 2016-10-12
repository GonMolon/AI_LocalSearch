package LocalSearch;

import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;

public class State {

    static private Paquetes packages;
    static private Transporte offers;

    // Contains the offer's index to which a package is assigned
    private int[] offer_of_package;
    // Contains the sum of weights of the packages assigned to an offer
    private int[] weight_of_offer;

    public State(Paquetes packages, Transporte offers) {
        if(State.packages == null || State.offers == null) {
            State.packages = packages;
            State.offers = offers;
        }
        offer_of_package = new int[packages.size()];
        weight_of_offer = new int[offers.size()];
    }

    public State(State from) {
        this.offer_of_package = from.offer_of_package.clone();
        this.weight_of_offer = from.weight_of_offer.clone();
    }

    public void setOfferOfPackage(int package_index, int offer_index) {
        offer_of_package[package_index] = offer_index;
    }

    public int getOfferOfPackage(int package_index) {
        return offer_of_package[package_index];
    }

    public void setWeightOfOffer(int offer_index, int weight) {
        weight_of_offer[offer_index] = weight;
    }

    public int getWeightOfOffer(int offer_index) {
        return weight_of_offer[offer_index];
    }
}
