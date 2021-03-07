package clients;
import exceptions.DuplicateRequestException;
import exceptions.InvalidProductRequest;
import exceptions.UnknownProductException;
import interfaces.Observer;
import multithreading.ReadTask;
import org.apache.commons.math3.util.Pair;
import org.apache.commons.math3.util.Precision;
import organisers.AuctionHouse;
import organisers.Information;
import java.time.LocalDate;

/**
 * Clients implement the Observer interface and are the ones that take
 * part in an Auction
 */
public class Client implements Observer {
    private int id;
    private String name;
    private String address;
    private int noParticipation;
    private int noWonAuctions;
    private Information info;
    private double commission;

    /**
     * Default constructor that initialises the information field
     */
    public Client() {
        info = new Information();
    }

    /**
     * Updates the client's information field. The information field
     * is the way a broker communicates with his client. The client
     * tells the broker his name and his current bid, and the broker
     * informs him about the maximum bid or his commission.
     * @param info the information object attributed to the client
     */
    @Override
    public void update(Information info) {
        this.info = info;
    }

    /**
     *
     * @return the current information attached to this client
     */
    @Override
    public Information getInfo() {
        return info;
    }

    /**
     * Generic method for copying fields from a client object
     * to one of its subclasses
     * @param obj the subclass object
     * @param <T> generic type that can be NaturalPerson or LegalPerson
     */
    public<T extends Client> void copyTo(T obj) {
        obj.setId(id);
        obj.setName(name);
        obj.setAddress(address);
        obj.setNoParticipation(noParticipation);
        obj.setNoWonAuctions(noWonAuctions);
    }

    /**
     * A method called by the broker, in which he asks the client to give his
     * current bid. This bid is calculated based on the information communicated by
     * the broker and is guaranteed to not exceed the maximum sum the client can
     * afford
     * @return a pair consisting of the current bid made by the client and
     * an integer value for the number of won auctions(used to differentiate
     * between clients who bid the same sum
     *
     */
    @Override
    public Pair<Double, Integer> bid() {
        double crtMaxBid = info.getMaxAuctionBid();
        double crtBid = info.getCrtBid();
        double maxAffordableSum = info.getMaxAffordableBid();

        double newBid = crtMaxBid + crtBid / 10 + maxAffordableSum / 10;

        //Recalculate the bid, based on the subclass of this client
        if(this instanceof NaturalPerson) {
            //More experienced clients will bid more
            newBid *= (double)LocalDate.now().getYear() / ((NaturalPerson)this).getBirthDate().getYear();
        } else if (this instanceof LegalPerson) {
            //Clients hired at better companies will bid more
            newBid *= (1 + ((LegalPerson)this).getSocialCapital() / 10000);
        }
        if(newBid > maxAffordableSum)
            newBid = maxAffordableSum;

        return new Pair<>(Precision.round(newBid, 2), noWonAuctions);
    }

    /**
     * A method called by the broker in which the client is updated of the
     * current status of the auction
     * @param winner decides if the client is the winner of the auction or not
     */
    @Override
    public void update(boolean winner) {
        noParticipation++;
        if(winner) noWonAuctions++;
    }

    /**
     * The method used by the client to sign up for a product.
     * @param id the id of the product demanded by the client
     * @param maxPrice the maximum price the client is willing to offer
     * @throws UnknownProductException if the product that the client wants is not existent in the auction houses
     * @throws DuplicateRequestException if this client already demanded the same product
     * @throws InvalidProductRequest if the client requests a product that costs more than he can offer
     */
    public void signUp(int id, double maxPrice) throws UnknownProductException, DuplicateRequestException, InvalidProductRequest {
        Thread t = new Thread(read());
        t.start();

        AuctionHouse.getInstance().request(this, id, maxPrice);
    }

    /** Sets the client's ID
     * @param id the id to be set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets the client's Name
     * @param name the name to be set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the client's address
     * @param address the address to be set
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Sets the client's number of appearances
     * @param noParticipation the number of appearances
     */
    public void setNoParticipation(int noParticipation) {
        this.noParticipation = noParticipation;
    }

    /**
     * Sets the client's number of won auctions
     * @param noWonAuctions the number of won auctions
     */
    public void setNoWonAuctions(int noWonAuctions) {
        this.noWonAuctions = noWonAuctions;
    }

    /**
     * Gets the client's ID
     * @return the client's ID
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the client's name
     * @return the name of the client
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the client's address
     * @return the address of the client
     */
    public String getAddress() {
        return address;
    }

    /**
     * Gets the client's number of appearances
     * @return the number of appearances in an auction
     */
    public int getNoParticipation() {
        return noParticipation;
    }

    /**
     * Gets the client's commission estabilished by the broker
     * @return the client's commission
     */
    public double getCommission() {
        return commission;
    }

    /**
     * Method that instantiates a runnable object for
     * reading the list of products
     * @return a runnable object used for reading
     */
    public Runnable read() {
        return new ReadTask();
    }

    /**
     * Converts the client to a string
     * @return string concatenation of the client's id and name
     */
    @Override
    public String toString() {
        return "id=" + id +
                ", name='" + name + '\'';
    }
}