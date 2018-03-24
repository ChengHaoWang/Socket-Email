package graph;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Check {
	static String line_end="\r\n";
    //信息检测
	public static boolean message(PrintWriter pw,BufferedReader br,StringBuilder sb,String msgCode) throws IOException{
		pw.flush();
		String message=br.readLine();
		sb.append("SERVER:");
		sb.append(message).append(line_end);
		if(message==null||message.indexOf(msgCode)<0){
			System.out.println("ERROR:"+message);
			pw.write("QUIT".concat(line_end));
			pw.flush();
			return false;
		}
		else{
			System.out.println(sb);
		}
		return true;
	}
}
