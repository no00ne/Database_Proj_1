import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;

public class DatabaseMani {
    private String database = "project1";
    private String username = "Steven";
    private String password = "7818";
    private Connection c = null;
    public Connection Connect() {

        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + database,
                    username, password);
            System.out.println("Connect success");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
        }
        return c;
    }

    private void closeConnection() {
        if (c != null) {
            try {
                c.close();
                c = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readUser() {
        Connect();
        int i = 0;
        String csvFile = "src/users.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            String sql = "INSERT INTO users VALUES" +
                    "(?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = c.prepareStatement(sql);

            br.readLine();
            while ((line = br.readLine()) != null) {
                line = line.replace("\"", "").replace("[","-").replace("]","-");
                String[] split_id = line.split("-"); // delete []
                String[] data = split_id[0].split(",", -1);

                stmt.setObject(1, Integer.parseInt(data[0]));
                stmt.setString(2, data[1]);
                stmt.setString(3, data[2]);
                stmt.setString(4, data[3]); // TODO: some ways to change this into date
                stmt.setObject(5, Integer.parseInt(data[4]));
                stmt.setString(6, data[5]);
                stmt.setArray(7, c.createArrayOf("integer", split_id[1].replace("\'", "").split(",")));
                // TODO: some ways to change the code above into correct format
                stmt.setString(8, split_id[2].replace(",", ""));
                stmt.executeUpdate();
                System.out.println("succes " + (++i) + " times");
            }
            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: finish the following methods: readVideoBasic, readVideoView, readContent
}