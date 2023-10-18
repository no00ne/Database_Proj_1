import java.lang.reflect.InvocationTargetException;
public class DataFactory {
    public DatabaseMani createDataMani(String arg) {
        String name;
        name = "DatabaseMani";

        try {
            return (DatabaseMani) Class.forName(name).getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
