package interfaces;
import org.apache.commons.math3.util.Pair;
import organisers.Information;

/**
 * Observer interface, that are updated every time the subjects
 * executes an important action, designed based on the Observer
 * design pattern
 */
public interface Observer {

    /**
     * Updates the observer whether he is the winner
     * @param winner tells if the observer won the auction
     */
    void update(boolean winner);

    /**
     * Updates the observer with a specific information field
     * @param info the information field set by the subject
     */
    void update(Information info);

    /**
     * Bidding, used for actively participating in an auction
     * @return a pair of the current bid and the current number
     * of won auctions
     */
    Pair<Double, Integer> bid();

    /**
     * A getter for an observer's information, used by the subject
     * to better perform in the auction process
     * @return the information of this observer
     */
    Information getInfo();
}
