package org.nuaa.server;

/* 实现客户端从服务器上下载文件*/
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.nuaa.server.Protocol;

public class Download implements Runnable{

	Socket socket = null;
	public Download(Socket socket) {
		super();
		this.socket = socket;
	}
	public boolean CheckSum(byte[] message) {
		byte check = 0;
		for(int i = 0;i<message.length-1;i++) {
			check = (byte)((check+message[i])%256);
		}
		if(check == message[message.length-1]) {
			return true;
		}
		return false;
	}
	public int Sum(char a[]) {
		int sum = 0;
		for(int i = 0;i<a.length;i++)
			sum += a[i];
		return sum-96;
	}
	public String ChooseFile(int num) {
		switch(num) {
		case 1:
			return "/Users/Ivys/Desktop/download/download1.txt";
		case 2:
			return "/Users/Ivys/Desktop/download/download2.txt";
		case 3:
			return "/Users/Ivys/Desktop/download/download3.txt";
		default:
			break;
		}
		return "";
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			
			InputStream is = socket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			int times = br.read();
			int choice = br.read();
			System.out.println("times="+times);
			System.out.println("choice="+choice);

			OutputStream os = socket.getOutputStream();
			DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			BufferedOutputStream bufferedOutputStream  = new BufferedOutputStream(os);
			BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(ChooseFile(choice)));
			byte[] buf = new byte[256];
			int len = 0;

			int i = times;
			DataOutputStream dos = null;
			int num = 0;
			boolean flag = true;
			while(i > 0) {
				if(flag){ //没出错时
					len = bufferedInputStream.read(buf);
					if(len == -1)
						break;
				}
				Protocol mess = new Protocol(num,buf,len);
				byte[] message = mess.getContentData();
				bufferedOutputStream.write(message,0,message.length);		

				bufferedOutputStream.flush();
				if(dis.readInt() == 1) {//出错
					flag = false;					
				}
				else {
					flag = true;
					i--;
				}
			}
			
			bufferedOutputStream.flush();
			socket.shutdownOutput();
			
			is = socket.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			String info = null;
			while((info = br.readLine()) != null) {
				System.out.println("客户端信息:"+info);
			}
			/*
			String a = "abc";
			byte[] b = a.getBytes();
			int c = b.length;
			System.out.println("bb:"+c);
			*/
			socket.shutdownInput();
			
			os.close();
			bufferedInputStream.close();
			bufferedOutputStream.close();
			is.close();
			br.close();
			dis.close();
			socket.close();
		}catch(UnknownHostException e) {
			e.printStackTrace();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}

}
