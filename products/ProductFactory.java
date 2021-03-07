package products;
import exceptions.UnknownProductException;
import helper.ColorType;
import interfaces.AbstractFactory;
import java.util.List;

/**
 * A Factory used for instantiating product objects
 */
public class ProductFactory implements AbstractFactory<Product> {

    /**
     * Method that creates a new product
     * @param params String list with the parameters of the product
     * @return the newly created product
     * @throws UnknownProductException if the subtype is invalid
     */
    @Override
    public Product create(List<String> params) throws UnknownProductException {

        //Use the builder
        Product crtProduct = new ProductBuilder()
                .withName(params.get(1))
                .withMinPrice(Double.parseDouble(params.get(2)))
                .withYear(Integer.parseInt(params.get(3)))
                .build();

        //Then based on the subtype, read the next attributes
        switch(params.get(0)) {
            /* Use copyTo method to copy the information from the previously created product to the one corresponding
            to a specific subtype */
            case "painting":
                Painting crtPainting = new Painting(params.get(4), ColorType.valueOf(params.get(5).toUpperCase()));
                crtProduct.copyTo(crtPainting);
                return crtPainting;
            case "jewelry":
                Jewelry crtJewelry = new Jewelry(params.get(4), Boolean.parseBoolean(params.get(5)));
                crtProduct.copyTo(crtJewelry);
                return crtJewelry;
            case "furniture":
                Furniture crtFurniture = new Furniture(params.get(4), params.get(5));
                crtProduct.copyTo(crtFurniture);
                return crtFurniture;
            case "clothing":
                Clothing crtClothing = new Clothing(params.get(4), params.get(5));
                crtProduct.copyTo(crtClothing);
                return crtClothing;
            case "antique":
                Antique crtAnt = new Antique(Integer.parseInt(params.get(4)), params.get(5));
                crtProduct.copyTo(crtAnt);
                return crtAnt;
            default:
                throw new UnknownProductException();
        }
    }
}
