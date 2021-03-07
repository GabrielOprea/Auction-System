package organisers;
import java.util.Objects;

/**
 * The class for storing all the relevant information about an auction, like
 * how many participants are needed for the auction to start, or the maximum steps
 * of bidding that are executed
 */
public class Auction {
    int id;
    int noParticipants;
    int idProduct;
    int noMaxSteps;
    int noSignedUpParticipants;

    /**
     * Constructor that initialises the number of signed up
     * participants to 0
     */
    public Auction() {
        noSignedUpParticipants = 0;
    }

    /**
     * Constructor that initialises the product id, the number
     * of maximum steps and the number of participants
     * @param idProduct the id of the product
     * @param noMaxSteps the number of maximum bidding steps
     * @param noParticipants the number of required participants for the auction to start
     */
    public Auction(int idProduct, int noMaxSteps, int noParticipants) {
        noSignedUpParticipants = 0;
        this.noParticipants = noParticipants;
        this.idProduct = idProduct;
        this.noMaxSteps = noMaxSteps;
    }

    /**
     * Checks if the auction can start, based on the number of participants
     * @return a boolean value describing whether the auction can start
     */
    public boolean canStart() {
        return noParticipants == noSignedUpParticipants;
    }

    /**
     * Increments the number of participants at this auction
     */
    public void incrementParticipants() {
        noSignedUpParticipants++;
    }

    /**
     * Sets the auction id
     * @param id the identifier to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the auction id
     * @return the identifier for this auction
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the ID of the product this auction was created for
     * @return the ID of the product for the auction
     */
    public int getIdProduct() {
        return idProduct;
    }

    /**
     * Gets the maximum bidding steps
     * @return the maximum number of steps for this auction
     */
    public int getNoMaxSteps() {
        return noMaxSteps;
    }

    /**
     * Compares two auctions based on the id of the product they
     * were created for
     * @param o an object to be compared
     * @return whether the auctions are equal or not
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return idProduct == auction.idProduct;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idProduct);
    }

    /**
     * Converts from Auction to String
     * @return a string representation of this instance
     */
    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", idProduct=" + idProduct +
                '}';
    }
}