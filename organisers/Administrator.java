package organisers;
import clients.Client;
import multithreading.AddTask;
import products.Product;

/**
 * The Administrator is the one that has the ability to
 * create threads for adding products, and to add any
 * type of object to the auction house. Implemented based
 * on the Singleton design pattern
 */
public class Administrator extends Employee {
    private static Administrator uniqueINSTANCE = null;

    /**
     * Gets the unique instance of the administrator
     * @return an unique instance
     */
    public static Administrator getInstance() {
        if(uniqueINSTANCE == null) {
            uniqueINSTANCE = new Administrator();
        }
        return uniqueINSTANCE;
    }

    /**
     * Private constructor for ensuring only one instance
     * is created
     */
    private Administrator() {
    }

    /**
     * Resets all the information in the auction house
     */
    public static void resetAll() {
        AuctionHouse.getInstance().resetAll();
    }

    /**
     * Returns a runnable object for adding a product
     * @param p the product to be added in the auction house
     * @return a readTask object
     */
    public Runnable addAuctionProduct(Product p) {
        return new AddTask(p);
    }

    /**
     * Adds a client to the auction house
     * @param c the client to be added
     */
    public void addAuctionClient(Client c) {
        AuctionHouse.getInstance().add(c);
    }

    /**
     * Adds an auction to the auction house
     * @param auction the auction to be added
     */
    public void addAuction(Auction auction) {
        AuctionHouse.getInstance().add(auction);
    }

    /**
     * Adds an employee, either a broker or the administrator
     * @param emp the employee to be added
     */
    public void addAuctionEmployee(Employee emp) {
        AuctionHouse.getInstance().add(emp);
    }
}