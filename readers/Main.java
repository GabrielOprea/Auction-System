package readers;
import java.io.IOException;
import exceptions.JSONDataException;
import exceptions.UnknownDataException;
import org.json.simple.parser.ParseException;

/**
 * The main class of the program, that calls the two additional
 * readers, one for JSON and one for XLSX
 */
public class Main {

    //A constant test number
    public static final int TEST_NUMBER = 10;

    public static void main(String[] args) throws IOException, ParseException, UnknownDataException, JSONDataException {

        //Read the initial Auction House configuration from JSON
        JSONReader.readJSON();

        //Read the client's requests from Excel
        XLSXReader.readXLSX();

    }
}