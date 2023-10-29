import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        try {
            DatabaseMani dm = new DataFactory().createDataMani("");
            System.out.println("Connected");
            Scanner in = new Scanner(System.in);
            while(in.hasNext()) {
                String text = in.nextLine();
                long start = System.nanoTime(); // time spent
                if (text.equalsIgnoreCase("readusers")) {
                    dm.readUser();
                } else if (text.equalsIgnoreCase("readcontents")) {
                    dm.readContent();
                } else if (text.equalsIgnoreCase("readvideos")) {
                    dm.readVideo();
                } else if (text.equalsIgnoreCase("exit")) {
                    in.close();
                    break;
                } else {
                    System.out.println("Command not found");
                }
                long end = System.nanoTime();
                System.out.println("Time: " + (end - start) / 1000000 + "ms");
            }
        } catch (IllegalArgumentException e) {
            System.err.println(e.getMessage());
        }

    }
}
