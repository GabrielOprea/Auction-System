package readers;

import exceptions.DuplicateRequestException;
import exceptions.InvalidProductRequest;
import org.apache.poi.ss.usermodel.*;
import organisers.AuctionHouse;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

/**
 * Class for reading the request from an XLSX Excel file
 */
public class XLSXReader {

    /**
     * Private constructor for hiding the default one
     */
    private XLSXReader() {
    }

    /**
     * Reads an Excel file
     */
    public static void readXLSX()  {
        readXLSX(Main.TEST_NUMBER);
    }

    /**
     * Reads an Excel file for a specified test
     * @param testNumber the current test
     */
    public static void readXLSX(int testNumber) {
        AuctionHouse crtHouse = AuctionHouse.getInstance();

        //The path to the XLSX file
        String xlsxName = "tests/test" + testNumber + "/test" + testNumber + ".xlsx";
        try (Workbook workbook = WorkbookFactory.create(new File(xlsxName))) {
            //Get the unique sheet
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter dataFormatter = new DataFormatter();

            //Obtain an iterator for the rows
            Iterator<Row> rowIterator = sheet.rowIterator();
            rowIterator.next();

            rowIterator.forEachRemaining( crtRow -> {
                Cell clientCell = crtRow.getCell(0);
                Cell productCell = crtRow.getCell(1);
                Cell sumCell = crtRow.getCell(2);

                //And parse the information they contain(two integers and a String with a '$' character at the end)
                int clientId = Integer.parseInt(dataFormatter.formatCellValue(clientCell));
                int productId = Integer.parseInt(dataFormatter.formatCellValue(productCell));
                String sumWithSign = dataFormatter.formatCellValue(sumCell);

                //Extract the '$' character
                int maxSum = Integer.parseInt(sumWithSign.substring(0, sumWithSign.length() - 1));

                //Try to sign up the client for his required product
                try {
                    crtHouse.getClients().get(clientId).signUp(productId, maxSum);
                } catch (Exception e) {
                    e.getMessage();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
