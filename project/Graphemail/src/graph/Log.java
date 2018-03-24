package graph;
import java.awt.*;  
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;
import javax.swing.*; 
import org.apache.commons.codec.binary.Base64;
public class Log extends JFrame implements ActionListener{
    //定义组件  
    JPanel jp1,jp2,jp3,jp4;//面板  
    JLabel jlb1,jlb2,jlb3;//标签  
    JButton jb1,jb2;//按钮  
    JTextField jtf;//文本  
    JPasswordField jpf;//密码  
    String line_end="\r\n";
    String fulluser,fullpass;
    
    public static void main(String[] args) {  
        Log log=new Log();  
    }  
      
    //构造函数  
    public Log(){  
        //创建面板  
        jp1=new JPanel();  
        jp2=new JPanel();  
        jp3=new JPanel();
        jp4=new JPanel();
        
        //创建标签  
        jlb1=new JLabel("用户名");  
        jlb2=new JLabel("密    码");  
        jlb3=new JLabel("本系统只支持网易邮箱和新浪邮箱");
        
        //创建按钮  
        jb1=new JButton("登录");  
        jb2=new JButton("重置"); 
        
        //创建文本框  
        jtf=new JTextField(10); 
        
        //创建密码框  
        jpf=new JPasswordField(10);
        
        //设置布局管理  
        this.setLayout(new GridLayout(4, 1));//网格式布局
        
        //加入各个组件  
        jp1.add(jlb1);  
        jp1.add(jtf); 
        
        jp2.add(jlb2);  
        jp2.add(jpf);  
          
        jp3.add(jb1);  
        jp3.add(jb2); 
        
        jp4.add(jlb3);
        //加入到JFrame  
        this.add(jp1);  
        this.add(jp2);  
        this.add(jp3);
        this.add(jp4);
        //设置窗体  
        this.setTitle("用户登录");//窗体标签  
        this.setSize(300, 200);//窗体大小  
        this.setLocationRelativeTo(null);//在屏幕中间显示(居中显示)  
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//退出关闭JFrame  
        this.setVisible(true);//显示窗体  
        //锁定窗体  
        this.setResizable(false);
        //设置监听事件
        jb1.addActionListener(this);
        jb2.addActionListener(this);
    }
	
    //清空文本框和密码框  
    public void clear()  
    {  
        jtf.setText("");  
        jpf.setText("");  
    } 
	
    public void login(String user,String password,String type) {
		Scanner sc=new Scanner(System.in);
		StringBuilder sb=new StringBuilder("\nServer info: \n------------\n");
    	try {
			Socket socket=new Socket("smtp.".concat(type).concat(".com"),25);
			
			InputStream is=socket.getInputStream();
			OutputStream os=socket.getOutputStream();
			BufferedReader br=new BufferedReader(new InputStreamReader(is));
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(os));
			
		    if(!Check.message(pw, br, sb, "220")){
		    	JOptionPane.showMessageDialog(null,"无法连接服务器","提示消息",JOptionPane.WARNING_MESSAGE); 
		    	clear();
		    }
		    
		    pw.write("helo wch".concat(line_end));//检查连接
			if(!Check.message(pw, br, sb, "250")){
				JOptionPane.showMessageDialog(null,"无法连接服务器","提示消息",JOptionPane.WARNING_MESSAGE); 
		    	clear();
		    }
			
			pw.write("auth login".concat(line_end));//请求登录
			if(!Check.message(pw, br, sb, "334")){
				JOptionPane.showMessageDialog(null,"无法登录","提示消息",JOptionPane.WARNING_MESSAGE); 
		    	clear();
		    }
			
			//将用户名和密码转换为base64编码
			byte[] usermid1=user.getBytes();
		    byte[] usermid2=Base64.encodeBase64(usermid1);
		    String user64=new String(usermid2);
		    byte[] passmid1=password.getBytes();
		    byte[] passmid2=Base64.encodeBase64(passmid1);
		    String password64=new String(passmid2);
		    
			pw.write(user64.concat(line_end));
			if(!Check.message(pw, br, sb, "334")){
				JOptionPane.showMessageDialog(null,"账号错误，请重新输入","提示消息",JOptionPane.WARNING_MESSAGE);
				clear();
		    }
			
			pw.write(password64.concat(line_end));
			if(!Check.message(pw, br, sb, "235")){
				JOptionPane.showMessageDialog(null,"密码错误，请重新输入","提示消息",JOptionPane.WARNING_MESSAGE);
				clear();
		    }
			else{
				JOptionPane.showMessageDialog(null,"登录成功","提示消息",JOptionPane.WARNING_MESSAGE);
				dispose();  
				Choose choice=new Choose(fulluser,fullpass,user64,password64,type);
			}
			pw.write("QUIT");
			pw.close();
			br.close();
			os.close();
			is.close();
			socket.close();
				
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand() =="登录" ){
				fulluser=jtf.getText();
				fullpass=jpf.getText();
				if(!fulluser.equals("")&&!fullpass.equals("")){
					String user;
					String test1;//邮箱类别
					StringBuilder test2=new StringBuilder("");
					StringTokenizer st=new StringTokenizer(fulluser,"@.");
					user=st.nextToken();
					while(st.hasMoreElements()){
						test1=st.nextToken();
						test2=test2.append(test1);
						if(test1.equals("163")){
							login(user,fullpass,test1);
						}
						else if(test1.equals("sina")){
							login(user,fullpass,test1);
						}
					}
					if(test2.indexOf("163")<0 && test2.indexOf("sina")<0){
						JOptionPane.showMessageDialog(null,"请输入完整邮箱名称","提示消息",JOptionPane.WARNING_MESSAGE);
						clear();
					}
				}
				else if(fulluser.equals("")){
					JOptionPane.showMessageDialog(null,"请输入邮箱账号","提示消息",JOptionPane.WARNING_MESSAGE);
					clear();
				}
				else if(fullpass.equals("")){
					JOptionPane.showMessageDialog(null,"请输入邮箱密码","提示消息",JOptionPane.WARNING_MESSAGE);
					clear();
				}
		}
		else if(e.getActionCommand()=="重置"){
			clear();
		}
	}
}  

