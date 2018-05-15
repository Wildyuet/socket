package org.nuaa.server;

/* 实现客户端上传文件至服务器*/

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Uploading implements Runnable{

    Socket socket = null;// 和本线程相关的Socket
//    Socket socket2 = null;
    public Uploading(Socket socket) {
        super();
        this.socket = socket;
//        this.socket2 = socket2;
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
    // 响应客户端的请求
    @Override
    public void run() {
        // TODO Auto-generated method stub
        OutputStream os = null;
        PrintWriter pw = null;
		DataOutputStream dos = null;

        try {
            InputStream is=socket.getInputStream();
            // 要完成客户端文件上传到服务器的功能需要将客户机的文件通过FileInputStream进行读取，并包装成BufferedInputStream，
            //将套接字的输出流包装成BufferedOutputStream，用BufferedInputStream中的read（）方法读取文件中的数据，
            //并用 BufferedOutputStream中的write（）方法进行写入，这样文件就送入了Socket的输出流；
            BufferedInputStream bufferedInputStream = new BufferedInputStream(is);// 将BufferedInputStream与套接字的输入流进行连接
            BufferedOutputStream bufferedOutputStream=new BufferedOutputStream(new FileOutputStream("/Users/Ivys/Desktop/tt.txt"));
            byte[] buf=new byte[271];
            int len=0;
            
            dos =  new DataOutputStream(socket.getOutputStream());
            while((len=bufferedInputStream.read(buf))!=-1){
            		if(CheckSum(buf,len)) {
					byte[] data = getData(buf,len-15);
					bufferedOutputStream.write(data,0,data.length);//写入文件
//					dos = new DataOutputStream(socket.getOutputStream()); 
					bufferedOutputStream.flush();// 刷新缓冲流
					dos.writeInt(0);
				}
				else {
//					dos = new DataOutputStream(socket.getOutputStream());
//					int temp = 1;
					dos.writeInt(1);
				}		                
                
            }
            //强行写入输出流，因为有些带缓冲区的输出流要缓冲区满的时候才输出
            bufferedOutputStream.flush();// 刷新缓冲流
            socket.shutdownInput();// 关闭输入流
            os=socket.getOutputStream();
            pw=new PrintWriter(os);
            pw.println("文件已保存至服务器的/Users/Ivys/Desktop/tt.txt ");
            pw.flush();
            socket.shutdownOutput();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }finally{
            // 关闭相关资源
            try {
            if (pw != null) {
                pw.close();
            }
            if (os != null) {
                os.close();
            }
            if (socket != null) {
                socket.close();
            }
            } catch (IOException e) {
            e.printStackTrace();
            }
        }
        
    }

}