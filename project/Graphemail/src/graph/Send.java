package graph;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import javax.swing.Spring;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileSystemView;

public class Send extends JFrame implements ActionListener{

	JPanel jp1,jp2,jp3,jp4,jp5;
	JLabel jlb1,jlb2,jlb3;
	JButton jb1,jb2,jb3;
	JTextField jtf1,jtf2,jtf3,jtf4;
	JTextArea jta;
	static String user;
	static String user64;
	static String password64;
	static String mailtype;
	String line_end="\r\n";
	
//	public static void main(String[] args){
//		Send send= new Send(user, user64, password64, mailtype);
//	}

	//构造函数
	public Send(String tuser,String tuser64,String tpassword64,String type) {
		user=tuser;
		user64=tuser64;
		password64=tpassword64;
		mailtype=type;
		
		jp1=new JPanel();
		jp2=new JPanel();
		jp3=new JPanel();
		jp4=new JPanel();
		jp5=new JPanel();
		
		jlb1=new JLabel("收件人:");
		jlb2=new JLabel("主题:  ");
		jlb3=new JLabel("正文:");
		
		jb1=new JButton("发送");
		jb2=new JButton("退出");
		jb3=new JButton("选择附件");
		jb1.setBounds(1, 1, 2, 2);
		
		jtf1=new JTextField(10);
		jtf2=new JTextField(10);
		jtf4=new JTextField(10);
		
		jta=new JTextArea(5,26);
		
		jp1.setLayout(null);
		jlb1.setBounds(5, 5, 60, 40);
		jtf1.setBounds(60, 17, 180, 20);//x,y,宽度，高度
		jp1.add(jlb1);
		jp1.add(jtf1);
		
		//jp2.setLayout(null);
		jlb2.setBounds(5, 30, 60, 40);
		jtf2.setBounds(60, 42, 180, 20);
		jp1.add(jlb2);
		jp1.add(jtf2);

		//jp3.setLayout(null);
		jlb3.setBounds(5, 55, 60, 40);
		jta.setBounds(5, 90, 285, 180);
		jp1.add(jlb3);
		jp1.add(jta);
		
		//jp4.setLayout(null);
		jb1.setBounds(60, 320, 60, 40);
		jb2.setBounds(160,320,60,40);
		jb3.setBounds(5, 280, 60, 30);
		jtf4.setBounds(70,280,200,30);
		Font font=new Font("", Font.BOLD, 12);
		jb3.setFont(font);
		jb3.setBorder(BorderFactory.createEtchedBorder());
		jp1.add(jb1);
		jp1.add(jb2);
		jp1.add(jb3);
		jp1.add(jtf4);
		
		this.add(jp1);
		//this.add(jp2);
		//this.add(jp3);
		//this.add(jp4);
		//this.setLayout(new GridLayout(4,1));
		
		this.setTitle("发送邮件");
		this.setSize(300,400);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		this.setResizable(false);
		
		jb1.addActionListener(this);
		jb2.addActionListener(this);
		jb3.addActionListener(this);
		
	}
	
