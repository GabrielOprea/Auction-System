package products;
import java.util.Objects;

/**
 * Jewelry subclass of Product
 */
public class Jewelry extends Product {
    String material;
    boolean preciousStone;

    /**
     * Create a jewelry with the specified parameters
     * @param material the material for the jewelry
     * @param preciousStone the precious stone used
     */
    public Jewelry(String material, boolean preciousStone) {
        this.material = material;
        this.preciousStone = preciousStone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Jewelry jewelry = (Jewelry) o;
        return preciousStone == jewelry.preciousStone && Objects.equals(material, jewelry.material);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), material, preciousStone);
    }

    @Override
    public String toString() {
        return "products.Jewelry{" + id +
                " material='" + material + '\'' +
                ", preciousStone=" + preciousStone +
                '}';
    }
}
