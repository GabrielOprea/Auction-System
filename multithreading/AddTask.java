package multithreading;
import organisers.AuctionHouse;
import products.Product;

/**
 * Class implementing runnable which executes
 * the task of adding a product in the products array
 * of the auction house
 */
public class AddTask implements Runnable {

    private final Product product;

    /**
     * Constructor that sets the product to be added
     * @param product the product to be added in the auction house
     */
    public AddTask(Product product) {
        this.product = product;
    }

    /**
     * Adds the product
     */
    @Override
    public void run() {
        AuctionHouse.getInstance().add(product);
    }
}