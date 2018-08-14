package com.wrf.chatroom.server;

public class User {
	private String account;
	private String password;
	private String nickName;
	
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	
	public User(String account, String password, String nickName) {
		super();
		this.account = account;
		this.password = password;
		this.nickName = nickName;
	}
	public User() {
		super();
	}
	public User(String account, String password) {
		super();
		this.account = account;
		this.password = password;
	}
	
	@Override
	public String toString() {
		return "User [account=" + account + ", password=" + password + ", nickName=" + nickName + "]";
	}
	
}