    public void clear()  
    {  
        jtf1.setText("");  
        jtf2.setText(""); 
        jta.setText("");
    } 
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand()=="发送"){
				StringBuilder sb=new StringBuilder();
				try {
					Socket socket=new Socket("smtp.".concat(mailtype).concat(".com"),25);
					
					InputStream is=socket.getInputStream();
					OutputStream os=socket.getOutputStream();
					BufferedReader br=new BufferedReader(new InputStreamReader(is));
					PrintWriter pw=new PrintWriter(new OutputStreamWriter(os));
					
				    if(!Check.message(pw, br, sb, "220"))
				    	JOptionPane.showMessageDialog(null,"连接错误","提示消息",JOptionPane.WARNING_MESSAGE); 
				    
				    pw.write("helo wch".concat(line_end));//检查连接
					if(!Check.message(pw, br, sb, "250"))
						JOptionPane.showMessageDialog(null,"连接错误","提示消息",JOptionPane.WARNING_MESSAGE); 
					
					pw.write("auth login".concat(line_end));//请求登录
					if(!Check.message(pw, br, sb, "334"))
						JOptionPane.showMessageDialog(null,"连接错误","提示消息",JOptionPane.WARNING_MESSAGE);
					
					pw.write(user64.concat(line_end));
					if(!Check.message(pw, br, sb, "334"))
						JOptionPane.showMessageDialog(null,"连接错误，请重新输入","提示消息",JOptionPane.WARNING_MESSAGE);
					
					pw.write(password64.concat(line_end));
					if(!Check.message(pw, br, sb, "235"))
						JOptionPane.showMessageDialog(null,"连接错误，请重新输入","提示消息",JOptionPane.WARNING_MESSAGE);
				
					pw.write("mail from:<".concat(user).concat(">").concat(line_end));
					if(!Check.message(pw, br, sb, "250"))
						JOptionPane.showMessageDialog(null,"设置发送邮箱失败，请重新输入","提示消息",JOptionPane.WARNING_MESSAGE);
					
					String rcmail=jtf1.getText();
					pw.write("rcpt to:<".concat(rcmail).concat(">").concat(line_end));
					if(!Check.message(pw, br, sb, "250")){
						JOptionPane.showMessageDialog(null,"接收邮箱错误，请重新输入","提示消息",JOptionPane.WARNING_MESSAGE);
						clear();
					}
					
					//发邮件
					pw.write("data".concat(line_end));
					if(!Check.message(pw, br, sb, "354")){
						JOptionPane.showMessageDialog(null,"无法输入邮件内容","提示消息",JOptionPane.WARNING_MESSAGE);
						clear();
				    }
					pw.write("from:<".concat(user).concat(">").concat(line_end));
					pw.write("to:<".concat(rcmail).concat(">").concat(line_end));
					String subject=jtf2.getText();
					if(subject==""){
						JOptionPane.showMessageDialog(null,"主题不能为空，请重新输入","提示消息",JOptionPane.WARNING_MESSAGE);
						clear();
					}
					pw.write("subject:".concat(subject).concat(line_end));
					pw.write(line_end);
					String context=jta.getText();
					System.out.println(context);
					pw.write(context.concat(line_end));
					pw.write(line_end);
					pw.write(".".concat(line_end));
					if(!Check.message(pw, br, sb, "250")){
						JOptionPane.showMessageDialog(null,"发送失败","提示消息",JOptionPane.WARNING_MESSAGE);
						pw.write("QUIT");
						clear();
				    }
					else{
						JOptionPane.showMessageDialog(null,"发送成功","提示消息",JOptionPane.WARNING_MESSAGE);
						pw.write("QUIT");
						clear();
					}
					pw.close();
					br.close();
					os.close();
					is.close();
					socket.close();
					
				} catch (Exception e2) {
					// TODO: handle exception
				}
		}
		else if(e.getActionCommand()=="退出"){
			dispose(); 
		}
		else if(e.getActionCommand()=="选择附件"){
			//打开对话框
			int result=0;
			File file=null;
			String path=null;
			JFileChooser fileChooser=new JFileChooser();
			FileSystemView fsv=FileSystemView.getFileSystemView();
			fileChooser.setCurrentDirectory(fsv.getHomeDirectory());
			fileChooser.setDialogTitle("请选择要上传的文件...");
			fileChooser.setApproveButtonText("确定");//用“确定”取代“打开文件”
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			result=fileChooser.showOpenDialog(fileChooser);//打开对话框
			if(JFileChooser.APPROVE_OPTION==result){
				path=fileChooser.getSelectedFile().getPath();
				file=fileChooser.getSelectedFile();
			}
			//创建文件夹保存文件
			Date now=new Date();
			Long longnow=now.getTime();
			DateFormat dFormat=new SimpleDateFormat("yyyy-MM-dd");
			String strToday=dFormat.format(longnow);
			String relFolderPath="upload"+File.separator+strToday;//文件夹相对路径
			File directory=new File("");
			String basepath;
			try {
				basepath = directory.getCanonicalPath();
				String fullFolderPath=basepath+File.separator+relFolderPath;//全路径
				//System.out.println(fullFolderPath);
				//根据全路径判断，如果不存在，创建文件夹
				File outfolder=new File(fullFolderPath);
				if(!outfolder.exists()){
					outfolder.mkdirs(); 
				}
				//创建文件
				String filename=file.getName();
				//System.out.println(filename);
				String fullFilePath=fullFolderPath+File.separator+longnow+filename;//要存入文件的全路径
				//System.out.println(fullFilePath);
				String fullUrlPath="upload/"+strToday+"/"+longnow+filename;//相对路径
				//System.out.println(fullUrlPath);
				File outFile=new File(fullFilePath);
			    if(!outFile.exists()){
			    	outFile.createNewFile();
			    }
			    //根据空文件创建字符流的目的地
			    BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(outFile));
			    //根据上传的文件创建缓冲区
			    byte[] buffer=new byte[(int)file.length()+1];
			    int len=0;
			    FileInputStream inStream=new FileInputStream(file);
			    ByteArrayOutputStream outStream=new ByteArrayOutputStream();
			    while((len=inStream.read(buffer))!=-1){
			    	outStream.write(buffer,0,len);
			    }
			    
			    bos.write(buffer);//写入目的地输出流
			    JOptionPane.showMessageDialog(null,"上传成功","提示消息",JOptionPane.WARNING_MESSAGE);
			    jtf4.setText(filename);
			    outStream.close();
			    inStream.close();
			    bos.close();
			    
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

}
