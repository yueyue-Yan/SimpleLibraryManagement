package jdbcPractice;

import java.sql.*;

public class 图书馆 {
	static Connection con = null;
	static PreparedStatement ps = null;

	// 增添图书
	public static void add(String bno, String bname, double price, String author) {
		try {
			connection();
			String sql = "insert into book values(?,?,?,?,10)";
			ps = con.prepareStatement(sql);
			ps.setString(1, bno);
			ps.setString(2, bname);
			ps.setDouble(3, price);
			ps.setString(4, author);
			ps.executeUpdate();
			System.out.println("添加成功");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// 靠书名查询图书信息
	public static void select(String x) {
		connection();
		String sql = "select * from book where bname =?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, x);
			ResultSet rs = ps.executeQuery();
			System.out.println("编号      书名          价格      作者        余量");
			String bno = "";
			while (rs.next()) {
				bno = rs.getString("bno");
				String bname = rs.getString("bname");
				double price = rs.getDouble("price");
				String author = rs.getString("author");
				int remaining = rs.getInt("remaining");
				System.out.println(bno + "    " + bname + "    " + price + "    " + author + "    " + remaining);
			}
			if (bno == "")
				System.out.println("未找到该图书！");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// 借书
	// 懒得写trycatch了直接throws
	public static void borrow(String bname) throws Exception {
		connection();
		int i = -1;
		String sql = "select * from book where bname=?";
		ps = con.prepareStatement(sql);
		ps.setString(1, bname);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			i = rs.getInt("remaining");// getint()检索当前行中指定列的值，若该列的int值为null返回0
		}
		if (i > 0) {
			String sq = "update book set remaining =remaining-1  where bname =?";
			PreparedStatement pt = con.prepareStatement(sq);
			pt.setString(1, bname);
			pt.executeUpdate();
			System.out.println("已成功借出");
			// 像sb表中更新数据；
			String sql2 = "insert into sb(sno,bno) values((select sno from session),(select bno from book where bname=?))";
			PreparedStatement pt2 = con.prepareStatement(sql2);
			pt2.setString(1, bname);
			pt2.executeUpdate();
		} else if (i == 0) {
			System.out.println("暂无库存");
		} else {
			System.out.println("暂无此图书");
		}

	}

	// 还书
	public static void back(String bname) throws Exception {
		connection();
		// 确认此时session表的sno与sb中sno是否一致且要还的书籍是否与sb表中选中sno的bno一致，一致则可以还书，否则不可以

		String sql = "select sno,bno from sb where sno=(select sno from session) and bno=(select bno from book where bname=?) ";
		ps = con.prepareStatement(sql);
		ps.setString(1, bname);
		ResultSet rs3 = ps.executeQuery();
		if (rs3.next()) {
			// 更新book表的余量
			String sql2 = "update book set remaining =remaining+1 where bname =?";
			ps = con.prepareStatement(sql2);
			ps.setString(1, bname);
			ps.executeUpdate();
			// 删除sb表该条中借书记录
			String sql3 = "delete from sb where id = (select a.id from(select max(id) as id from sb where sno=(select sno from session) and bno=(select bno from book where bname=?))a);";
			PreparedStatement pt2 = con.prepareStatement(sql3);
			pt2.setString(1, bname);
			pt2.executeUpdate();
			System.out.println("归还成功");
		} else {
			System.out.println("书库中暂无需要您归还该书记录");
		}
	}

	// 用户查询自己借了哪几本书：
	public static void aboutme() throws Exception {
		connection();
		int count = 0;
		//确定该用户一共借了几本书
		String sql1 = "select count(*) conb from sb join session s on s.sno=sb.sno";
		ps = con.prepareStatement(sql1);
		ResultSet rs = ps.executeQuery(sql1);
		if (rs.next()) {
			count = rs.getInt("conb");
		}
		if (count == 0) {
			System.out.println("您还未借阅过任何书籍，多读点书吧！");
			return;
		}
		//打印出用户所借书籍名称
		System.out.println("总共借书" + count + "本，分别为：");
		String sql2 = "select bname from book join sb on book.bno=sb.bno join session s on s.sno=sb.sno";
		ps = con.prepareStatement(sql2);
		rs = ps.executeQuery(sql2);
		while (rs.next()) {
			String bname = rs.getString("bname");
			System.out.println(bname);
		}
	}

	// 创建驱动
	// 不能抛出异常。必须try catch
	public static void connection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/yy1?useUnicode=true&characterEncoding="
					+ "utf8&useSSL=false&serverTimezone=Asia/Shanghai", "root", "<06011127>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//关闭
	public static void close() {
		try {
			ps.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}
}
