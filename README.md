# Database_Proj_1
## CS307 project No.1

This is about a the structure of a fictional Danmu video website – Synchronized User-generated Subtitle Technology Company (SUSTC).



We got 3 files: users.csv, videos.csv and danmu.csv.



We plan to use 7 tables to manage all the data.
- users: to store some of the data of users
- video_basic: to store basic data of videos
- video_view: to record users who have viewed the video and their last watch time duration.
- content: to store all contents in all videos, which we call "Dan Mu" in Chinese.
- like_id : to store users id who like the videos
- coin_id : to store users id who give coins for the videos
- favorite_id : to store users' id and their favorite videos BV.



We have input all the data into the database named =="project1"==. And we got statistic that there 37881 users, 7865 videos and 12478996 contents at all.
## before running u need to do :
```bash
1. you`d better close all computer defend ,just like windows`s Antimalware Service Executable
2. you`d better change your datebase`s conf into these [postgresql.conf](src%2Fpostgresql.conf)
3. creat a datebase using [proj.sql](proj.sql)
4. u need to set run Configurations :[Main.xml](.idea%2FrunConfigurations%2FMain.xml)
u need to set <option name="VM_PARAMETERS" value="-Xmx10g " />, which is in The above file
      "-Xmx10g " means The maximum heap memory is 10gb, more heap memory allows more IntervalOfExecution , means the program will be faster
 please Make sure The maximum heap memory and IntervalOfExecution match !!!! 
5. u need to set follow Program arguments   
          String   databaseSoftware = args[0]; //数据库选择
          int IntervalOfExecution = Integer.parseInt(args[1]);//进行多少次循环执行一次预处理命令
          String ConnectionPoll = args[2];//数据库连接池选择 ，有助于提高导入速度，JDBC默认是没有连接池的
          int MaximumPoolSize = Integer.parseInt(args[3]);//最大连接池个数，或者是最大允许多少连接，这个值需要大于thread
          int thread = Integer.parseInt(args[4]);//线程数,最好和cpu核数相同
6.set your database, username, password   at   [DatabaseMani.java](src%2FDatabaseMani.java)     
7. Here are commands:
readusers   
readcontents
readvideos
runAll //run above 3 commands
clearall //This is THE SCRIPT that formats the database and returns it to a no-data state
exit 


```


## Something about my computer:
```bash
System Model: OMEN by HP Gaming Laptop 16-k0xxx
Processor: 12th Gen Intel(R) Core(TM) i7-12700H (20 CPUs), ~2.3GHz
Memory: 16384MB RAM
Available OS Memory: 16050MB RAM
Microsoft Graphics Hybrid: Supported
DirectX Database Version: 1.4.7
DxDiag Version: 10.00.22621.0001 64bit Unicode
```
