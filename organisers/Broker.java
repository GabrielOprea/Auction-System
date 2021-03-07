package organisers;
import clients.Client;
import clients.NaturalPerson;
import exceptions.DuplicateRequestException;
import helper.Color;
import interfaces.Observer;
import interfaces.Subject;
import multithreading.DeleteTask;
import org.apache.commons.math3.util.Pair;
import products.Product;
import java.util.ArrayList;
import java.util.List;

/**
 * The Broker is the one that actively performs the
 * interaction with both the client and the auction house.
 * He is responsible for taking the client's bids and communicating
 * them to the auction house. Also implements the Subject interface,
 * for better controlling and communicating with Observer objects
 */
public class Broker extends Employee implements Subject {

    //The clients are kept in a list of observers
    ArrayList<Observer> observers;

    //An additional list kept by the broker with information about each observer
    ArrayList<Information> entryInfo;

    //cash obtained from commissions
    private double cash;
    private int noWonAuctions;

    //Threshold for comparing two double numbers
    public static final double THRESHOLD = 1;

    /**
     * Constructor that sets the broker's
     * cash and won auctions to 0
     */
    public Broker() {
        cash = 0;
        noWonAuctions = 0;
        observers = new ArrayList<>();
        entryInfo = new ArrayList<>();
    }

    /**
     * Constructor that explicitly sets the broker's name, experience
     * and rating
     * @param name The name of the broker
     * @param yearsOfExperience The broker's experience
     * @param rating The rating of the broker
     */
    public Broker(String name, int yearsOfExperience, double rating) {
        this();
        this.name = name;
        this.yearsOfExperience = yearsOfExperience;
        this.rating = rating;
    }

    /**
     * Returns a runnable object for deleting a specified product
     * from the auction house. The method is used for deleting already
     * sold products
     * @param p The product to be deleted
     * @return a DeleteTask object
     */
    public Runnable deleteAuctionProduct(Product p) {
        return new DeleteTask(p);
    }

    /**
     * Resets all the information kept by the broker
     * for his clients
     */
    public void reset() {
        observers.forEach(client->client.update(new Information()));
    }

    /**
     *
     * Applies a commission for every observer
     */
    public void applyCommission() {
        observers.forEach(this::applyCommission);
    }

    /**
     * Applies a commission for a specific observer
     * @param obs the observer to get the commission
     */
    public void applyCommission(Observer obs) {
        int percentageCommission;
        Client crtClient = (Client)obs;
        int noParticipation = crtClient.getNoParticipation();
        /* Gets the specified percentage commission, based on the subclass of the client
        and the number of appearances in an auction */
        if(obs instanceof NaturalPerson) {
            if (noParticipation < 5) {
                percentageCommission = 20;
            }
            else percentageCommission = 15;
        }
        else {
            if(noParticipation < 25) {
                percentageCommission = 25;
            }
            else percentageCommission = 10;
        }

        //Sets the calculated commission as a field for the information object
        obs.getInfo().setCommission(percentageCommission);
    }

    /**
     * Adds an observer in the list
     * @param o the added observer
     */
    @Override
    public void add(Observer o) {
        observers.add(o);
        Information observerInfo = new Information();
        if(o instanceof Client) {
            observerInfo.setName(((Client)o).getName());
        }
        entryInfo.add(observerInfo);
    }

    /**
     * Adds an observer in the list and an information object, describing
     * the maximum sum the observer is willing to pay
     * @param o the observer to be added
     * @param maxPrice the maximum price he is willing to pay
     */
    public void add(Observer o, double maxPrice) {
        add(o);
        /* Set the maximum affordable bid for the corresponding information object
        with the sum received as parameter */
        entryInfo.get(entryInfo.size() - 1).setMaxAffordableBid(maxPrice);
    }

    /**
     * Adds the observer in the list, alongside the Product and maximum price,
     * which are added as fields for the information object
     * @param o the observer to be added
     * @param p the product the observer wants
     * @param maxPrice the maximum price the observer is willing to pay
     * @throws DuplicateRequestException if the same product has already been requested by the same observer
     */
    public void add(Observer o, Product p, double maxPrice) throws DuplicateRequestException {
        for(int i = 0; i < observers.size(); i++) {
            //If the same observer is found in the list, and he requested the same product, throw an exception
            if(observers.get(i).equals(o) && entryInfo.get(i).getDemandedProduct().equals(p))
                throw new DuplicateRequestException();
        }

        //Else, add the observer and set his demanded product accordingly
        add(o, maxPrice);
        entryInfo.get(entryInfo.size() - 1).setDemandedProduct(p);
    }

    /**
     * Method called by the brokers when the auction is started.
     * All the observers are updated with their information held by the broker
     * @param demandedProduct The product demanded by the observers
     */
    public void start(Product demandedProduct) {
        for(int i = 0; i < observers.size(); i++) {
            /* If the current observer wants the demanded product, then update his information, because
            he will participate in the upcoming auction */
            if(entryInfo.get(i).getDemandedProduct().getId() == demandedProduct.getId()) {
                observers.get(i).update(entryInfo.get(i));
            }
        }
    }

