package jdbcPractice;

import java.sql.*;

public class ͼ��� {
	static Connection con = null;
	static PreparedStatement ps = null;

	// ����ͼ��
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
			System.out.println("��ӳɹ�");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// ��������ѯͼ����Ϣ
	public static void select(String x) {
		connection();
		String sql = "select * from book where bname =?";
		try {
			ps = con.prepareStatement(sql);
			ps.setString(1, x);
			ResultSet rs = ps.executeQuery();
			System.out.println("���      ����          �۸�      ����        ����");
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
				System.out.println("δ�ҵ���ͼ�飡");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close();
		}
	}

	// ����
	// ����дtrycatch��ֱ��throws
	public static void borrow(String bname) throws Exception {
		connection();
		int i = -1;
		String sql = "select * from book where bname=?";
		ps = con.prepareStatement(sql);
		ps.setString(1, bname);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			i = rs.getInt("remaining");// getint()������ǰ����ָ���е�ֵ�������е�intֵΪnull����0
		}
		if (i > 0) {
			String sq = "update book set remaining =remaining-1  where bname =?";
			PreparedStatement pt = con.prepareStatement(sq);
			pt.setString(1, bname);
			pt.executeUpdate();
			System.out.println("�ѳɹ����");
			// ��sb���и������ݣ�
			String sql2 = "insert into sb(sno,bno) values((select sno from session),(select bno from book where bname=?))";
			PreparedStatement pt2 = con.prepareStatement(sql2);
			pt2.setString(1, bname);
			pt2.executeUpdate();
		} else if (i == 0) {
			System.out.println("���޿��");
		} else {
			System.out.println("���޴�ͼ��");
		}

	}

	// ����
	public static void back(String bname) throws Exception {
		connection();
		// ȷ�ϴ�ʱsession���sno��sb��sno�Ƿ�һ����Ҫ�����鼮�Ƿ���sb����ѡ��sno��bnoһ�£�һ������Ի��飬���򲻿���

		String sql = "select sno,bno from sb where sno=(select sno from session) and bno=(select bno from book where bname=?) ";
		ps = con.prepareStatement(sql);
		ps.setString(1, bname);
		ResultSet rs3 = ps.executeQuery();
		if (rs3.next()) {
			// ����book�������
			String sql2 = "update book set remaining =remaining+1 where bname =?";
			ps = con.prepareStatement(sql2);
			ps.setString(1, bname);
			ps.executeUpdate();
			// ɾ��sb������н����¼
			String sql3 = "delete from sb where id = (select a.id from(select max(id) as id from sb where sno=(select sno from session) and bno=(select bno from book where bname=?))a);";
			PreparedStatement pt2 = con.prepareStatement(sql3);
			pt2.setString(1, bname);
			pt2.executeUpdate();
			System.out.println("�黹�ɹ�");
		} else {
			System.out.println("�����������Ҫ���黹�����¼");
		}
	}

	// �û���ѯ�Լ������ļ����飺
	public static void aboutme() throws Exception {
		connection();
		int count = 0;
		//ȷ�����û�һ�����˼�����
		String sql1 = "select count(*) conb from sb join session s on s.sno=sb.sno";
		ps = con.prepareStatement(sql1);
		ResultSet rs = ps.executeQuery(sql1);
		if (rs.next()) {
			count = rs.getInt("conb");
		}
		if (count == 0) {
			System.out.println("����δ���Ĺ��κ��鼮���������ɣ�");
			return;
		}
		//��ӡ���û������鼮����
		System.out.println("�ܹ�����" + count + "�����ֱ�Ϊ��");
		String sql2 = "select bname from book join sb on book.bno=sb.bno join session s on s.sno=sb.sno";
		ps = con.prepareStatement(sql2);
		rs = ps.executeQuery(sql2);
		while (rs.next()) {
			String bname = rs.getString("bname");
			System.out.println(bname);
		}
	}

	// ��������
	// �����׳��쳣������try catch
	public static void connection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection("jdbc:mysql://localhost:3306/yy1?useUnicode=true&characterEncoding="
					+ "utf8&useSSL=false&serverTimezone=Asia/Shanghai", "root", "<06011127>");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//�ر�
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
