package jdbcPractice;

import java.sql.*;

import java.util.*;

public class ͼ��ݵ�¼ {
	// ����ģ���û���½�ɹ���ʵ��
	public static void main(String[] args) {
		try (Scanner s = new Scanner(System.in)) {
			System.out.println("�û�����");
			String lName = s.nextLine();
			System.out.println("���룺");
			String lPwd = s.nextLine();
			Map<String, String> userinfo = new HashMap<>();
			userinfo.put("lName", lName);
			userinfo.put("lPwd", lPwd);
			// ��֤�û������뷽��login
			boolean loginSuccess = login(userinfo);
			System.out.println(loginSuccess ? "��½�ɹ�" : "��½ʧ��");
			if (loginSuccess) {
				System.out.println("��������������");
				System.out.println("0->�˳�");
				System.out.println("1->��ѯָ���鼮��Ϣ");
				System.out.println("2->����");
				System.out.println("3->����");
				System.out.println("4->����ͼ��");
				System.out.println("5->��ѯ�Լ��������");
				int num = s.nextInt();
				while (num != 0) {
					// 1����
					if (num == 1) {
						System.out.println("������Ҫ��Ѱ�鼮���ƣ�");
						// ע����next(),������nextLine()Ŷ
						String s1 = s.next();
						ͼ���.select(s1);
						// 2����
					} else if (num == 2) {
						System.out.println("������Ҫ�����鼮���ƣ�");
						// ע����next(),������nextLine()Ŷ
						String s2 = s.next();
						ͼ���.borrow(s2);
					}
					// 3����
					else if (num == 3) {
						System.out.println("������Ҫ���鼮���ƣ�");
						String s3 = s.next();
						ͼ���.back(s3);
					}
					// 4����
					else if (num == 4) {
						System.out.println("����������ͼ��ı�ţ��������۸����ߣ�");
						String bno = s.next();
						String bname = s.next();
						double price = s.nextDouble();
						String author = s.next();
						ͼ���.add(bno, bname, price, author);
					}
					//5��ѯ����������
					else if (num == 5) {
						ͼ���.aboutme();
					}
					// ��������
					else {
						System.out.println("���벻�Ϸ������������룡");
					}
					num = s.nextInt();
				}
				//���Ҫ�˳������session��
				exit();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

//��¼����
	private static boolean login(Map<String, String> userLoginfo) {
		// ������ʶ��
		boolean loginSuccess = false;
		// �������������
		// ��ֵ���е�ֵ
		String loginName = userLoginfo.get("lName");
		String loginPwd = userLoginfo.get("lPwd");

		// JDBC����
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		try {
			// 1.ע������
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 2.��ȡ����
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yy1?useUnicode=true&characterEncoding="
					+ "utf8&useSSL=false&serverTimezone=Asia/Shanghai", "root", "<06011127>");
			// 3.��ȡ���ݿ��������
			stat = conn.createStatement();
			// 4.ִ��sql���
			String sql = "select * from student where sname = '" + loginName + "' and psaaword = '" + loginPwd + "'";
			rs = stat.executeQuery(sql);
			// 5.�����ѯ�����
			if (rs.next()) {
				loginSuccess = true;
				// ����¼��Ϣ����αsession���У����ں���ѧ�������ȷ�����ϵ
				// sb���session��sno���û������bno
				// ��sessionΪ�գ�˵��session�������ݣ���δ��¼���½ʧ�ܣ��޷�����
				// ����˳���¼ֻҪɾ��session���е����ݼ���
				//
				// INSERT INTO Ŀ��� (�ֶ�1, �ֶ�2, ...) SELECT �ֶ�1, �ֶ�2, ... FROM ��Դ��
				// �ǵüӵ����� "'"
				String sql2 = "insert into session(sno,sname) select sno,sname from student s where" + " s.sname=" + "'"
						+ loginName + "'";
				stat.executeUpdate(sql2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		// 6.�ͷ���Դ
		finally {
			JDBCUtil.close(conn, stat, rs);
		}
		return loginSuccess;
	}

	// �˳�����
	private static void exit() {
		Connection conn = null;
		Statement stat = null;
		try {
			// 1.ע������
			Class.forName("com.mysql.cj.jdbc.Driver");
			// 2.��ȡ����
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yy1?useUnicode=true&characterEncoding="
					+ "utf8&useSSL=false&serverTimezone=Asia/Shanghai", "root", "<06011127>");
			// 3.��ȡ���ݿ��������
			stat = conn.createStatement();
			// 4.ִ��sql���
			String sql1 = "delete from session";
			int count = stat.executeUpdate(sql1);
			// count:��Ӱ����������
			System.out.println(count == 1 ? "�ɹ��˳�!" : "�ϸ��û��˳��쳣");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}



