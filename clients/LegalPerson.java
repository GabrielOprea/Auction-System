package clients;
import helper.CompanyType;

/**
 * A subclass of Client
 */
public class LegalPerson extends Client {
    CompanyType company;
    double socialCapital;

    /**
     * Constructs a legal person, with a specific company and social capital
     * @param company enum for the company type(SRL or SA)
     * @param socialCapital the social capital of the company
     */
    public LegalPerson(CompanyType company, double socialCapital) {
        this.company = company;
        this.socialCapital = socialCapital;
    }

    /**
     * Gets the social capital of the company
     * @return the social capital of the company that employed the client
     */
    public double getSocialCapital() {
        return socialCapital;
    }

    /**
     * Converts the legal client to string
     * @return the string representation of the client
     */
    @Override
    public String toString() {
        return "LegalPerson{" +
                "company=" + company +
                ", socialCapital=" + socialCapital +
                '}';
    }
}
