package javaserver;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

import javax.swing.text.html.HTML;

public class Response {

	private static final String breakline = "\r\n";

	private Socket socket;
	private int status;
	private String file_name;
	private String http_version;

	private String response_line;
	private String content_type = "";
	private String content_length = "";
	private byte[] file_read;

	public Response(Socket socket, int status, String file_name, String http_version) {
		// TODO Auto-generated constructor stub
		this.socket = socket;
		this.status = status;
		this.file_name = file_name;
		this.http_version = http_version;

		set_response_line();
		set_content_type();
		try {
			this.file_read = read_file();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.status = Http_error.HTTP500Interal_Server_Error;
			set_response_line();
			set_content_type();
			try {
				this.file_read = read_file();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		this.content_length = "Content-Length: "+ String.valueOf(this.file_read.length);
	}

	public void set_response_line() {
		switch (this.status) {
		case Http_error.HTTP200OK:
			this.response_line = this.http_version + " " + this.status + " OK";
			break;
		case Http_error.HTTP404Not_found:
			this.response_line = this.http_version + " " + this.status + " File Not Found";
			this.file_name = "fileNotFound.html";
			break;
		case Http_error.HTTP401Unauthorized:
			this.response_line = this.http_version + " " + this.status + " Unauthorized";
			this.file_name = "unauthorized.html";
			break;
		case Http_error.HTTP500Interal_Server_Error:
			this.response_line = this.http_version + " " + this.status + " Internal Server Error";
			this.file_name = "internalServerError.html";
			break;
		}
	}

	public void set_content_type() {
		this.content_type = "Content-Type: ";
		if (this.file_name.endsWith(".html") || this.file_name.endsWith(".htm")) {
			this.content_type += "text/html";
		} else if (this.file_name.endsWith(".txt")) {
			this.content_type += "text/plain";
		} else if (this.file_name.endsWith(".jpg") || this.file_name.endsWith(".jpeg")) {
			this.content_type += "image/jpeg";
		} else if (this.file_name.endsWith(".gif")) {
			this.content_type += "image/gif";
		} else if (this.file_name.endsWith(".png")) {
			this.content_type += "image/png";
		} else if (this.file_name.endsWith(".css")) {
			this.content_type += "text/css";
		} else {
			this.content_type += "application/octet-stream";
		}
	}

	public byte[] read_file() throws IOException {
		File file = new File(Server.FILE_PATH + file_name);
		FileInputStream read_file = new FileInputStream(file);
		byte[] buf = new byte[(int) file.length()];
		read_file.read(buf);
		return buf;
	}

	// file client need was found and this function is used to send it
	public void send_the_response() {
		try {
			OutputStream server_output = this.socket.getOutputStream();
			server_output.write((this.response_line + this.breakline).getBytes());
			server_output.write((this.content_length + this.breakline).getBytes());
			server_output.write((this.content_type + this.breakline).getBytes());
			server_output.write((this.breakline).getBytes());
			server_output.write(this.file_read);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
