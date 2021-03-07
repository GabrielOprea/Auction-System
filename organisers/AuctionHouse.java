package organisers;
import clients.Client;
import exceptions.DuplicateRequestException;
import exceptions.InvalidProductRequest;
import exceptions.UnknownProductException;
import helper.Color;
import products.Product;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The AuctionHouse class is based on the Singleton Design
 * Pattern and holds all the fields necessary for the auctions
 */
public class AuctionHouse {
    private static AuctionHouse uniqueINSTANCE = null;

    //The products available in the house
    private List<Product> products;

    //The products sold
    private List<Product> soldProducts;

    private List<Client> clients;
    private List<Auction> auctions;
    private List<Employee> employees;

    //Logger used for printing messages in each auction
    private final Logger logger = Logger.getLogger(AuctionHouse.class.getName());
    private int organisedAuctions;

    //Lock used for preventing race conditions between various Threads
    private final Lock lock = new ReentrantLock();
    Condition wasAdded = lock.newCondition();

    /**
     * Creates an unique instance for this Auction House or
     * returns the existent one
     * @return the unique instance of the auction house
     */
    public static AuctionHouse getInstance() {
        if(uniqueINSTANCE == null) {
            uniqueINSTANCE = new AuctionHouse();
        }
        return uniqueINSTANCE;
    }

    /**
     * Private constructor for preventing instantiation
     */
    private AuctionHouse() {
        resetAll();
    }


    /**
     * Resets all the attributes to their initial values
     */
    public void resetAll() {

        products = new ArrayList<>();
        clients = new ArrayList<>();
        auctions = new ArrayList<>();
        employees = new ArrayList<>();
        soldProducts = new ArrayList<>();
        organisedAuctions = 0;
    }

    /**
     * Adds a new product to the list.
     * @param p the product to be added
     */
    public void add(Product p) {
        //Obtains a lock
        lock.lock();

        if(products.contains(p)) {
            lock.unlock();
            return;
        }

        //Adds the product and signals that a new product has been added
        products.add(p);
        wasAdded.signalAll();

        //Releases the lock
        lock.unlock();
    }

    /**
     * Adds a new client in the Auction House
     * @param c the client to be added
     */
    public void add(Client c) {
        if(!clients.contains(c)) {
            c.setId(clients.size());
            clients.add(c);
        }
    }

    /**
     * Adds a new employee in the house
     * @param e the employee to be added
     */
    public void add(Employee e) {
        employees.add(e);
    }

    /**
     * Adds a new available auction
     * @param auction the new auction
     */
    public void add(Auction auction) {
        auctions.add(auction);
    }

    /**
     * Adds a product to the list of sold products
     * @param product the newly sold product
     */
    public void addSoldProduct(Product product) {
        soldProducts.add(product);
    }

    /**
     * Deletes a product from the list of available products
     * @param p the product to be deleted
     */
    public void deleteProduct(Product p) {
        //Obtains the lock
        lock.lock();

        //If the list does not contain the product, then wait until a new one is added
        try {
            while(!products.contains(p)) {
                wasAdded.await();
            }
            products.remove(p);
        } catch(InterruptedException e) {
            //Interrupt the thread in case of problems
            Thread.currentThread().interrupt();
        } finally {
            //Release the lock for other threads
            lock.unlock();
        }
    }

    /**
     * Method for reading all the products, used by the clients
     * when signing up
     */
    public void readProducts() {
        //Obtains a lock to avoid race conditions
        lock.lock();

        products.forEach(prod -> logger.log(Level.ALL, "Product: {0} ", prod));

        //Releases the lock for either an add or a delete task
        lock.unlock();
    }

    /**
     * Register a request by a specific client, for a specific product, with the maximum price specified. If the
     * product requested has an auction that can be started, then start an auction, and log a string representation
     * of the auction process
     * @param c The client that makes the request
     * @param productId the product requested by him
     * @param maxPrice the maximum price the client is willing to offer for the product
     * @throws UnknownProductException if the product is not found
     * @throws DuplicateRequestException if the request has already been sent
     * @throws InvalidProductRequest if the sum offered is too small
     */
    public void request(Client c, int productId, double maxPrice) throws UnknownProductException, DuplicateRequestException, InvalidProductRequest {

        //Add the client and find the requested product
        add(c);
        Product demandedProduct;
        demandedProduct = findProduct(productId, maxPrice);

        //Check whether the request for this product is valid, then assign a broker to the client
        checkValidRequest(c, demandedProduct);
        assignRandomBroker(c, demandedProduct, maxPrice);

        //Search for an auction for that specific product
        for(Auction auction : auctions) {
            if(auction.getIdProduct() == productId) {
                //If an auction is found, increment the participants
                auction.incrementParticipants();

                //If the auction can be started, then start it
                if(auction.canStart()) {
                    //Initialise a new string builder to be printed
                    StringBuilder sb = new StringBuilder();

                    //Notify the brokers about the auction
                    startBrokers(demandedProduct);
                    organisedAuctions++;
                    appendAuctionInfo(sb, demandedProduct);

                    //Compute the maximum bid at each step
                    double biggestBid = 0;
                    int maxSteps = auction.getNoMaxSteps();
                    for(int step = 0; step < maxSteps; step++) {
                        //Request the bids from the brokers
                        biggestBid = updateBiggestBid(biggestBid, demandedProduct, step, sb);
                    }
                    //If the maximum bid is too small, then the product is not sold
                    if(biggestBid < demandedProduct.getMinPrice()) {
                        appendNotSoldMessage(sb, demandedProduct, biggestBid);
                    } else sb.append(updateWinner(biggestBid, demandedProduct));
                    //If the product is sold, print its information
                    String auctionInfo = sb.toString();
                    logger.log(Level.INFO, auctionInfo);

                    //Remove the auction and tell the brokers to close the communication to the clients
                    auctions.remove(auction);
                    reset();
                }
                break;
            }
        }
    }

