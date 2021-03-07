package clients;
import exceptions.UnknownClientException;
import helper.CompanyType;
import interfaces.AbstractFactory;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Factory used for created specific subclasses of client
 */
public class ClientFactory implements AbstractFactory<Client> {

    /**
     * Returns a specific client, based of a list of parameters
     * @param params a List of parameters for creating the client object
     * @return a subclass of client
     * @throws UnknownClientException if the client type is neither legal nor natural
     */
    @Override
    public Client create(List<String> params) throws UnknownClientException {
        //Use builder for building a client object
        Client crtClient = new ClientBuilder()
                .withName(params.get(1))
                .withAddress(params.get(2))
                .build();

        //Then create a subclass of the client's
        switch (params.get(0)) {
            case "natural" -> {
                //Convert the birthday string to a date time formatter
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");
                NaturalPerson crtNaturalPerson = new NaturalPerson(LocalDate.parse(params.get(3),
                        formatter));
                crtClient.copyTo(crtNaturalPerson);
                return crtNaturalPerson;
            }
            case "legal" -> {
                LegalPerson crtLegalPerson = new LegalPerson(CompanyType.valueOf(params.get(3)),
                        Double.parseDouble(params.get(4)));
                crtClient.copyTo(crtLegalPerson);
                return crtLegalPerson;
            }
            //Throw an exception if the client's subtype is unknown
            default -> throw new UnknownClientException();
        }
    }
}
