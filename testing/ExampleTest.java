package testing;
import clients.Client;
import clients.LegalPerson;
import clients.NaturalPerson;
import exceptions.*;
import interfaces.AbstractFactory;
import interfaces.FactoryProvider;
import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import organisers.*;
import products.Clothing;
import products.Furniture;
import products.Product;
import readers.JSONReader;
import readers.XLSXReader;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * A JUnit5 testing class
 */
public class ExampleTest {

    /**
     * Tests the equals method from Product
     */
    @Test
    @DisplayName("Products identity")
    public void checkProductEquals() {
        Product initialProd = new Product(1, "testing product", 120.02, 2015);
        Product testFurniture = new Furniture("Amazon", "wool");
        Product testClothing = new Clothing("Amazon", "wool");
        initialProd.copyTo(testFurniture);
        initialProd.copyTo(testClothing);
        Assertions.assertNotEquals(testClothing, testFurniture);
    }

    /**
     * Tests if the clients' ID's are unique
     */
    @Test
    @DisplayName("Unique IDs")
    public void checkUniqueIDs() throws UnknownDataException, JSONDataException, ParseException, IOException {

        JSONReader.readJSON();
        XLSXReader.readXLSX();

        Set<Integer> clientIDs = new HashSet<>();

        AuctionHouse crtHouse = AuctionHouse.getInstance();

        for(Client c : crtHouse.getClients()) {
            clientIDs.add(c.getId());
        }

        Assertions.assertEquals(clientIDs.size(), crtHouse.getClients().size());
    }

    /**
     * Tests whether all transactions are valid(no product was sold for less than it was worth)
     */
    @Test
    @DisplayName("Valid transactions")
    public void checkValidSoldProducts() throws IOException, UnknownDataException, ParseException, JSONDataException {

        JSONReader.readJSON();
        XLSXReader.readXLSX();

        List<Product> products = AuctionHouse.getInstance().getProducts();
        boolean valid = true;
        for(Product p : products) {
            if(p.getSellPrice() != 0 && p.getSellPrice() < p.getMinPrice()) {
                valid = false;
                break;
            }
        }
        Assertions.assertTrue(valid);
    }

