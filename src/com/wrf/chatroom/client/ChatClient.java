package com.wrf.chatroom.client;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient {
	private JTextArea output;
	private JTextField input;
	private JButton sendButton;
	private JButton quitButton;
	private String nickName;
	
	private BufferedReader in;
	private PrintStream out;
	
	private Socket socket;
	
	public ChatClient(Socket socket,String nickName) {
		output= new JTextArea(10,50);
		input=new JTextField(50);
		sendButton=new JButton("����");
		quitButton=new JButton("�˳�");
		this.nickName=nickName;
		this.socket=socket;
	}
	
	public void launchFrame() {
		//�����������
		JFrame frame=new JFrame("��ӭ��!"+nickName);
		Container container=frame.getContentPane();
		container.setLayout(new BorderLayout());
		
		container.add(new JScrollPane(output),BorderLayout.CENTER);
		container.add(input,BorderLayout.SOUTH);
		
		JPanel panel=new JPanel();
		panel.add(sendButton);
		panel.add(quitButton);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		container.add(panel,BorderLayout.EAST);
		
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		
		frame.setSize(510,210);
		frame.setLocation(450, 230);
		frame.setVisible(true); 
		//�������Ͱ�ť
		
		sendButton.addActionListener(new SendHandler());
		input.addActionListener(new SendHandler());
		
	}
	
	
	
	//������Ϣ��������
	 private void sendMessageToServer() {
		 String text=input.getText();
		 out.println("1"+nickName+":"+text);
		 input.setText("");
	 }
	
	 //��Ϣ������
	 private class SendHandler implements ActionListener{
		@Override
		 public void actionPerformed(ActionEvent e) {
				sendMessageToServer();
		}
		 
	 }
	 
	 //���ӷ�����
	 public void doConnect(Socket socket) {
		 try {
			 in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out=new PrintStream(socket.getOutputStream());
			new Thread(new MessageReader()).start();
		} catch (IOException e) {
			System.out.println("�޷����ӵ�������!");
			e.printStackTrace();
		}
	 }
	 
	 private class MessageReader implements Runnable{
		 private boolean keepListening=true;
		 
		@Override
		public void run() {
			while(keepListening==true) {
				try {
					String nextLine=in.readLine();
					output.append(nextLine+"\n");
				} catch (IOException e) {
					keepListening=false;
					e.printStackTrace();
				}
			}
		}
		
	 }
	 
	 
	 
	 
	
	
	
	
	
}
