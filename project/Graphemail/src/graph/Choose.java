package graph;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Choose extends JFrame implements ActionListener{
	JPanel jp1,jp2,jp3;
	JButton jb1,jb2,jb3;
	String user,password,user64,password64;
	String mailtype;
	
	//构造函数
	public Choose(String tuser,String tpass,String tuser64,String tpassword64,String type) {
		// TODO Auto-generated constructor stub
		
		user=tuser;
		password=tpass;
		user64=tuser64;
		password64=tpassword64;
		mailtype=type;
		
		//创建面板
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		
		//创建按钮
		jb1=new JButton("发送邮件");
		jb2=new JButton("接收邮件");
	    jb3=new JButton("退        出");
		
		//设置布局管理
		this.setLayout(new GridLayout(3,1));
		
		//加入组件
		jp1.add(jb1);
		jp2.add(jb2);
		jp3.add(jb3);
		
		//加入到Frame
		this.add(jp1);
		this.add(jp2);
		this.add(jp3);
		
		//设置窗体
		this.setTitle("选择操作");
		this.setSize(300, 200);
		this.setLocationRelativeTo(null);//居中
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭
		this.setVisible(true);
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="发送邮件"){
			//dispose();
			Send send=new Send(user,user64,password64,mailtype);
		}
		else if(e.getActionCommand()=="接收邮件"){
			//dispose();
			Recv recv=new Recv(user,password,user64,password64,mailtype);
		}
		else if(e.getActionCommand()=="退        出"){
			dispose();
		}
	}

}
