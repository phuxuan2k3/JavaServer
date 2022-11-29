package javaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

public class Server implements Runnable {

	private static final int PORT = 8080;
	private static final int LINGER_TIME = 5000;
//	private static final int SO_TIMEOUT = 1000;
	public static String FILE_PATH = ".\\web_src_2\\";

	protected volatile boolean exit = false;
	private ServerSocket ss;
	protected volatile ArrayList<Socket> socList = new ArrayList<Socket>();

	protected static Hashtable<String, String> account;

	public void serve() {
		try {
			ss = new ServerSocket(PORT);

			while (exit == false) {
				System.out.println("Waiting...");
				Socket socket = ss.accept();
				System.out.println("Client accepted");

				socket.setSoLinger(true, LINGER_TIME);
				// socket.close() will block for LINGER_TIME if there's still data to be sent
//				socket.setSoTimeout(SO_TIMEOUT);
				// socket.read() will block for SO_TIMEOUT if there isn't a byte to read

				Handler h = new Handler(socket, this);
				Thread soc_thread = new Thread(h);
				socList.add(socket);
				soc_thread.setPriority(Thread.MAX_PRIORITY);
				soc_thread.start();
			}
		} catch (IOException ioe) {

		}
	}

	@Override
	public void run() {
		Server.account = new Hashtable<>();
		account.put("admin", "123456");

		this.serve();
	}

	public void stopServer() {
		System.out.println("stopServer");
		try {
			while (socList.isEmpty() == false) {
				Socket soc_tem = socList.get(0);
				try {
					if (soc_tem.isClosed() == false) {
						soc_tem.close();
						System.out.println("stopClient - from server");
					}
					System.out.println("still");
				} catch (Exception e) {

				}
				socList.remove(0);
			}
			this.exit = true;
			this.ss.close();
		} catch (Exception e) {

		}
	}
}
