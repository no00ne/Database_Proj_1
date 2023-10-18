public class Main {
    public static void main(String[] args) {
        try {
            DatabaseMani dm = new DataFactory().createDataMani("");
            dm.readUser();
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

    }
}