package graph;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Content extends JFrame implements ActionListener{
	static String user,password;
	static String user64;
	static String password64;
	static String mailtype;
	static String num;
	String content;
	StringBuilder sb=new StringBuilder();
	String line_end="\r\n";
	JPanel jp1,jp2;
	JTextArea jta;
	JButton jb1,jb2;
	
	/*public static void main(String [] args) {
		Content content=new Content(num, user, password64, user64, password64, mailtype);
	}*/
	
	public Content(String tnum,String tuser,String tpass,String tuser64,String tpassword64,String type) {
		// TODO Auto-generated constructor stub
		user=tuser;
		password=tpass;
		user64=tuser64;
		password64=tpassword64;
		mailtype=type;
		num=tnum;
		
		jp1=new JPanel();
		jp2=new JPanel();
		jta=new JTextArea();
		jb1=new JButton("下载");
		jb2=new JButton("退出");
		
		jp1.add(jta);
		jp1.setLayout(null);
		jta.setBounds(0, 10, 290, 270);
		//jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		JScrollPane jsp=new JScrollPane(jta);
		jsp.setBounds(0,10,290,270);
		jp1.add(jsp);
		
		jb1.setBounds(60,300,80,40);
		jb2.setBounds(160,300,80,40);
		jp1.add(jb1);
		jp1.add(jb2);
		
		//this.setLayout(new GridLayout(2,1));
		this.add(jp1);
		//this.add(jp2);
		
		this.setTitle("邮件内容");
		this.setSize(300,400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		
		try {
			Socket socket=new Socket("pop3.".concat(mailtype).concat(".com"),110);
			BufferedReader br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			if(!Check.message(pw, br, sb, "+OK")){
				JOptionPane.showMessageDialog(null,"连接错误","提示消息",JOptionPane.WARNING_MESSAGE);
				dispose();
			}
			pw.write("user ".concat(user).concat(line_end));
			if(!Check.message(pw, br, sb, "+OK")){
				JOptionPane.showMessageDialog(null,"连接错误","提示消息",JOptionPane.WARNING_MESSAGE);
				dispose();
			}
			pw.write("pass ".concat(password).concat(line_end));
			if(!Check.message(pw, br, sb, "+OK")){
				JOptionPane.showMessageDialog(null,"连接错误","提示消息",JOptionPane.WARNING_MESSAGE);
				dispose();
			}
			pw.write("retr ".concat(num).concat(line_end));
			if(!Check.message(pw, br, sb, "+OK")){
				JOptionPane.showMessageDialog(null,"没有此邮件","提示消息",JOptionPane.WARNING_MESSAGE);
				dispose();
			}
			StringBuilder ct=new StringBuilder("");
			String str;
			while ((str=br.readLine())!=null) {
				System.out.println(str);
				ct.append(str);
				ct.append(line_end);
				if(str.equals(".")){
					break;
				}
			}
			content=new String(ct);
			jta.setText(content);
			pw.write("QUIT");
		    pw.close();
		    br.close();
		    socket.close();
		} catch (Exception e2) {
			// TODO: handle exception
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="下载"){
			try {
				File file=new File(num.concat(".txt"));
				FileWriter fileWriter=new FileWriter(file);
				fileWriter.write(content);
				fileWriter.close();
				JOptionPane.showMessageDialog(null,"邮件已保存在：".concat(System.getProperty("user.dir")),"提示消息",JOptionPane.WARNING_MESSAGE);
			} catch (Exception e2) {
				// TODO: handle exception
			}
		}
		else if(e.getActionCommand()=="退出"){
			dispose();
		}
	}

}
