package org.nuaa.client;
/* 界面 */
import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import javax.swing.JFrame;
import org.nuaa.client.*;
public class ClientInter extends JFrame implements ActionListener {
	JTextArea jta, jta1, jta2;
	JTextField jtf_file,jtf_times;
	JButton jb_down,jb_up;
	JPanel jp;
	Client client;
	public  ClientInter() {
	    setTitle("客户端");
        jta=new JTextArea();
        
        jtf_file=new JTextField("文件名",15);//输入文件窗口栏
        jtf_times=new JTextField("片数",3);//输入分片数栏
        
        jb_down=new JButton("确认下载");
        jb_down.addActionListener(this);
        jb_up=new JButton("确认上传");
        jb_up.addActionListener(this);
        
        jp=new JPanel();
        jp.add(jtf_file);
        jp.add(jtf_times);
        jp.add(jb_down);
        jp.add(jb_up);
        
        
        this.add(jta,"Center");
        jta.setText("	提供下载	|	提供上传  \n"
        			  +"	download1.txt	|	upload1.txt \n"
        			  +"	download2.txt	|	upload2.txt \n"
        			  +"	download3.txt	|	upload3.txt");
        
        this.add(jp,"South");
        this.setSize(500, 300);
        this.setVisible(true);
//        jtf_file.setBounds(300, 300, 100, 100);
//        jtf_times.setBounds(400, 300, 100, 100);
//        jb_up.setBounds(500, 300, 100, 100);
//        jb_down.setBounds(500, 300, 100, 100);
        
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
       
        client = new Client(jta);
//        client.start();
        
        /**窗体关闭按钮事件*/
        this.addWindowListener(new WindowAdapter()
        {
            public void windowClosing(WindowEvent e)
            {
                if(JOptionPane.showConfirmDialog(null, "<html>确定退出吗？","系统提示",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE)==0)
                {        
                    System.exit(0);
//                    client.closeSocket();
                }
                else
                {
                    return;
                }
            }
        });
}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		// 假如点击了按钮jb_down
		if (arg0.getSource() == jb_down) {
			String file1 = "download1";
			String file2 = "download2";
			String file3 = "download3";
			boolean check1 = jtf_file.getText().trim().equals(file1);
			boolean check2 = jtf_file.getText().trim().equals(file2);
			boolean check3 = jtf_file.getText().trim().equals(file3);
			char[] timesnum = jtf_times.getText().trim().toCharArray();
			int choice = 0;
			int times_num = timesnum[0] - '0';
			if(check1) {
				choice = 1;
			}
			else if(check2) {
				choice = 2;
			}
			else {
				choice = 3;
			}
			client.Download(choice,times_num);
			// ctsT.sendData(buffer);
		}
		else if(arg0.getSource() == jb_up) {
			String file1 = "upload1";
			String file2 = "upload2";
			String file3 = "upload3";
			boolean check1 = jtf_file.getText().trim().equals(file1);
			boolean check2 = jtf_file.getText().trim().equals(file2);
			boolean check3 = jtf_file.getText().trim().equals(file3);
			char[] timesnum = jtf_times.getText().trim().toCharArray();
			int choice = 0;
			int times_num = timesnum[0] - '0';
			if(check1) {
				choice = 1;
			}
			else if(check2) {
				choice = 2;
			}
			else {
				choice = 3;
			}
			client.Uploading(choice,times_num);
		}
			

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ClientInter();
	}

}
