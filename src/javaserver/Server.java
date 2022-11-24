package javaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class Server {

	private static final int PORT = 8080;
	private static final int LINGER_TIME = 5000;
	private static final int SO_TIMEOUT = 10000;
	public static final String FILE_PATH =".\\web_src_2\\";

	protected static Hashtable<String, String> account;

	public void serve() throws IOException {
		ServerSocket ss = new ServerSocket(PORT);

		while (true) {
			Socket socket = ss.accept();
			
			socket.setSoLinger(true, LINGER_TIME);
			socket.setSoTimeout(SO_TIMEOUT);
			
			Thread soc_thread = new Thread(new Handler(socket));
			soc_thread.setPriority(Thread.MAX_PRIORITY);
			soc_thread.start();
		}
	}

	public static void main(String[] args) {
		Server.account = new Hashtable<>();
		account.put("admin", "123456");

		Server server = new Server();
		try {
			server.serve();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
