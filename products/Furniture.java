package products;
import java.util.Objects;

/**
 * Furniture is a subclass of Product
 */
public class Furniture extends Product {
    String type;
    String material;

    /**
     * Create a new Furniture object
     * @param type the type of furniture
     * @param material the material for this furniture
     */
    public Furniture(String type, String material) {
        this.type = type;
        this.material = material;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Furniture furniture = (Furniture) o;
        return Objects.equals(type, furniture.type) && Objects.equals(material, furniture.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), type, material);
    }

    @Override
    public String toString() {
        return "products.Furniture{" + id +
                " type='" + type + '\'' +
                ", material='" + material + '\'' +
                '}';
    }
}