    //Appends the information about the auction number and the product's worth to the string builder
    private void appendAuctionInfo(StringBuilder sb, Product demandedProduct) {
        sb.append("Auction ")
                .append(organisedAuctions)
                .append("\n")
                .append("\t" + Color.PURPLE + "Auction for ")
                .append(demandedProduct.getName())
                .append(" worth ")
                .append(demandedProduct.getMinPrice())
                .append("$:").append(Color.RESET)
                .append("\n");
    }

    //Appends the message about the minimum selling price and the biggest bid for this product to the string builder
    private void appendNotSoldMessage(StringBuilder sb, Product demandedProduct, double biggestBid) {
        String productType = Product.getType(demandedProduct);
        sb.append("\t" + Color.PURPLE)
                .append(productType).append(" ")
                .append(demandedProduct.getName())
                .append(" was not sold! Biggest bid: ")
                .append(biggestBid)
                .append("$, Min sell price: ")
                .append(demandedProduct.getMinPrice())
                .append("$.\n")
                .append(Color.RESET);
    }

    /* Gets the bids from each broker, and update the biggest bid with the maximum bid from them, then concatenate
    a String to the StringBuilder */
    private double updateBiggestBid(double biggestBid, Product demandedProduct, int step, StringBuilder sb) {

        //Print message about the number of steps
        int i = 0;
        sb.append(Color.CYAN + "\t\tStep ").append(step).append(":").append(Color.RESET).append("\n");
        for (Broker br : getBrokers()) {
            //The bids obtained by each broker
            List<Double> bids = br.bid(demandedProduct);
            for(Double bid : bids) {
                //Print message about the number of bids
                i++;
                sb.append(Color.BLUE + "\t\t\tBid ").append(i).append(": ").append(bid).append("$").append(Color.RESET).append("\n");
            }
            double maxBidOfBroker = 0;
            if(!bids.isEmpty()) maxBidOfBroker = Collections.max(bids);
            //Extract the biggest bid
            biggestBid = Math.max(biggestBid, maxBidOfBroker);
        }
        //Return this bid and update the brokers about the biggest bid
        updateBrokers(biggestBid, demandedProduct);
        sb.append("\n");
        return biggestBid;
    }

    //Checks if a request is valid. That is, if the product was not requested before by the same client
    private void checkValidRequest(Client c, Product demandedProduct) throws DuplicateRequestException {
        for(Broker br : getBrokers()) {
            List<Client> brokerClients = br.getClients();
            List<Information> entryInfo = br.getEntryInfo();
            for (int i = 0; i < brokerClients.size(); i++) {
                if (c.equals(brokerClients.get(i)) && entryInfo.get(i).getDemandedProduct().equals(demandedProduct)) {
                    throw new DuplicateRequestException();
                }
            }
        }
    }

    //Assigns a random broker for the specified client
    private void assignRandomBroker(Client c, Product demandedProduct, double maxPrice) throws DuplicateRequestException {
        List<Broker> brokers = getBrokers();

        int randomBrokerId = (new Random()).nextInt(brokers.size());
        brokers.get(randomBrokerId).add(c, demandedProduct, maxPrice);
    }

    //Tells the brokers to start the auction
    private void startBrokers(Product demandedProduct) {
        getBrokers().forEach(br->br.start(demandedProduct));
    }

    //Updates the brokers about the biggest Bid, and then the brokers update the observers
    private void updateBrokers(double biggestBid, Product demandedProduct) {
        getBrokers().forEach(br->br.notifyObservers(biggestBid, demandedProduct));
    }

    /*Update the brokers about the winning bid. Then, the brokers search for the observer that put that bid, then
    return a String with the winner's name and bid */
    private String updateWinner(double biggestBid, Product demandedProduct) {
        StringBuilder result = new StringBuilder();
        getBrokers().forEach(br -> result.append(br.notifyWinner(biggestBid, demandedProduct)));
        return result.toString();
    }

    //Tells the broker to reset all the observers' information
    private void reset() {
        getBrokers().forEach(Broker::reset);
    }

    public List<Product> getProducts() {
        lock.lock();
        List<Product> list = new ArrayList<>(new HashSet<>(products));
        lock.unlock();
        return list;
    }

    public List<Broker> getBrokers() {
        List<Broker> brokers = new ArrayList<>();
        employees.stream()
                .filter(emp->emp instanceof Broker)
                .forEach(emp->brokers.add((Broker) emp));

        return brokers;
    }

    public List<Auction> getAuctions() {
        return auctions;
    }

    public List<Product> getSoldProducts() {
        return soldProducts;
    }

    /**
     * Searches a product by ID and verifies if the maxPrice is
     * bigger than the product's minimum price
     * @param productId the product's ID
     * @param maxPrice the maximum price offered by the client
     * @return the Product found
     * @throws UnknownProductException if the product is not found
     * @throws InvalidProductRequest if the sum offered is too small
     */
    public Product findProduct(int productId, double maxPrice) throws UnknownProductException, InvalidProductRequest {
        Product demandedProduct = products.stream()
                .filter(prod -> prod.getId() == productId)
                .findFirst()
                .orElse(null);

        if(demandedProduct == null) {
            throw new UnknownProductException();
        }

        if(demandedProduct.getMinPrice() > maxPrice)
            throw new InvalidProductRequest();
        return demandedProduct;
    }

    /**
     * Gets the list of clients
     * @return the list of clients signed up in this house
     */
    public List<Client> getClients() {
        return clients;
    }
}
