package org.nuaa.client;

import org.nuaa.client.Protocol;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextArea;

/**
 * 基于TCP协议的Socket通信，实现文件传输-客户端
 */
public class Client {
	
	static JTextArea jta;
	private static BufferedOutputStream bufferedOutputStream;
	public Client(JTextArea jta) {
		this.jta = jta;
	}
	public static boolean CheckSum(byte[] message,int len) {
		byte check = 0;
		for(int i = 0;i<len-1;i++) {
			check = (byte)((check+message[i])%256);
		}
		if(check == message[len-1]) {
			return true;
		}
		return false;
	}
	public static byte[] getData(byte[] message,int len) {
		byte[] data = new byte[len];
		 System.arraycopy(message, 14, data, 0, len);
		return data;
	}
	public static String ChooseFile(int num) {
		switch(num) {
		case 1:
			return "/Users/Ivys/Desktop/upload/upload1.txt";
		case 2:
			return "/Users/Ivys/Desktop/upload/upload2.txt";
		case 3:
			return "/Users/Ivys/Desktop/upload/upload3.txt";
		default:
			break;
		}
		return "";
	}
	public static void Download(int choice,int times) {
		try {
			OutputStream os = null;
			DataOutputStream dos = null;
			PrintWriter pw = null;
			Socket socket = new Socket("127.0.0.1",9999);
			
			// 先发送信号，是下载文件还是上传文件
            dos =  new DataOutputStream(socket.getOutputStream());
            dos.writeInt(1);
            
			InputStream is = socket.getInputStream();
			BufferedInputStream bufferedInputStream = new BufferedInputStream(is);
			
			dos = new DataOutputStream(socket.getOutputStream());
			dos.write(times);
			dos.write(choice);
		
			bufferedOutputStream = new BufferedOutputStream(new FileOutputStream("/Users/Ivys/Desktop/download.txt"));
			byte[] buf = new byte[271];
			int len = 0;
			int num = 0;
			
			while(num<times && (len=bufferedInputStream.read(buf))!=-1){
				if(CheckSum(buf,len)) {
					byte[] data = getData(buf,len-15);
					bufferedOutputStream.write(data,0,data.length);//写入文件
					jta.append("\n已完成第"+(num+1)+"片的下载");
					bufferedOutputStream.flush();
					dos.writeInt(0);// 没出错
					num++;
				}
				else {
					jta.append("\n第"+(num+1)+"片下载失败，进行重传");
					dos.writeInt(1);
				}		                
                
            }
			
			bufferedOutputStream.flush();
			socket.shutdownInput();
			os = socket.getOutputStream();
			pw = new PrintWriter(os);
			pw.println("文件已保存至客户端/Users/Ivys/Desktop/download.txt");
			jta.append("\n文件已下载至客户端"+"/Users/Ivys/Desktop/download.txt"
					+ "\n--------------------------------------------------\n");
			pw.flush();
			socket.shutdownOutput();
			
			if(pw != null) {
				pw.close();
			}
			if(os != null) {
				os.close();
			}
			socket.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void Uploading(int file,int times) {
		try {
			
	        // 1.创建客户端的Socket，指定服务器的IP和端口
	        Socket socket = new Socket("127.0.0.1", 9999);
	        
	        // 1.2 先发送信号，是下载文件还是上传文件
            DataOutputStream dos =  new DataOutputStream(socket.getOutputStream());
            dos.writeInt(0);
//            dos.close();
	        // 2.获取该Socket的输出流，用来向服务器发送文件
	        OutputStream os = socket.getOutputStream();
	        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
	        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(os);// 将BufferedOutputStream与套接字的输出流进行连接      
	        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(ChooseFile(file)));// 读取客户机文件
	        byte[] buf = new byte[256];
	        int len = 0,num = 0,check = 3;
	        boolean flag = true;
	        InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
//			InputStream iss = socket_check.getInputStream();
//			BufferedReader brr = new BufferedReader(new InputStreamReader(is));
			
			while(true) {
				if(flag){ //没出错时
					len = bufferedInputStream.read(buf);
					if(len == -1 || num>=times)
						break;
				}
			
				Protocol mess = new Protocol(num,buf,len);
				byte[] message = mess.getContentData();
				bufferedOutputStream.write(message,0,message.length);		
				bufferedOutputStream.flush();// 刷新缓冲流
				
				if((check = dis.readInt()) == 1) {//出错
					flag = false;
					jta.append("\n第"+(num+1)+"片上传失败，进行重传");
				}
				else {
					num++;
					flag = true;
					jta.append("\n已完成第"+num+"片的上传");
				}
			}
	        bufferedOutputStream.flush();// 刷新缓冲流
	        socket.shutdownOutput();// 禁用此套接字的输出流
	        
	        // 3.获取输入流，取得服务器的信息
	         is = socket.getInputStream();
	         br = new BufferedReader(new InputStreamReader(is));
	        String info = null;
	        while ((info = br.readLine()) != null) {
	        	 jta.append("\n成功上传至服务器:"+info+"\n----------------------------------\n");
	         System.out.println("服务器端的信息：" + info);
	        }
	        socket.shutdownInput();// 禁用此套接字的输出流
	        // 4.关闭资源
	        os.close();
	        bufferedInputStream.close();
	        bufferedOutputStream.close();
	        is.close();
	        br.close();
	        socket.close();
	    } catch (UnknownHostException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}