package clients;
import java.time.LocalDate;

/**
 * A subclass of the client
 */
public class NaturalPerson extends Client {
    LocalDate birthDate;

    /**
     * Instantiated a natural person with a specific birth date
     * @param birthDate a LocalDate object representing his/her birthdate
     */
    public NaturalPerson(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    @Override
    public String toString() {
        return "NaturalPerson{" +
                "birthDate=" + birthDate +
                '}';
    }
}
