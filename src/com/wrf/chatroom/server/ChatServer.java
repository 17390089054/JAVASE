package com.wrf.chatroom.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private List<ClientConnection> connectionList = new ArrayList<ClientConnection>();

	public void startServer() {
		ServerSocket serverSocket = null;
		Socket socket;

		try {
			serverSocket = new ServerSocket(2000);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);
		}

		System.out.println("服务器启动成功！端口:" + 2000);
		// 不断创建socket对象以满足多个客户端的请求
		while (true) {
			try {
				socket = serverSocket.accept();
				handleClientConnection(socket);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	// 处理客户端请求
	private void handleClientConnection(Socket socket) {
		ClientConnection con = new ClientConnection(socket);
		connectionList.add(con);
		Thread thread = new Thread(con);
		thread.start();
	}

	public void sendToAllClients(String message) {
		
		for (ClientConnection con : connectionList) {
			con.sendMessageToClient(message);
		}
	}

	// 多线程处理多个客户连接
	class ClientConnection implements Runnable {
		 BufferedReader in = null;
		 PrintStream out = null;
		 Socket socket = null;

		// 读取客户端登录信息 解析 并返回
		public void run() {
			InputStream socketInput = null;
			OutputStream socketOutput = null;
			String account = null;
			String password = null;
			try {
				socketInput = socket.getInputStream();
				in = new BufferedReader(new InputStreamReader(socketInput));
				socketOutput = socket.getOutputStream();
				out = new PrintStream(socketOutput);

				String input = in.readLine();
				while(true) {
				// 0代表登录信息 1代表聊天信息
				if (input != null && input.startsWith("0")) {
					// 切割信息 分理出账号密码
					String[] temp = input.split(";");
					account = temp[0].substring(1);
					password = temp[1];
					// 调用UserDao 验证登录信息
					UserDao dao = new UserDao();
					Boolean flag = dao.login(account, password);
					System.out.println("flag:" + flag);
					// 返回用户信息
					out.println(new Boolean(flag).toString() + "#"+dao.getUser().getNickName());
					out.flush();
				}

				if (input != null && input.startsWith("1")) {
					sendToAllClients(input);
				}

				input = in.readLine();
				}

			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (in != null) {
						in.close();
					}
					if (out != null) {
						out.close();
					}
				} catch (IOException e) {

					e.printStackTrace();
				}

			}

		}

		public void sendMessageToClient(String message) {
			try {
				out.println(message.substring(1));
				out.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public ClientConnection(Socket s) {
			socket = s;
		}

	}

	public static void main(String[] args) {
		new ChatServer().startServer();
	}

}
