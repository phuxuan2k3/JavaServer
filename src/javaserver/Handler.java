package javaserver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class Handler extends Server {
	private Socket socket;
	private String username;
	private String password;
	private volatile boolean exit = false;

	public Handler(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	public boolean is_file_exist(String file_name) {
		File file = new File(Server.FILE_PATH + file_name);
		return file.exists();
	}

	public boolean is_get_request(String method) {
		return method.startsWith("GET");
	}

	public boolean is_post_request(String method) {
		return method.startsWith("POST");
	}

	public void set_username_pass_word(String payload) {
		String[] s1 = payload.split("&");

		String[] s2 = s1[0].split("=");
		this.username = s2[1];

		String[] s3 = s1[1].split("=");
		this.password = s3[1];
	}

	public boolean is_correct_account() {
		String psd = Server.account.get(username);
		if (psd != null && psd.equals(this.password) == true) {
			return true;
		}
		return false;
	}

	public void stopClient() {
		System.out.println("stopClient");
		this.exit = true;
		try {
			socket.close();
		} catch (IOException e) {

		}
		super.socList.remove(this);
	}

	@Override
	public void run() {

		while (exit == false) {
			Request request = new Request(this.socket);
			request.read_http_request(); // Blocks the thread

			if (request.is_there_request() == false) {
				break;
			}
			String file_name = request.get_file_name();

			Response response = null;

			if (is_get_request(request.get_method()) == true) {
				if (is_file_exist(file_name) == true) {
					response = new Response(socket, Http_error.HTTP200OK, file_name, request.get_http_version());
				} else {
					response = new Response(socket, Http_error.HTTP404Not_found, file_name, request.get_http_version());
				}
			} else if (is_post_request(request.get_method()) == true) {
				set_username_pass_word(request.get_payload());
				if (is_correct_account()) {
					response = new Response(socket, Http_error.HTTP200OK, file_name, request.get_http_version());
				} else {
					response = new Response(socket, Http_error.HTTP401Unauthorized, file_name,
							request.get_http_version());
				}
			}

			response.send_the_response();
		}

		this.stopClient();
	}
}
