import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DatabaseMani {
    private String database = "project1";
    private String username = "Steven";
    private String password = "****";
    private Connection c = null;
    String errorName = "";
    long num;

    public Connection Connect() { // get connection to database

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

    private void closeConnection() { // close connection to database
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

            br.readLine();
            while ((line = br.readLine()) != null) {
                String regex = "(\"[^\"]*(\n[^\"])*\"|[^,\"]*)*";
                StringBuilder sb = new StringBuilder(line);
                while (sb.toString().split("\"", -1).length % 2 == 0) {
                    line = br.readLine();
                    sb.append("\n").append(line);
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
                            replace("\"", "").replaceAll("\\[|\\]|\'", "").split(",", -1)));
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

    public void readContent() { // read danmu.csv and insert into database
        Connect();

        String csvFile = "src/danmu.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String line;
            Scanner in = new Scanner(System.in);
            long i = in.nextLong(); // FIXME: just for test
            num = i;

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
                }
                line = sb.toString();
                List<String> fields = parseCSVLine(line, regex);
                errorName = fields.get(0) + "," + fields.get(1) + "," + fields.get(2);

                String sql = "INSERT INTO content VALUES(?,?,?,?,?)";
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setLong(1, ++i);
                stmt.setString(2, fields.get(0));
                stmt.setLong(3, Long.parseLong(fields.get(1)));
                stmt.setDouble(4, Double.parseDouble(fields.get(2)));
                stmt.setString(5, fields.get(3));
                stmt.executeUpdate();
                num++;
                if (i % 1000 == 0)
                    System.out.println(i);
            }
            closeConnection();
        } catch (Exception e) {
            System.err.println(num);
            System.err.println(errorName);
            e.printStackTrace();
            closeConnection();
        }
    }

    public void readVideo() {
        /* read videos.csv and insert into table
            video_basic, video_view, like_id, coin_id, favorite_id
        */
        Connect();

        String csvFile = "src/videos.csv";
        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            String regex = "(\"[^\"]*(\n[^\"])*\"|[^,\"]*)*";
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

            br.readLine();
            while ((line = br.readLine()) != null) {
                StringBuilder sb = new StringBuilder(line);
                while (sb.toString().split("\"", -1).length % 2 == 0) {
                    line = br.readLine();
                    sb.append("\n").append(line);
                }
                line = sb.toString();
                List<String> fields = parseCSVLine(line, regex);
                List<String> list1 = Arrays.stream(fields.get(10)
                        .replaceAll("\\[|\\]|\'|\"", "").replaceAll(" ", "")
                        .split(",", -1)).toList();
                List<String> list2 = Arrays.stream(fields.get(11)
                        .replaceAll("\\[|\\]|\'|\"", "").replaceAll(" ", "")
                        .split(",", -1)).toList();
                List<String> list3 = Arrays.stream(fields.get(12)
                        .replaceAll("\\[|\\]|\'|\"", "").replaceAll(" ", "")
                        .split(",", -1)).toList();
                List<String> list4 = Arrays.stream(fields.get(13)
                        .replaceAll("\\[|\\]|\\(|\\)|\'|\"", "").replaceAll(" ", "")
                        .split(",", -1)).toList();

                String sql = "INSERT INTO video_basic VALUES(?,?,?,?,?,?,?,?,?,?)"; // video basic
                PreparedStatement stmt = c.prepareStatement(sql);
                stmt.setString(1, fields.get(0));
                stmt.setString(2, fields.get(1));
                stmt.setLong(3, Long.parseLong(fields.get(2)));
                stmt.setString(4, fields.get(3));
                stmt.setTimestamp(5, new Timestamp(dateFormat.parse(fields.get(4)).getTime()));
                stmt.setTimestamp(6, new Timestamp(dateFormat.parse(fields.get(5)).getTime()));
                stmt.setTimestamp(7, new Timestamp(dateFormat.parse(fields.get(6)).getTime()));
                stmt.setInt(8, Integer.parseInt(fields.get(7)));
                stmt.setString(9, fields.get(8));
                stmt.setLong(10, Long.parseLong(fields.get(9)));
                stmt.executeUpdate();

                for (int j = 0; j < list4.size(); j += 2) {
                    sql = "INSERT INTO video_view VALUES(?,?,?)"; // video view
                    stmt = c.prepareStatement(sql);
                    stmt.setString(1, fields.get(0));
                    stmt.setLong(2, Long.parseLong(list4.get(j)));
                    stmt.setInt(3, Integer.parseInt(list4.get(j + 1)));
                    stmt.executeUpdate();
                }

                sql = "INSERT INTO like_id VALUES(?,?)"; // like id
                stmt = c.prepareStatement(sql);
                for (String s : list1) {
                    stmt.setString(1, fields.get(0));
                    stmt.setLong(2, Long.parseLong(s));
                    stmt.executeUpdate();
                }

                sql = "INSERT INTO coin_id VALUES(?,?)"; // coin id
                stmt = c.prepareStatement(sql);
                for (String s : list2) {
                    stmt.setString(1, fields.get(0));
                    stmt.setLong(2, Long.parseLong(s));
                    stmt.executeUpdate();
                }

                sql = "INSERT INTO favorite_id VALUES(?,?)"; // favorite id
                stmt = c.prepareStatement(sql);
                for (String s : list3) {
                    stmt.setString(1, fields.get(0));
                    stmt.setLong(2, Long.parseLong(s));
                    stmt.executeUpdate();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            closeConnection();
        }
    }

    private static List<String> parseCSVLine(String line, String regex) { // parse csv line
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
}
