package readers;

import clients.Client;
import exceptions.JSONDataException;
import exceptions.UnknownDataException;
import interfaces.AbstractFactory;
import interfaces.FactoryProvider;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import organisers.Administrator;
import organisers.Auction;
import organisers.AuctionHouse;
import organisers.Broker;
import products.Product;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Class for parsing and reading a JSON file
 */
public class JSONReader {

    /**
     * Utility class does not need to be instantiated
     */
    private JSONReader() {
    }

    /**
     * Reads the data from JSON file
     * @throws UnknownDataException if the products or clients are of unknown subtypes
     * @throws JSONDataException if the data is invalid
     * @throws ParseException if the JSON file cannot be parsed
     * @throws IOException if the JSON file does not exist
     */
    public static void readJSON() throws UnknownDataException, JSONDataException, ParseException, IOException {
        readJSON(Main.TEST_NUMBER);
    }

    /**
     * Reads the data from JSON file, with the specified testNumber
     * @param testNumber the test to be executed
     * @throws IOException if the JSON file does not exist
     * @throws ParseException if the JSON file cannot be parsed
     * @throws JSONDataException if the data is invalid
     * @throws UnknownDataException if the products or clients are of unknown subtypes
     */
    public static void readJSON(int testNumber) throws IOException, ParseException, JSONDataException, UnknownDataException {
        AuctionHouse crtHouse = AuctionHouse.getInstance();
        String jsonName = "tests/test" + testNumber + "/test" + testNumber + "_info.json";
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(jsonName)) {
            //Initialise the main JSON object and all the auxiliary lists of products, clients, brokers and auctions
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            JSONObject admin = (JSONObject) jsonObject.get("administrator");
            JSONArray productsArr = (JSONArray) jsonObject.get("products");
            JSONArray clientsArr = (JSONArray) jsonObject.get("clients");
            JSONArray brokersArr = (JSONArray) jsonObject.get("brokers");
            JSONArray auctionsArr = (JSONArray) jsonObject.get("auctions");

            //Get an iterator for each list, used for traversal
            Iterator<JSONObject> productsIterator = productsArr.iterator();
            Iterator<JSONObject> clientsIterator = clientsArr.iterator();
            Iterator<JSONObject> brokersIterator = brokersArr.iterator();
            Iterator<JSONObject> auctionsIterator = auctionsArr.iterator();

            //Obtain the factory necessary for creating product objects
            AbstractFactory<Product> productFactory = FactoryProvider.getFactory("product");

            int productIndex = 0;
            while (productsIterator.hasNext()) {
                //Iterate through products and extract all their attributes
                JSONObject crtProduct = productsIterator.next();
                String productType = (String) crtProduct.get("product_type");
                String name = (String) crtProduct.get("name");
                String minPrice = String.valueOf(((Number) crtProduct.get("min_price")).doubleValue());
                String year = String.valueOf(((Number) crtProduct.get("year")).intValue());

                /*additionalInfo is read afterwards and it differs, based on the type of product(for example,
                a Painting may have a painter and a color type */
                String additionalInfo1;
                String additionalInfo2;
                final String material = "material";
                switch (productType) {
                    //Read the additional information from JSON, based on the product's type
                    case "furniture":
                        additionalInfo1 = (String) crtProduct.get("type");
                        additionalInfo2 = (String) crtProduct.get(material);
                        break;
                    case "jewelry":
                        additionalInfo1 = (String) crtProduct.get(material);
                        additionalInfo2 = (String) crtProduct.get("precious_stone");
                        break;
                    case "painting":
                        additionalInfo1 = (String) crtProduct.get("painter_name");
                        additionalInfo2 = (String) crtProduct.get("color");
                        break;
                    case "clothing":
                        additionalInfo1 = (String) crtProduct.get("designer");
                        additionalInfo2 = (String) crtProduct.get(material);
                        break;
                    case "antique":
                        additionalInfo1 = String.valueOf(crtProduct.get("age"));
                        additionalInfo2 = (String) crtProduct.get("origin");
                        break;
                    default:
                        //If the product type is not found, then the JSON file cannot be parsed how it was intended
                        throw new JSONDataException();
                }

                /*Take all the parameters and build a list with them. Then the list is passed in the obtained
                Product factory for instantiating the current product object */
                String[] params = new String[]{productType, name, minPrice, year,
                        additionalInfo1, additionalInfo2};

                //Create and add the new product
                Product p = productFactory.create(Arrays.asList(params));
                p.setId(productIndex);
                productIndex++;
                Thread t = new Thread(Administrator.getInstance().addAuctionProduct(p));
                t.start();
            }

            while (clientsIterator.hasNext()) {
                //Iterate through clients, same as with products
                JSONObject crtClient = clientsIterator.next();
                String clientType = (String) crtClient.get("client_type");
                String name = (String) crtClient.get("name");
                String address = (String) crtClient.get("address");
                String additionalInfo1;
                String additionalInfo2 = "none";
                switch (clientType) {
                    case "legal":
                        additionalInfo1 = (String) crtClient.get("company");
                        additionalInfo2 = String.valueOf(((Number) crtClient.get("capital")).doubleValue());
                        break;
                    case "natural":
                        additionalInfo1 = (String) crtClient.get("birth_date");
                        break;
                    default:
                        throw new JSONDataException();
                }

                //Obtain the client factory
                AbstractFactory<Client> clientFactory = FactoryProvider.getFactory("client");

                //Build a list of parameters and use the list for a client creation
                String[] params = new String[]{clientType, name, address,
                        additionalInfo1, additionalInfo2};

                Client c = clientFactory.create(Arrays.asList(params));
                Administrator.getInstance().addAuctionClient(c);
            }

            //Read the information about brokers
            while (brokersIterator.hasNext()) {
                JSONObject crtEmployee = brokersIterator.next();
                String name = (String) crtEmployee.get("name");
                int yearsExp = ((Number) crtEmployee.get("years_of_experience")).intValue();
                double rating = ((Number) crtEmployee.get("rating")).intValue();

                crtHouse.add(new Broker(name, yearsExp, rating));
            }

            //Read the unique instance of Administrator
            String name = (String) admin.get("name");
            int yearsExp = ((Number) admin.get("years_of_experience")).intValue();
            double rating = ((Number) admin.get("rating")).intValue();

            //Obtain the new instance and set its field to those read
            Administrator adm = Administrator.getInstance();
            adm.setName(name);
            adm.setYearsOfExperience(yearsExp);
            adm.setRating(rating);
            Administrator.getInstance().addAuctionEmployee(adm);

            //Read the auctions from JSON, rather than generating them afterwards
            int auctionIndex = 0;
            while (auctionsIterator.hasNext()) {
                JSONObject crtAuction = auctionsIterator.next();
                int productId = ((Number) crtAuction.get("product_id")).intValue();
                int noMaxSteps = ((Number) crtAuction.get("no_max_steps")).intValue();
                int noParticipants = ((Number) crtAuction.get("no_participants")).intValue();

                Auction createdAuction = new Auction(productId, noMaxSteps, noParticipants);
                createdAuction.setId(auctionIndex);
                auctionIndex++;
                Administrator.getInstance().addAuction(createdAuction);
            }
        }
    }
}