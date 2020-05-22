package com.wrf.chatroom.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserDao {
	
	private static List<User>userList=new ArrayList<User>();
	private User user=new User();
	static {
		String fileName=System.getProperty("user.dir")+File.separator+
				"File"+File.separator+"userstable.txt";
		BufferedReader br=null;
		try {
			br=new BufferedReader(new FileReader(new File(fileName)));
			String line=null;
			while((line=br.readLine())!=null) {
				String []temp=line.split("#");
				userList.add(new User(temp[0],temp[1],temp[2]));
			}
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public Boolean login(String account, String password) {
		boolean flag=false;
		for(User u:userList) {
			if(u.getAccount().equals(account)&&u.getPassword().equals(password)) {
				flag=true;
				user=u;
				return flag;
			}
		}
	
		return flag;
	}
	
	public User getUser() {
		return this.user;
	}
	
	public static void main(String[] args) {
		UserDao dao=new UserDao();
		System.out.println(dao.login("123456", "lb123"));
		System.out.println(dao.login("23456789", "gy456"));
		System.out.println(dao.login("2222222", "zf789"));
		
	}
	
	

}
