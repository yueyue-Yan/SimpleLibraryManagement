### 基于Java实现的图书管理系统

##### 需求简介

只需本地运行，基于Java语言，无需设计网页交互界面，但要在程序交互界面提示用户操作。

***

##### 环境依赖

Java  jdbc MySQL

***

##### 功能实现

1. 登陆退出

2. 借书

3. 还书

4. 加书

5. 查阅指定书籍信息

6. 查阅个人借书信息 

***

##### 目录描述

- MySQL数据表格

  - student(sno;sname;password)

  - sb(sno;bno)

  - book(bno;bname;price;author;remaining)

  - session

- .Java文件

  - 图书馆登录.java

  - 图书馆.java


***

##### 遇到问题及解决方案

**1. 如何登录实现退出操作与数据表相联系？**

创建session表，存储当前登录的用户，退出操作时，清空session表

**2.一人借阅多本相同书籍，如何做到一次只还一本？（sb表中数据只删除一条）**

自增id，删除id最大的

时间戳，删除时间戳最大的

**3. 如何实现“自己借书自己还”？**

查询sb表时保证sno等于来自session中的sno，及当前用户；bno来自用户输入要还的书籍，

查询是否有对应的数据，若有，则证明该书是当前登录用户所借阅，可以归还。

~~~
// 确认此时session表的sno与sb中sno是否一致且要还的书籍是否与sb表中选中sno的bno一致，一致则可以还书，否则不可以
		String sql = "select sno,bno from sb where sno=(select sno from session) and bno=(select bno from book where bname=?) ";
		ps = con.prepareStatement(sql);
		ps.setString(1, bname);
		ResultSet rs3 = ps.executeQuery();
~~~

**4. 在登陆方法接受用户输入中Scanner不能接收多个String**

将nextline()改为next()

***

