package products;
import java.util.Objects;

/**
 * Clothing type of product, this class extends Product
 */
public class Clothing extends Product {

    String designer;
    String material;

    /**
     * Creates a new clothing object
     * @param designer the designer's name
     * @param material the material for this price of clothing
     */
    public Clothing(String designer, String material) {
        this.designer = designer;
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Clothing clothing = (Clothing) o;
        return Objects.equals(designer, clothing.designer) && Objects.equals(material, clothing.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), designer, material);
    }

    @Override
    public String toString() {
        return "products.Clothing{" + id +
                " designer='" + designer + '\'' +
                ", material='" + material + '\'' +
                '}';
    }
}