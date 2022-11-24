package javaserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Request {

	private Socket socket;
	private boolean is_exist = true;
	private String method;
	private String file_name;
	private String http_version;
	private List<String> headers;
	private String payload;

	public Request(Socket socket) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
	}

	public void set_value(String method, String file_name, String http_version, String payload) {
		this.method = method;
		this.file_name = file_name;
		this.http_version = http_version;
		this.payload = payload;
	}

	public boolean is_there_request() {
		return is_exist;
	}

	public String get_method() {
		return this.method;
	}

	public String get_file_name() {
		return this.file_name;
	}

	public String get_http_version() {
		return this.http_version;
	}

	public String get_payload() {
		return this.payload;
	}

	public void read_http_request() {
		try {
			BufferedReader client_input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String request_line = null;

			if ((request_line = client_input.readLine()) == null || request_line.length() == 0) {
				this.is_exist = false;
				return;
			}
			String[] request_line_elements = request_line.split(" ");

			this.headers = new ArrayList<>();
			String header = "";
			while ((header = client_input.readLine()) != null && header.length() != 0) {
				this.headers.add(header);
			}

			String payload = "";
			if (request_line_elements[0].startsWith("POST") == true) {
				while (client_input.ready() == true) {
					payload += String.valueOf((char) client_input.read());
				}
			}

			set_value(request_line_elements[0], request_line_elements[1], request_line_elements[2], payload);
			if (request_line_elements[1].equals("/") == true) {
				this.file_name = "\\index.html";
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.is_exist = false;
		}

	}
}
