package organisers;
import products.Product;

/**
 * The class used for mediating the communication between the brokers and
 * the clients
 */
public class Information {

    private Product demandedProduct;

    //the maximum bid the client can afford
    private double maxAffordableBid;

    //the current bid offered by the client
    private double crtBid;

    //the biggest bid offered by any client
    private double maxAuctionBid;

    //notifies is this client is the winner
    private boolean winner;

    //the percentage value for the commission
    private double commission;

    private int noWonAuctions;
    private String name;

    /**
     * Default constructor for setting the winner attribute to false
     * and demanded product to null(not recommended)
     */
    public Information() {
        winner = false;
        demandedProduct = null;
    }

    /**
     * Decides whether the client is the winner of the auction
     * @return client's status
     */
    public boolean isWinner() {
        return winner;
    }

    /**
     * Sets a commission for the client
     * @param commission the value of the commission
     */
    public void setCommission(double commission) {
        this.commission = commission;
    }

    /**
     * Sets the number of won auctions
     * @param noWonAuctions the number of won actions
     */
    public void setNoWonAuctions(int noWonAuctions) {
        this.noWonAuctions = noWonAuctions;
    }

    /**
     * Sets the winner attribute
     * @param winner decides whether the client with this information won
     */
    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public void setDemandedProduct(Product demandedProduct) {
        this.demandedProduct = demandedProduct;
    }

    public void setMaxAffordableBid(double maxAffordableBid) {
        this.maxAffordableBid = maxAffordableBid;
    }

    public void setCrtBid(double crtBid) {
        this.crtBid = crtBid;
    }

    public void setMaxAuctionBid(double maxAuctionBid) {
        this.maxAuctionBid = maxAuctionBid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Product getDemandedProduct() {
        return demandedProduct;
    }

    public double getMaxAffordableBid() {
        return maxAffordableBid;
    }

    public double getCrtBid() {
        return crtBid;
    }

    public double getMaxAuctionBid() {
        return maxAuctionBid;
    }

    public int getNoWonAuctions() {
        return noWonAuctions;
    }

    public double getCommission() {
        return commission;
    }

    public String getName() {
        return name;
    }
}
