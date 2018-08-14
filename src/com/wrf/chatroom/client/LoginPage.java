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
		private JTextField account=new JTextField();//�˺ſ�
		private JPasswordField password=new JPasswordField();//�����
		private JLabel accountText=new JLabel();//�˺�label
		private JLabel passwordText=new JLabel();//����label
		private JButton loginBtn=new JButton();//��¼��ť
		private JButton exitBtn=new JButton();//�˳���ť
		private JSeparator line=  new JSeparator();//�ָ���
		private JLabel label=new JLabel();//ͼƬ��
		private ImageIcon image;//ͼƬ����
		 
		private BufferedReader in=null;//��ȡ���������
		private PrintStream out=null;//�����������

		public LoginPage() {
			super();
			setTitle("�����¼");
			getContentPane().setLayout(null);
			this.setLocation(450,230);
			//����ͼƬ ���ñ���
			String bgImage=System.getProperty("user.dir")+File.separator+
					"File"+File.separator+"login_bg.jpg";
			image=new ImageIcon(new ImageIcon(bgImage).getImage().getScaledInstance(333, 166, Image.SCALE_DEFAULT));
			label.setHorizontalAlignment(SwingConstants.CENTER);
			
			label.setIcon(image);
			label.setBounds(20, 0, 333, 88);
			getContentPane().add(label);
			
			accountText.setText("�˺�:");
			accountText.setBounds(20, 100, 61, 32);
			getContentPane().add(accountText);
			
			account.setBounds(65, 107, 131, 22); 
			getContentPane().add(account);
			
			passwordText.setText("����:");
			passwordText.setBounds(20, 138, 61, 18);
			getContentPane().add(passwordText);
			
			password.setBounds(65, 138, 131, 22);
			getContentPane().add(password);
			
			loginBtn.setText("��¼");
			loginBtn.setBounds(90,169,86,30);
			getContentPane().add(loginBtn);
			
			exitBtn.setText("�˳�");
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
		
		//������¼��ťУ����Ϣ �����͵�������
		class LoginListener implements ActionListener{

			@Override
			public void actionPerformed(ActionEvent e) {
				String accountVal=account.getText();
				String passwordVal=password.getText();
				
				if(e.getSource()==loginBtn) {
					if(accountVal.equals("")) {
						JOptionPane.showMessageDialog(null, "�˺Ų���Ϊ��!","����",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					if(passwordVal.equals("")) {
						JOptionPane.showMessageDialog(null, "���벻��Ϊ��!","����",JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					//��֤ͨ���������������
					Socket socket=null;
					try {
						socket=new Socket("127.0.0.1",2000);
						in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
						out=new PrintStream(socket.getOutputStream());
						
						out.println("0"+accountVal+";"+passwordVal);
						
						//�����߳�
						new Thread(new LoginHelper(socket)).start();
						
					} catch (IOException e1) {
						JOptionPane.showMessageDialog(null, "��������û��","��ʾ",JOptionPane.INFORMATION_MESSAGE);
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
									JOptionPane.showMessageDialog(null, "�û������������","����",JOptionPane.ERROR_MESSAGE);
									
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
