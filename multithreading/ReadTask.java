package multithreading;
import organisers.AuctionHouse;

/**
 * Class implementing runnable which executes
 * the task of reading all the products
 * in the products array of the auction house
 */
public class ReadTask implements Runnable{

    /**
     * Reads all the products from the array
     */
    @Override
    public void run() {
        AuctionHouse.getInstance().readProducts();
    }
}
