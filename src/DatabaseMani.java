import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseMani {
    private String database = "project1";
    private String username = "Steven";
    private String password = "7818";
    private Connection c = null;
    String errorName = "";

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
                System.out.println("Close connection success");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void readUser() { // read users.csv and insert into database
        Connect();

        String csvFile = "src/users.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            Scanner in = new Scanner(System.in);
            int i = in.nextInt();

            br.readLine();
            for (int j = 0; j < i; j++) {
                br.readLine();
            }

            while ((line = br.readLine()) != null) {
                String regex = "(\"[^\"]*(\n[^\"])*\"|[^,\"]*)*";
                StringBuilder sb = new StringBuilder(line);
                while (sb.toString().split("\"", -1).length % 2 == 0) {
                    line = br.readLine();
                    sb.append("\n").append(line);
                    i++;
                }
                line = sb.toString();
                List<String> fields = parseCSVLine(line, regex);

                String sql = "INSERT INTO users VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setLong(1, Long.parseLong(fields.get(0)));
                stmt.setString(2, fields.get(1));
                stmt.setString(3, fields.get(2));
                stmt.setString(4, fields.get(3));
                stmt.setInt(5, Integer.parseInt(fields.get(4)));
                stmt.setString(6, fields.get(5));
                if (fields.get(6).equals("[]")) {
                    stmt.setArray(7, c.createArrayOf("bigint", new String[]{}));
                } else {
                    stmt.setArray(7, c.createArrayOf("bigint", fields.get(6).
                            replace("\"","").replaceAll("\\[|\\]|\'", "").split(",", -1)));
                }
                stmt.setString(8, fields.get(7));
                stmt.executeUpdate();
            }
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    public void readContent() {
        Connect();

        String csvFile = "src/danmu.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            Scanner in = new Scanner(System.in);
            int i = in.nextInt();

            br.readLine();
            for (int j = 0; j < i; j++) {
                br.readLine();
            }

            while ((line = br.readLine()) != null) {
                String regex = "(\"[^\"]*(\n[^\"])*\"|[^,\"]*)*";
                String sql = "INSERT INTO content VALUES(?,?,?,?,?)";
                PreparedStatement stmt = c.prepareStatement(sql);
                String[] data = line.split(",", -1);
                errorName = data[0] + " " + data[1] + " " + data[2] + " " + data[3];

                stmt.setInt(1, ++i);
                stmt.setString(2, data[0]);
                stmt.setLong(3, Long.parseLong(data[1]));
                stmt.setDouble(4, Double.parseDouble(data[2]));
                StringBuilder str = new StringBuilder();
                for (i = 3; i < data.length; i++) {
                    str.append(data[i]);
                }
                stmt.setString(5, str.toString());
                int num = stmt.executeUpdate();
                System.out.println("success " + i + " times");
                System.out.println(num);
                closeConnection();
            }
        } catch (Exception e) {
            System.err.println(errorName);
            e.printStackTrace();
            closeConnection();
        }
    }

    private static List<String> parseCSVLine(String line, String regex) {
        List<String> fields = new ArrayList<>();
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        while (matcher.find()) {
            String field = matcher.group();
            if (!fields.isEmpty() && !field.isEmpty() && fields.get(fields.size() - 1).isEmpty()) {
                fields.remove(fields.size() - 1);
            }
            fields.add(field);
        }
        return fields;
    }



    public void DeleteUsers() {
        Connect();
        try {
            String sql = "DELETE FROM users;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public void DeleteVideos() {
        Connect();
        try {
            String sql = "DELETE FROM video_basic;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }

    public void DeleteView() {
        Connect();
        try {
            String sql = "DELETE FROM video_view;";
            PreparedStatement stmt = c.prepareStatement(sql);
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        closeConnection();
    }
    // TODO: finish the following methods: readVideoBasic, readVideoView, readContent
}
