package javaserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;

public class Server implements Runnable {

	private static final int PORT = 8080;
	private static final int LINGER_TIME = 5000;
	public static String FILE_PATH = ".\\web_src_2\\";

	protected volatile boolean exit = false;
	private ServerSocket ss;
	protected volatile ArrayList<Socket> socList = new ArrayList<Socket>();

	protected static Hashtable<String, String> account;

	public void serve() {
		try {
			ss = new ServerSocket(PORT);

			while (exit == false) {
				Socket socket = ss.accept();

				socket.setSoLinger(true, LINGER_TIME);

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
		try {
			while (socList.isEmpty() == false) {
				Socket soc_tem = socList.get(0);
				try {
					if (soc_tem.isClosed() == false) {
						soc_tem.close();
					}
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
