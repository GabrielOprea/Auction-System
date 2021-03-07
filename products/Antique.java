package products;
import java.util.Objects;

/**
 * An Antique type of product
 */
public class Antique extends Product {

    int age;
    String origin;

    /**
     * Creates a new Antique
     * @param age the age of the antique
     * @param origin the country of origin
     */
    public Antique(int age, String origin) {
        this.age = age;
        this.origin = origin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Antique antique = (Antique) o;
        return age == antique.age && Objects.equals(origin, antique.origin);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), age, origin);
    }

    @Override
    public String toString() {
        return "Antique{" + id +
                " age=" + age +
                ", origin='" + origin + '\'' + '}';
    }
}