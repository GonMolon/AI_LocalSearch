package LocalSearch;

import IA.Azamon.Oferta;
import IA.Azamon.Paquete;
import IA.Azamon.Paquetes;
import IA.Azamon.Transporte;

import javax.jws.soap.SOAPBinding;
import java.util.Comparator;

public class Statement {

    private static Statement statement;

    private Paquetes packages;
    private Transporte transport;
    protected int[][] offers;
    private double totalWeight = 0;
    private double totalOfferedWeight = 0;

    public static void generateStatement(int n, double prop, int seed, SortMode sortMode) {
        statement = new Statement(n, prop, seed, sortMode);
    }

    private Statement(int n, double prop, int seed, SortMode sortMode) {
        packages = new Paquetes(n, seed);
        packages.sort(new Comparator<Paquete>() {
            @Override
            public int compare(Paquete a, Paquete b) {
                if(a.getPrioridad() < b.getPrioridad()) {
                    return -1;
                } else if(a.getPrioridad() > b.getPrioridad()) {
                    return 1;
                } else {
                    return 0;
                }
            }
        });
        transport = new Transporte(packages, prop, seed);
        transport.sort(new Comparator<Oferta>() {
            @Override
            public int compare(Oferta a, Oferta b) {
                if(a.getDias() < b.getDias()) {
                    return -1;
                } else if(a.getDias() > b.getDias()) {
                    return 1;
                } else {
                    if(sortMode != SortMode.RANDOM) {
                        if(a.getPrecio() < b.getPrecio()) {
                            return sortMode == SortMode.OPTIMUM ? -1 : 1;
                        } else if(a.getPrecio() > b.getPrecio()) {
                            return sortMode == SortMode.OPTIMUM ? 1 : -1;
                        } else {
                            return 0;
                        }
                    } else {
                        return 0;
                    }
                }
            }
        });
        for(Paquete paquete : packages) {
            totalWeight += paquete.getPeso();
        }
        offers = new int[5][];
        for(int i = 0; i < 5; ++i) {
            offers[i] = new int[0];
        }
        int t = 0;
        int T = 0;
        for(int i = 0; i < transport.size(); ++i) {
            ++t;
            ++T;
            Oferta offer = transport.get(i);
            totalOfferedWeight += offer.getPesomax();
            if(i == transport.size()-1 || offer.getDias() != transport.get(i+1).getDias()) {
                offers[offer.getDias()-1] = new int[t];
                int k = 0;
                for(int j = T-t; j < T; ++j) {
                    offers[offer.getDias()-1][k] = j;
                    ++k;
                }
                t = 0;
            }
        }
    }

    public static Statement getStatement() {
        return statement;
    }

    public int totalPackages() {
        return packages.size();
    }

    public int totalOffers() {
        return transport.size();
    }

    public int totalOffersFromDays(int days) {
        return offers[days-1].length;
    }

    public Oferta getOffer(int ID) {
        return transport.get(ID);
    }

    public Paquete getPackage(int ID) {
        return packages.get(ID);
    }

    private void print() {
        print2();
    }

    private void print1() {
        System.out.println("PACKAGES");
        int id = 0;
        int total_weight = 0;
        for(Paquete paquete : packages) {
            System.out.println("Package ID: " + id++);
            System.out.println("    " + "Priority: " + paquete.getPrioridad());
            System.out.println("    " + "Weight: " + paquete.getPeso());
            total_weight += paquete.getPeso();
        }
        System.out.println("Total weight: " + total_weight + "kg");
        System.out.println("OFFERS");
        for(int days = 0; days < 5; ++days) {
            System.out.println(days+1 + " days: ");
            for(int offerID : offers[days]) {
                Oferta oferta = getOffer(offerID);
                System.out.println("  " + "Offer ID: " + offerID);
                System.out.println("    " + "Price (kg): " + oferta.getPrecio());
                System.out.println("    " + "Max weight : " + oferta.getPesomax());
            }
        }
        /*
        System.out.println("TOTAL WEIGHT = " + totalWeight);
        System.out.println("TOTAL OFFERED WEIGHT = " + totalOfferedWeight);
        System.out.println("REAL PROPORTION = " + totalOfferedWeight/totalWeight);
        */
    }

    private void print2() {
        System.out.println("--------STATEMENT---------");
        int id = 0;
        float total_weight = 0;
        System.out.print("Package ID:\t");
        for(Paquete paquete : packages) {
            System.out.format("%5d",id++);
            total_weight += paquete.getPeso();
        }
        System.out.println();
        System.out.print("Priority:\t");
        for(Paquete paquete : packages) {
            System.out.format("%5d",paquete.getPrioridad());
        }
        System.out.println();
        System.out.print("Weight:\t\t");
        for(Paquete paquete : packages) {
            System.out.format("%5.1f",paquete.getPeso());
        }
        System.out.println();
        System.out.println(total_weight + " total weight");
        System.out.println();


        id = 0;
        System.out.print("Offer ID:\t");
        for (Oferta offer : transport) {
            System.out.format("%5d",id++);
        }
        System.out.println();
        System.out.print("Days:\t\t");
        for (Oferta offer : transport) {
            System.out.format("%5d",offer.getDias());
        }
        System.out.println();
        System.out.print("Price(kg):\t");
        for (Oferta offer : transport) {
            System.out.format("%5.2f",offer.getPrecio());
        }
        System.out.println();
        System.out.print("Max weight:\t");
        for (Oferta offer : transport) {
            System.out.format("%5.1f",offer.getPesomax());
        }
        System.out.println();
        System.out.println("--------/STATEMENT--------");
    }
}
