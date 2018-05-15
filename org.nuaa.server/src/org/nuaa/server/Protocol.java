package org.nuaa.server;


import java.math.*;

public class Protocol {
	
//		报头为学号、姓名、本次分片在整个文件中的位置
//		报尾为校验和：校验和s的计算：设要发送n字节，bi为第i个字，s=(b0+b1+…+bn) mod 256
	private int ID = 161510205;
	private String name = "丁悦";
	private int location;
	private byte[] data;
	private int len;
	private byte checksum = 0;

	Protocol(int loc,byte[] data,int len){
		location = loc;
		this.data = data;
		this.len = len;
	}
	
	public int getLen() {
		return len;
	}
	
	public int getID() {
		return ID;
	}
	public String getName() {
		return name;
	}
	public int getLocation() {
		return location;
	}
	public byte[] getData() {
		return data;
	}
	
	public byte[] intToByte(int intnum) {
		byte[] bytenum = new byte[4];
		bytenum[3] = (byte)((intnum>>24) & 0xFF);
		bytenum[2] = (byte)((intnum>>16) & 0xFF);
		bytenum[1] = (byte)((intnum>>8) & 0xFF);
		bytenum[0] = (byte)(intnum & 0xFF);
		return bytenum;
	}
	
	public byte[] getContentData() {
		
		byte checksum = (byte)getCheckSum();
		byte[] data = getData();
		for(int i = 0;i<getLen();i++) {
			checksum = (byte)((checksum+data[i])%256);
		}
		//int temp = Integer.parseInt(name, 2);
		byte[] name =  getName().getBytes();
		for(int i = 0;i<6;i++) {
			checksum = (byte)((checksum+name[i])%256);
		}
		byte[] ID =  intToByte(getID());
		for(int i = 0;i<4;i++) {
			checksum = (byte)((checksum+ID[i])%256);
		}
		byte location[] = intToByte(getLocation());
		for(int i = 0;i<4;i++) {
			checksum = (byte)((checksum+location[i])%256);
		}
		
		byte[] checksum2 = new byte[1];
		checksum2[0] = checksum;
		
		byte[] all = new byte[15+getLen()];
		System.arraycopy(ID, 0, all, 0, 4);
		System.arraycopy(name, 0, all, 4, name.length);
		System.arraycopy(location, 0, all, name.length+4, 4);
		System.arraycopy(data, 0, all, name.length+8, getLen());
		int a = name.length+data.length+2;
		System.arraycopy(checksum2, 0, all, name.length+getLen()+8, 1);
		return all;
		
	}
	public byte getCheckSum() {
		return checksum;
	}

}
