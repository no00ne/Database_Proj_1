import java.sql.*;
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
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseMani {
    private String database = "postgres";
    private String username = "postgres";
    private String password = "";
    private Connection c = null;
    String errorName = "";
    long num;

    public Connection Connect() { // get connection to database
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/" + database);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(10);
        HikariDataSource dataSource = new HikariDataSource(config);
        try {
            c = dataSource.getConnection();
            System.out.println("Connect success");
            return c;
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            System.exit(0);
            return null;
        }
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

                String sql = "INSERT INTO project1.users VALUES(?, ?, ?, ?, ?, ?, ?, ?);";
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
        int i=0;//计数器

        /* read videos.csv and insert into table
            video_basic, video_view, like_id, coin_id, favorite_id
        */
        Connect();






       /* try {
            disableConstraintsInSchema(c, "project1");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }*/
        String csvFile = "src/videos.csv";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            String regex = "(\"[^\"]*(\n[^\"])*\"|[^,\"]*)*";
            String line;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            br.readLine();
            String insertVideoSQL = "INSERT INTO project1.video_basic VALUES(?,?,?,?,?,?,?,?,?,?)";
            String insertViewSQL = "INSERT INTO project1.video_view VALUES(?,?,?)";
            String insertLikeSQL = "INSERT INTO project1.like_id VALUES(?,?)";
            String insertCoinSQL = "INSERT INTO project1.coin_id VALUES(?,?)";
            String insertFavoriteSQL = "INSERT INTO project1.favorite_id VALUES(?,?)";

        PreparedStatement insertVideoStmt = c.prepareStatement(insertVideoSQL);
        PreparedStatement insertViewStmt = c.prepareStatement(insertViewSQL);
        PreparedStatement insertLikeStmt = c.prepareStatement(insertLikeSQL);
        PreparedStatement insertCoinStmt = c.prepareStatement(insertCoinSQL);
        PreparedStatement insertFavoriteStmt = c.prepareStatement(insertFavoriteSQL);


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


                insertVideoStmt.setString(1, fields.get(0));
                insertVideoStmt.setString(2, fields.get(1));
                insertVideoStmt.setLong(3, Long.parseLong(fields.get(2)));
                insertVideoStmt.setString(4, fields.get(3));
               insertVideoStmt.setTimestamp(5, new Timestamp(dateFormat.parse(fields.get(4)).getTime()));
                insertVideoStmt.setTimestamp(6, new Timestamp(dateFormat.parse(fields.get(5)).getTime()));
                insertVideoStmt.setTimestamp(7, new Timestamp(dateFormat.parse(fields.get(6)).getTime()));
                insertVideoStmt.setInt(8, Integer.parseInt(fields.get(7)));
                insertVideoStmt.setString(9, fields.get(8));
                insertVideoStmt.setLong(10, Long.parseLong(fields.get(9)));
                insertVideoStmt.executeUpdate();

            // 批量插入 video_view
            for (int j = 0; j < list4.size(); j += 2) {
                insertViewStmt.setString(1, fields.get(0));
                insertViewStmt.setLong(2, Long.parseLong(list4.get(j)));
                insertViewStmt.setInt(3, Integer.parseInt(list4.get(j + 1)));
                insertViewStmt.addBatch();
            }

            // 批量插入 like_id
            for (String s : list1) {
                insertLikeStmt.setString(1, fields.get(0));
                insertLikeStmt.setLong(2, Long.parseLong(s));
                insertLikeStmt.addBatch();
            }

            // 批量插入 coin_id
            for (String s : list2) {
                insertCoinStmt.setString(1, fields.get(0));
                insertCoinStmt.setLong(2, Long.parseLong(s));
                insertCoinStmt.addBatch();
            }

            // 批量插入 favorite_id
            for (String s : list3) {
                insertFavoriteStmt.setString(1, fields.get(0));
                insertFavoriteStmt.setLong(2, Long.parseLong(s));
                insertFavoriteStmt.addBatch();
            }
                System.out.println(++i);
                if (i % 500 == 0) {
                    // 在 i 被 100 整除时执行的操作
                    Statement state = c.createStatement();
                    state.executeUpdate("SET CONSTRAINTS ALL DEFERRED;");
                    insertVideoStmt.executeBatch();
                    insertViewStmt.executeBatch();
                    insertLikeStmt.executeBatch();
                    insertCoinStmt.executeBatch();
                    insertFavoriteStmt.executeBatch();
                }
            }

        // 执行批量插入
        insertVideoStmt.executeBatch();
        insertViewStmt.executeBatch();
        insertLikeStmt.executeBatch();
        insertCoinStmt.executeBatch();
        insertFavoriteStmt.executeBatch();
            Statement state = c.createStatement();
            state.executeUpdate("SET CONSTRAINTS ALL IMMEDIATE");
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

 /*   public static void disableConstraintsInSchema(Connection connection, String schemaName) throws Exception {
        // 构造 SQL 查询，用于获取指定 schema 内的所有约束的名称
        String query = "SELECT conname FROM pg_constraint WHERE connamespace = (SELECT oid FROM pg_namespace WHERE nspname = ?)";

        // 构建 PreparedStatement 对象
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, schemaName);

        // 执行查询
        ResultSet resultSet = preparedStatement.executeQuery();

        // 依次禁用每个约束
        while (resultSet.next()) {
            String constraintName = resultSet.getString(1);
            String disableConstraintSQL = "ALTER TABLE " + schemaName + "." + constraintName + " DISABLE TRIGGER ALL";
            PreparedStatement disableStatement = connection.prepareStatement(disableConstraintSQL);
            disableStatement.executeUpdate();
            disableStatement.close();
        }

        // 关闭资源
        resultSet.close();
        preparedStatement.close();
    }*/
}

