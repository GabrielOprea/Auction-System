package products;
import helper.ColorType;
import java.util.Objects;

/**
 * Painting subclass of Product
 */
public class Painting extends Product {
    String painterName;
    ColorType colors;

    /**
     * Create a new Painting object
     * @param painterName the name of the painter
     * @param colors the type of color used for the painting
     */
    public Painting(String painterName, ColorType colors) {
        this.painterName = painterName;
        this.colors = colors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Painting painting = (Painting) o;
        return Objects.equals(painterName, painting.painterName) && colors == painting.colors;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), painterName, colors);
    }

    @Override
    public String toString() {
        return "products.Painting{" + id +
                " painterName='" + painterName + '\'' +
                ", colors=" + colors +
                '}';
    }
}
