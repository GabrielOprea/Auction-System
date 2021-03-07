package products;
import java.util.Objects;

/**
 * The Product class for describing what the clients can buy
 * from the auction house
 */
public class Product {
    int id;
    String name;
    double sellPrice;
    double minPrice;
    int year;

    /**
     * Default constructor
     */
    public Product() {
        sellPrice = 0;
    }

    /**
     * Constructs a product with the specified id
     * @param id the id of the new product
     */
    public Product(int id) {
        this.id = id;
    }

    /**
     * Generic method for copying fields from a product object
     * to one of its subclasses
     * @param obj the subclass object
     * @param <T> generic type that can be any product
     */
    public<T extends Product> void copyTo(T obj) {
        obj.setId(id);
        obj.setName(name);
        obj.setSellPrice(sellPrice);
        obj.setMinPrice(minPrice);
        obj.setYear(year);
    }

    /**
     * Complete constructor for product
     * @param id the attributed id
     * @param name product's name
     * @param sellPrice product's sell price
     * @param minPrice product's minimum bid price
     * @param year the year this product has been added
     */
    public Product(int id, String name, double sellPrice, double minPrice, int year) {
        this.id = id;
        this.name = name;
        this.sellPrice = sellPrice;
        this.minPrice = minPrice;
        this.year = year;
    }

    /**
     * Constructor for product
     * @param id the attributed id
     * @param name product's name
     * @param minPrice product's minimum bid price
     * @param year the year this product has been added
     */
    public Product(int id, String name, double minPrice, int year) {
        this(id, name, 0, minPrice, year);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSellPrice(double sellPrice) {
        this.sellPrice = sellPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getSellPrice() {
        return sellPrice;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public int getYear() {
        return year;
    }

    /**
     * Static method that returns the subclass name of this product object
     * @param product the product we want the subclass name
     * @return the subclass name
     */
    public static String getType(Product product) {
        return product.getClass().toString().replace(".", " ").split(" ")[2];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return year == product.year && Objects.equals(name, product.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {

        return "products.Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sellPrice=" + sellPrice +
                ", minPrice=" + minPrice +
                ", year=" + year + '}';
    }
}