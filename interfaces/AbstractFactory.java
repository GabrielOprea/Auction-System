package interfaces;
import exceptions.UnknownDataException;
import java.util.List;

/**
 * Interface used to instantiate various objects
 * @param <T> Generic type for object that is required to be created
 */
public interface AbstractFactory<T> {
    T create(List<String> params) throws UnknownDataException;
}