    /**
     * Verifies the commission for a natural person
     * @param c the client to be verified
     * @return whether the commission is valid
     */
    public boolean verifyNatural(Client c) {
        boolean valid = true;
        if (c.getNoParticipation() < 5) {
            if (c.getCommission() != 0 && c.getCommission() != 20) {
                valid = false;
            }
        } else {
            if (c.getCommission() != 0 && c.getCommission() != 15) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Verifies the commission for a legal person
     * @param c the client to be verified
     * @return whether the commission is valid
     */
    public boolean verifyLegal(Client c) {
        boolean valid = true;
        if(c.getNoParticipation() < 25) {
            if(c.getCommission() != 0 && c.getCommission() != 25) {
                valid = false;
            }
        }
        else {
            if(c.getCommission() != 0 && c.getCommission() != 10) {
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Test for verifying the validity of the commissions
     */
    @Test
    @DisplayName("Valid commission")
    public void checkValidCommission() throws IOException, UnknownDataException, ParseException, JSONDataException {

        JSONReader.readJSON();
        XLSXReader.readXLSX();

        List<Client> clients = AuctionHouse.getInstance().getClients();

        boolean valid = true;
        //Check for each client
        for(Client c: clients) {
            if(c instanceof NaturalPerson) {
                valid = verifyNatural(c);
            }
            else if(c instanceof LegalPerson) {
                valid = verifyLegal(c);
            }
        }
        Assertions.assertTrue(valid);
        valid = true;

        //Check also if the broker cash sum has increased when he won an auction
        for(Broker broker : AuctionHouse.getInstance().getBrokers()) {
            if(broker.getCash() == 0 && broker.getNoWonAuctions() != 0) {
                valid = false;
                break;
            }
        }
        Assertions.assertTrue(valid);
    }

    /**
     * Checks if a client can request an invalid product without exception
     */
    @Test
    @DisplayName("Invalid products")
    public void checkInvalidProduct() throws IOException, UnknownDataException, ParseException, JSONDataException {
        JSONReader.readJSON();
        XLSXReader.readXLSX();
        AuctionHouse crtHouse = AuctionHouse.getInstance();
        Client testClient = crtHouse.getClients().get(0);
        Assertions.assertThrows(UnknownProductException.class,
                ()->        testClient.signUp(1000, 1000));
    }

    /**
     * Checks the program behaviour for invalid clients
     */
    @Test
    @DisplayName("Invalid clients")
    public void checkInvalidClient() throws UnknownDataException, JSONDataException, ParseException, IOException {
        JSONReader.readJSON();
        XLSXReader.readXLSX();
        AbstractFactory<Client> clientFactory = FactoryProvider.getFactory("client");
        String[] params = new String[]{"child", "Andrew", "Paris", "Eiffel Tower"};
        Assertions.assertThrows(UnknownClientException.class,
                ()->        clientFactory.create(Arrays.asList(params)));
    }

    /**
     * Checks what happens if the maximum sum bid by the client is less than the price of the product
     */
    @Test
    @DisplayName("Max bid sum")
    public void checkMaxBid() throws UnknownDataException, JSONDataException, ParseException, IOException {
        JSONReader.readJSON();
        XLSXReader.readXLSX();
        AuctionHouse crtHouse = AuctionHouse.getInstance();
        Assertions.assertThrows(InvalidProductRequest.class,
                ()->        crtHouse.getClients().get(0).signUp(crtHouse.getProducts().get(0).getId(), -1));
    }

    /**
     * Checks if the program does not throw an exception and runs successfully, for all tests
     */
    @Test
    @DisplayName("Passes tests")
    public void checkPassingTests() {

        int[] tests = new int[] {1,2,3,4,5,6,7,8,9,10};
        for(int testNumber : tests) {
            Assertions.assertDoesNotThrow(() -> JSONReader.readJSON(testNumber));
            Assertions.assertDoesNotThrow(() -> XLSXReader.readXLSX(testNumber));
        }
    }

    /**
     * Checks if the output is deterministic, and brokers randomisation does not affect it
     */
    @Test
    @DisplayName("Deterministic result")
    public void checkDeterministicResult() throws UnknownDataException, JSONDataException, ParseException, IOException {

        Double firstRun;
        Double secondRun;

        JSONReader.readJSON();
        XLSXReader.readXLSX();

        firstRun = AuctionHouse.getInstance().getSoldProducts().get(0).getSellPrice();

        Administrator.resetAll();

        JSONReader.readJSON();
        XLSXReader.readXLSX();

        secondRun = AuctionHouse.getInstance().getSoldProducts().get(0).getSellPrice();

        Assertions.assertEquals(firstRun, secondRun);
    }

    private void checkMultithreadingAux() throws InterruptedException {

        //Start an executor
        ExecutorService executor = Executors.newCachedThreadPool();
        for(Client client: AuctionHouse.getInstance().getClients()) {
            executor.execute(client.read());
        }

        //Add two delete tasks, and two add tasks
        executor.execute(AuctionHouse.getInstance().getBrokers().get(0)
                .deleteAuctionProduct(new Product(1000, "testProduct1", 120, 2000)));
        executor.execute(Administrator.getInstance()
                .addAuctionProduct(new Product(1000, "testProduct1", 120, 2000)));
        executor.execute(AuctionHouse.getInstance().getBrokers().get(0)
                .deleteAuctionProduct(new Product(1001, "testProduct2", 120, 2000)));
        executor.execute(Administrator.getInstance()
                .addAuctionProduct(new Product(1001, "testProduct2", 120, 2000)));

        //close the executor and wait for the tasks termination
        executor.shutdown();
        executor.awaitTermination(100L, TimeUnit.MILLISECONDS);

    }

    /**
     * Checks whether the multithreading produces any errors/exceptions
     */
    @Test
    @DisplayName("Multithreading")
    public void checkMultithreading() throws UnknownDataException, JSONDataException, ParseException, IOException {

        JSONReader.readJSON();
        XLSXReader.readXLSX();

        Assertions.assertDoesNotThrow(this::checkMultithreadingAux);
    }

}
