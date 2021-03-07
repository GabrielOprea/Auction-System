package products;

/**
 * Class used for building Product objects
 */
public class ProductBuilder {

    private final Product product = new Product();

    /**
     * Sets the ID for the builder
     * @param id the id to be set
     * @return the same builder object
     */
    public ProductBuilder withId(int id) {
        product.setId(id);
        return this;
    }

    /**
     * Sets the product's name for the builder
     * @param name the name of the product
     * @return the same builder object
     */
    public ProductBuilder withName(String name) {
        product.setName(name);
        return this;
    }

    /**
     * Sets the selling price for the builder
     * @param sellPrice the id to be set
     * @return the same builder object
     */
    public ProductBuilder withSellPrice(double sellPrice) {
        product.setSellPrice(sellPrice);
        return this;
    }

    /**
     * Sets the minimum price for the builder
     * @param minPrice the minimum price to be set
     * @return the same builder object
     */
    public ProductBuilder withMinPrice(double minPrice) {
        product.setMinPrice(minPrice);
        return this;
    }

    /**
     * Sets the year attribute for the builder
     * @param year the year the product has been added
     * @return the same builder object
     */
    public ProductBuilder withYear(int year) {
        product.setYear(year);
        return this;
    }

    /**
     * Builds the product
     * @return the built product
     */
    public Product build() {
        return product;
    }
}
