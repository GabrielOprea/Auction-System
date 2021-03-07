package interfaces;
import products.Product;

/**
 * Subject interface, that can notify observers of the events
 * happened in the auction
 */
public interface Subject {

    /**
     * Adds an observer to the subscribers list
     * @param o the added observer
     */
    void add(Observer o);

    /**
     * Notifies the observers about the maximum bid
     * of the demanded product
     * @param bid the maximum bid in the current step of an auction
     * @param demandedProduct the product demanded by the observers
     */
    void notifyObservers(double bid, Product demandedProduct);

    /**
     * Notifies a specific observer about the maximum bid after a specific step
     * @param o the Observer to be notified
     * @param bid the maximum bid in the current step of an auction
     */
    void notifyObserver(Observer o, double bid);

    /**
     * Removes an Observer from the list
     * @param o the Observer to be removed
     */
    void removeObserver(Observer o);
}