    /**
     * Notifies all the observers about the biggest bid for the demanded product
     * @param biggestBid the biggest bid offered by someone
     * @param demandedProduct the product demanded by the observers
     */
    @Override
    public void notifyObservers(double biggestBid, Product demandedProduct) {
        for(int i = 0; i < observers.size(); i++) {
            if(demandedProduct.equals(getInfo(i).getDemandedProduct())) {
                notifyObserver(observers.get(i), biggestBid);
            }
        }
    }

    /**
     * Notifies a specific observer about the biggest bid for the demanded product
     * @param o the Observer to be notified
     * @param bid the maximum bid in the current step of an auction
     */
    @Override
    public void notifyObserver(Observer o, double bid) {
        //Set the maximum bid in the information object, then set this object as a field for the observer
        o.getInfo().setMaxAuctionBid(bid);
        o.update(o.getInfo());
    }

    /**
     * Remove an observer from the list
     * @param o the Observer to be removed
     */
    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    /**
     * Computes the winner of the auction
     * @param bid the winning bid, that is, the biggest bid from all steps
     * @param demandedProduct the demanded product by the participants
     * @return a string representation of the winner
     */
    public String notifyWinner(double bid, Product demandedProduct) {
        int noMaxWonAuctions = -1;
        Observer winner = null;

        /*Apply a percentage commission for the bidders. The sum will be extracted only
        from the winner */
        applyCommission();

        /* Search for the observer who bid the maximum sum. If more are found, then
        the winner will be decided based on the one that won the most auctions */
        for(int i = 0; i < observers.size(); i++) {
            if(demandedProduct.equals(entryInfo.get(i).getDemandedProduct())) {
                //Increment the number of appearances for everyone
                observers.get(i).update(false);
                if(Math.abs(getInfo(i).getCrtBid() - bid) < THRESHOLD){
                    int crtNoWonAuctions = getInfo(i).getNoWonAuctions();
                    if(crtNoWonAuctions > noMaxWonAuctions) {
                        noMaxWonAuctions = crtNoWonAuctions;
                        winner = observers.get(i);
                    }
                }
            }
        }

        //the winner is found
        if(winner != null) {
            //Set the sell price for the product
            demandedProduct.setSellPrice(bid);

            //Take the cash from the commission
            cash += winner.getInfo().getCommission() * bid;
            noWonAuctions++;

            //Notify the winner about the result
            winner.getInfo().setWinner(true);
            winner.update(true);

            //Add the sold product to a separate list, and delete it from the products available
            addSoldProduct(demandedProduct);
            Thread t = new Thread(deleteAuctionProduct(demandedProduct));
            t.start();

            //Return a string representation for the selling process
            String productType = Product.getType(demandedProduct);
            return "\t" + Color.PURPLE + productType + " \"" + demandedProduct.getName() + "\" has been sold to "
                    + winner.getInfo().getName() + " for " + bid + " dollars." + Color.RESET + "\n";
        }

        //If the winner is not a client of this broker return an empty string
        return "";
    }

    /**
     * Method that requests all the clients to bid for the specified product
     * @param auctionProd the product the clients must bid for
     * @return a List of doubles including all bids
     */
    public List<Double> bid(Product auctionProd) {
        List<Double> bids = new ArrayList<>();

        for(int i = 0; i < observers.size(); i++) {
            //Take bids only from observers who want this product
            if(auctionProd.equals(entryInfo.get(i).getDemandedProduct())) {
                //Extract the pair of bid and number of won auctions
                Pair<Double, Integer> crtClientBid = observers.get(i).bid();
                //Include those in the information field for the current observer
                getInfo(i).setCrtBid(crtClientBid.getFirst());
                getInfo(i).setNoWonAuctions(crtClientBid.getSecond());
                //Add the bid to the list
                bids.add(crtClientBid.getFirst());
            }
        }
        return bids;
    }

    /**
     * Adds the sold product to a list of sold products
     * @param product the product to be added
     */
    private void addSoldProduct(Product product) {
        AuctionHouse.getInstance().addSoldProduct(product);
    }

    /**
     * Gets a list of clients from the observer list
     * @return a list of clients
     */
    public List<Client> getClients() {
        List<Client> clientsList = new ArrayList<>();
        observers.forEach(obs -> clientsList.add((Client) obs));
        return clientsList;
    }

    /**
     * Gets the entry info list
     * @return a list of information for each observer
     */
    public List<Information> getEntryInfo() {
        return entryInfo;
    }

    /**
     * Gets the sum of money this broker has obtained
     * @return the broker's cash
     */
    public double getCash() {
        return cash;
    }

    /**
     * Gets the broker's number of won auctions
     * @return the number of won auctions
     */
    public int getNoWonAuctions() {
        return noWonAuctions;
    }

    /**
     * Gets the information corresponding to the observer with the specified index
     * in the observers list
     * @param i the index of the observer
     * @return the information associated with the observer
     */
    private Information getInfo(int i) {
        return observers.get(i).getInfo();
    }
}

