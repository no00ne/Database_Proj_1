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

We use java to write a script to input all data. I know that Python is slower than Java so I just use java.

Java program spent about 17793.785s to import all data into ```video_view```, about 24220.655s to import ```like_id```, ```coin_id```, ```favorite_id``` in all, about 1403.025s to import ```content```.
Others spent much less time.

I don't have enough time to implement it, but I believe that the process can be faster if I use ```CPP``` or ```C```.

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
