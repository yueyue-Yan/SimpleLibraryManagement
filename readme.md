### ����Javaʵ�ֵ�ͼ�����ϵͳ

##### ������

ֻ�豾�����У�����Java���ԣ����������ҳ�������棬��Ҫ�ڳ��򽻻�������ʾ�û�������

***

##### ��������

Java  jdbc MySQL

***

##### ����ʵ��

1. ��½�˳�

2. ����

3. ����

4. ����

5. ����ָ���鼮��Ϣ

6. ���ĸ��˽�����Ϣ 

***

##### Ŀ¼����

- MySQL���ݱ��

  - student(sno;sname;password)

  - sb(sno;bno)

  - book(bno;bname;price;author;remaining)

  - session

    [����Դ��](https://c.runoob.com/front-end/712)

- .Java�ļ�

  - ͼ��ݵ�¼.java

  - ͼ���.java

    [Դ����](https://c.runoob.com/front-end/712)

***

##### �������⼰�������

**1. ��ε�¼ʵ���˳����������ݱ�����ϵ��**

����session���洢��ǰ��¼���û����˳�����ʱ�����session��

**2.һ�˽��Ķ౾��ͬ�鼮���������һ��ֻ��һ������sb��������ֻɾ��һ����**

����id��ɾ��id����

ʱ�����ɾ��ʱ�������

**3. ���ʵ�֡��Լ������Լ�������**

��ѯsb��ʱ��֤sno��������session�е�sno������ǰ�û���bno�����û�����Ҫ�����鼮��

��ѯ�Ƿ��ж�Ӧ�����ݣ����У���֤�������ǵ�ǰ��¼�û������ģ����Թ黹��

~~~
// ȷ�ϴ�ʱsession���sno��sb��sno�Ƿ�һ����Ҫ�����鼮�Ƿ���sb����ѡ��sno��bnoһ�£�һ������Ի��飬���򲻿���
		String sql = "select sno,bno from sb where sno=(select sno from session) and bno=(select bno from book where bname=?) ";
		ps = con.prepareStatement(sql);
		ps.setString(1, bname);
		ResultSet rs3 = ps.executeQuery();
~~~

**4.�ڵ�½���������û�������Scanner���ܽ��ն��String **

��nextline()��Ϊnext()

***

