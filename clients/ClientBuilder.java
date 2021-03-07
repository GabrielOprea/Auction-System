package clients;

/**
 * Based on the Builder design pattern, used for easily instantiating client objects
 */
public class ClientBuilder {

    private final Client client = new Client();

    /**
     * Sets the client's ID
     * @param id the id to be set
     * @return the builder object, not yet built
     */
    @SuppressWarnings("unused")
    public ClientBuilder withId(int id) {
        client.setId(id);
        return this;
    }

    /**
     * Sets the client's name
     * @param name the name to be set
     * @return the builder object, not yet built
     */
    public ClientBuilder withName(String name) {
        client.setName(name);
        return this;
    }

    /**
     * Sets the client's address
     * @param address the address to be set
     * @return the builder object, not yet built
     */
    public ClientBuilder withAddress(String address) {
        client.setAddress(address);
        return this;
    }

    /**
     * Sets the client's participation
     * @param noParticipation the participation to be set
     * @return the builder object, not yet built
     */
    @SuppressWarnings("unused")
    public ClientBuilder withNoParticipation(int noParticipation) {
        client.setNoParticipation(noParticipation);
        return this;
    }

    /**
     * Sets the client's number of won auctions
     * @param noWonAuctions the number of won auctions to be set
     * @return the builder object, not yet built
     */
    @SuppressWarnings("unused")
    public ClientBuilder withNoWonAuctions(int noWonAuctions) {
        client.setNoWonAuctions(noWonAuctions);
        return this;
    }

    /**
     * Builds the client object based on the builder's fields
     * @return the newly built client
     */
    public Client build() {
        return client;
    }
}
