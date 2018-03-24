package graph;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
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

public class Recv extends JFrame implements ActionListener{
	
	static String user,password;
	static String user64;
	static String password64;
	static String mailtype;
	StringBuilder sb=new StringBuilder();
	String line_end="\r\n";
	JPanel jp1,jp2,jp3,jp4;
	JLabel jl1,jl2;
	JTextField jtf;
	JTextArea jta;
	JButton jb1,jb2;
	
	public Recv(String tuser,String tpass,String tuser64,String tpassword64,String type) {
		user=tuser;
		password=tpass;
		user64=tuser64;
		password64=tpassword64;
		mailtype=type;
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp4=new JPanel();
		
		jl1=new JLabel("邮件列表：");
		jl2=new JLabel("选择要读取的邮件序号：");
		
		jtf=new JTextField(10);
		
		jta=new JTextArea(6,26);
		
		jb1=new JButton("确定");
		jb2=new JButton("退出");
		
		jp1.add(jl1);
		jp1.add(jta);
		jp1.setLayout(null);
		jl1.setBounds(0, 0, 80, 20);
		jta.setBounds(5, 30, 280, 150);
		//jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		JScrollPane jsp=new JScrollPane(jta);
		jsp.setBounds(5,30,280,150);
		jp1.add(jsp);
		
		jp2.add(jl2);
		jp2.add(jtf);
		jp2.add(jb1);
		jp2.add(jb2);
		
		this.add(jp1);
		this.add(jp2);

		this.setLayout(new GridLayout(2,1));
		
		this.setTitle("读取邮件");
		this.setSize(300,400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		
		//显示邮件列表
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
			pw.write("list".concat(line_end));
			if(!Check.message(pw, br, sb, "+OK")){
				JOptionPane.showMessageDialog(null,"连接错误","提示消息",JOptionPane.WARNING_MESSAGE);
				dispose();
			}
			StringBuilder sb=new StringBuilder();//存储邮件列表
			String str;
			while ((str=br.readLine())!=null) {
				sb=sb.append(str);
				sb=sb.append(line_end);
				if(str.indexOf(".")!=-1)
					break;
			}
			String sb1=new String(sb);
			jta.setText(sb1);
			pw.write("QUIT");
			pw.close();
			br.close();
			socket.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
    public void clear()  
    {  
        jtf.setText("");
    } 
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="确定"){
			String num=jtf.getText();
			boolean bl=true;
			boolean bc=true;
			if(num.equals("")){
				JOptionPane.showMessageDialog(null,"请选择序号","提示消息",JOptionPane.WARNING_MESSAGE);
				bl=false;
			}
			else{
			    for (int i = num.length()-1;i>=0;i--){    
				    if (!Character.isDigit(num.charAt(i))){  
					    bl=false;
					    bc=false;
				    }  
			    }
			    if(bc==false){
			    	JOptionPane.showMessageDialog(null,"请输入合法序号","提示消息",JOptionPane.WARNING_MESSAGE);
			    	clear();
			    }
			}
			if(bl==true){
				Content content=new Content(num, user, password, user64, password64, mailtype);
			}	
		}
		else if(e.getActionCommand()=="退出")
			dispose();
	}

}
