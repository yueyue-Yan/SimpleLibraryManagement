package jdbcPractice;

import java.sql.*;

import java.util.*;

public class 图书馆登录 {
	// 需求：模拟用户登陆成功的实现
	public static void main(String[] args) {
		try (Scanner s = new Scanner(System.in)) {
			System.out.println("用户名：");
			String lName = s.nextLine();
			System.out.println("密码：");
			String lPwd = s.nextLine();
			Map<String, String> userinfo = new HashMap<>();
			userinfo.put("lName", lName);
			userinfo.put("lPwd", lPwd);
			// 验证用户名密码方法login
			boolean loginSuccess = login(userinfo);
			System.out.println(loginSuccess ? "登陆成功" : "登陆失败");
			if (loginSuccess) {
				System.out.println("请输入您的需求：");
				System.out.println("0->退出");
				System.out.println("1->查询指定书籍信息");
				System.out.println("2->借书");
				System.out.println("3->还书");
				System.out.println("4->增加图书");
				System.out.println("5->查询自己借书情况");
				int num = s.nextInt();
				while (num != 0) {
					// 1查书
					if (num == 1) {
						System.out.println("请输入要查寻书籍名称：");
						// 注意用next(),而不是nextLine()哦
						String s1 = s.next();
						图书馆.select(s1);
						// 2借书
					} else if (num == 2) {
						System.out.println("请输入要借阅书籍名称：");
						// 注意用next(),而不是nextLine()哦
						String s2 = s.next();
						图书馆.borrow(s2);
					}
					// 3还书
					else if (num == 3) {
						System.out.println("请输入要还书籍名称：");
						String s3 = s.next();
						图书馆.back(s3);
					}
					// 4加书
					else if (num == 4) {
						System.out.println("请输入新增图书的编号，书名，价格，作者：");
						String bno = s.next();
						String bname = s.next();
						double price = s.nextDouble();
						String author = s.next();
						图书馆.add(bno, bname, price, author);
					}
					//5查询自身借书情况
					else if (num == 5) {
						图书馆.aboutme();
					}
					// 错误输入
					else {
						System.out.println("输入不合法！请重新输入！");
					}
					num = s.nextInt();
				}
				//最后要退出（清空session表）
				exit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//登录方法
	private static boolean login(Map<String, String> userLoginfo) {
		// 打标记意识：
		boolean loginSuccess = false;
		// 单独定义变量：
		// 键值对中的值
		String loginName = userLoginfo.get("lName");
		String loginPwd = userLoginfo.get("lPwd");

		// JDBC代码
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			// 1.注册驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 2.获取连接
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yy1?useUnicode=true&characterEncoding="
					+ "utf8&useSSL=false&serverTimezone=Asia/Shanghai", "root", "<06011127>");
			// 3.获取数据库操作对象
			stat = conn.createStatement();
			// 4.执行sql语句
			String sql = "select * from student where sname = '" + loginName + "' and psaaword = '" + loginPwd + "'";
			rs = stat.executeQuery(sql);
			// 5.处理查询结果集
			if (rs.next()) {
				loginSuccess = true;
				// 将登录信息放入伪session表中，便于后续学号与借书等方法联系
				// sb表加session表sno和用户需求的bno
				// 若session为空，说明session里无数据，即未登录或登陆失败，无法借书
				// 最后退出登录只要删掉session表中的数据即可
				//
				// INSERT INTO 目标表 (字段1, 字段2, ...) SELECT 字段1, 字段2, ... FROM 来源表
				// 记得加单引号 "'"
				String sql2 = "insert into session(sno,sname) select sno,sname from student s where" + " s.sname=" + "'"
						+ loginName + "'";
				stat.executeUpdate(sql2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 6.释放资源
		finally {
			JDBCUtil.close(conn, stat, rs);
		}
		return loginSuccess;
	}

	// 退出方法
	private static void exit() {
		Connection conn = null;
		Statement stat = null;
		try {
			// 1.注册驱动
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 2.获取连接
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yy1?useUnicode=true&characterEncoding="
					+ "utf8&useSSL=false&serverTimezone=Asia/Shanghai", "root", "<06011127>");
			// 3.获取数据库操作对象
			stat = conn.createStatement();
			// 4.执行sql语句
			String sql1 = "delete from session";
			int count = stat.executeUpdate(sql1);
			// count:被影响数据条数
			System.out.println(count == 1 ? "成功退出!" : "上个用户退出异常");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}



