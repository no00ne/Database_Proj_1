import java.util.Scanner;

public class runThis {
    public static void main(String[] args) {


        String databaseSoftware = "postgresql";//数据库选择
        int IntervalOfExecution = 500;//进行多少次循环执行一次预处理命令
        String ConnectionPoll = "Hikari";//数据库连接池选择，有助于提高导入速度，JDBC默认是没有连接池的
        int MaximumPoolSize = 10;//最大连接池个数，或者是最大允许多少连接，这个值需要大于thread
        int thread = 16;//线程数,最好和cpu核数相同

        if (args.length>0) {
            databaseSoftware = args[0];
            IntervalOfExecution = Integer.parseInt(args[1]);
            ConnectionPoll = args[2];
            MaximumPoolSize = Integer.parseInt(args[3]);
            thread = Integer.parseInt(args[4]);

        }
        try {
            DatabaseMani dm = new DataFactory().createDataMani("");
            System.out.println("Connected");
            Scanner in = new Scanner(System.in);
            while (in.hasNext()) {
                String text = in.nextLine();
                long start = System.nanoTime(); // time spent
                if (text.equalsIgnoreCase("readusers")) {
                    dm.readUser(databaseSoftware, IntervalOfExecution, ConnectionPoll, MaximumPoolSize, thread);
                } else if (text.equalsIgnoreCase("readcontents")) {
                    dm.readContent(databaseSoftware, IntervalOfExecution, ConnectionPoll, MaximumPoolSize, thread);
                } else if (text.equalsIgnoreCase("readvideos")) {
                    dm.readVideo(databaseSoftware, IntervalOfExecution, ConnectionPoll, MaximumPoolSize, thread);
                } else if (text.equalsIgnoreCase("runAll")) {
                    dm.readUser(databaseSoftware, IntervalOfExecution, ConnectionPoll, MaximumPoolSize, thread);
                    dm.readContent(databaseSoftware, IntervalOfExecution, ConnectionPoll, MaximumPoolSize, thread);
                    dm.readVideo(databaseSoftware, IntervalOfExecution, ConnectionPoll, MaximumPoolSize, thread);
                } else if (text.equalsIgnoreCase("clearall")) {
                    dm.clearAll();//这是格式化数据库的脚本，可以就数据库回复到无数据的状态
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
