package com.wrf.chatroom.client;

import java.awt.Color;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class LoginPage extends JFrame{
		private JTextField account=new JTextField();//账号框
		private JPasswordField password=new JPasswordField();//密码框
		private JLabel accountText=new JLabel();//账号label
		private JLabel passwordText=new JLabel();//密码label
		private JButton loginBtn=new JButton();//登录按钮
		private JButton exitBtn=new JButton();//退出按钮
		private JSeparator line=  new JSeparator();//分割线
		private JLabel label=new JLabel();//图片域
		private ImageIcon image;//图片对象
		 
		private BufferedReader in=null;//读取输服务器入
		private PrintStream out=null;//输出到服务器

		public LoginPage() {
			super();
			setTitle("聊天登录");
			getContentPane().setLayout(null);
			this.setLocation(450,230);
			//加载图片 设置背景
			String bgImage=System.getProperty("user.dir")+File.separator+
					"File"+File.separator+"login_bg.jpg";
			image=new ImageIcon(new ImageIcon(bgImage).getImage().getScaledInstance(333, 166, Image.SCALE_DEFAULT));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			
			label.setIcon(image);
			label.setBounds(20, 0, 333, 88);
			getContentPane().add(label);
			
			accountText.setText("账号:");
			accountText.setBounds(20, 100, 61, 32);
			getContentPane().add(accountText);
			
			account.setBounds(65, 107, 131, 22); 
			getContentPane().add(account);
			
			passwordText.setText("密码:");
			passwordText.setBounds(20, 138, 61, 18);
			getContentPane().add(passwordText);
			
			password.setBounds(65, 138, 131, 22);
			getContentPane().add(password);
			
			loginBtn.setText("登录");
			loginBtn.setBounds(90,169,86,30);
			getContentPane().add(loginBtn);
			
			exitBtn.setText("退出");
			exitBtn.setBounds(209, 169, 86, 23);
			getContentPane().add(exitBtn);
			
			line.setBackground(Color.LIGHT_GRAY);
			line.setBounds(20, 94, 334, 11);
			getContentPane().add(line);
			
			setSize(377,241);
			setResizable(false);
			setVisible(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			exitBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
						dispose();
				}
			});
			
			loginBtn.addActionListener(new LoginListener());
		}
		
		//监听登录按钮校验信息 并发送到服务器
		class LoginListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				String accountVal=account.getText();
				String passwordVal=password.getText();
				
				if(e.getSource()==loginBtn) {
					if(accountVal.equals("")) {
						JOptionPane.showMessageDialog(null, "账号不能为空!","警告",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if(passwordVal.equals("")) {
						JOptionPane.showMessageDialog(null, "密码不能为空!","警告",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					//验证通过后输出到服务器
					Socket socket=null;
					try {
						socket=new Socket("127.0.0.1",2000);
						in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out=new PrintStream(socket.getOutputStream());
						
						out.println("0"+accountVal+";"+passwordVal);
						
						//启动线程
						new Thread(new LoginHelper(socket)).start();
						
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "服务器还没打开","提示",JOptionPane.INFORMATION_MESSAGE);
					}
				}
				
				if(e.getSource()==exitBtn) {
					dispose();
				}
				
			}
			
		}
		
		public class LoginHelper implements Runnable{
			private Socket socket;
			String accoutVal=account.getText();
			String passwordVal=password.getText();
			
			public LoginHelper(Socket socket) {
				this.socket=socket;
			}
			
			@SuppressWarnings("deprecation")
			public void run() {
				System.out.println("login run");
				
				try {
					String login=in.readLine();
					System.out.println("login:"+login);
					while(login!=null) {
						String []temp=login.split("#");
								if(temp[0].equals("true")) {
									ChatClient client=new ChatClient(socket,temp[1]);
									client.launchFrame();
									client.doConnect(socket);
									dispose();
									Thread.currentThread().stop();
								}else {
									JOptionPane.showMessageDialog(null, "用户名或密码错误","警告",JOptionPane.ERROR_MESSAGE);
									
								}
								
						login=in.readLine();
						
					}
					
					
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				
				
			}
			
			
		}
		
		
		
		
		
		
		
		 
		public static void main(String[] args) {
			new LoginPage();
		}
		
		
		
}
