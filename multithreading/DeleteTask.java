package multithreading;
import organisers.AuctionHouse;
import products.Product;

/**
 * Class implementing runnable which executes
 * the task of deleting a product from the products array
 * of the auction house
 */
public class DeleteTask implements Runnable {

    private final Product product;

    /**
     * Constuctor for setting the product to be removed
     * @param product the product to be removed from the list
     */
    public DeleteTask(Product product) {
        this.product = product;
    }

    /**
     * Removes the product
     */
    @Override
    public void run() {
        AuctionHouse.getInstance().deleteProduct(product);
    }
}
