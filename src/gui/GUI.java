package gui;

import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javaserver.Server;

import com.jgoodies.forms.layout.FormSpecs;
import java.awt.Color;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JLabel;
import java.awt.CardLayout;
import java.awt.Font;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Toolkit;

public class GUI {
	public class JImageComponent extends JComponent {
		private Image img;

		public JImageComponent(Image i) {
			this.img = i;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			this.setBounds(0, 0, this.getParent().getWidth(), this.getParent().getHeight());
			g.drawImage(img, 0, 0, this.getParent().getWidth(), this.getParent().getHeight(), null);
		}
	}

	private JFrame frmWebServer;
	private Image theme;
	private JImageComponent icTheme;
	private BorderDiagGradient_GUI customBorder = null;
	private JButton serverBtn;
	private Thread server;
	private Server serverConnection;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frmWebServer.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		theme = new ImageIcon("./gui_src/Theme.png").getImage();
		icTheme = new JImageComponent(theme);

		initialize();
		customBorder.setSpinDiag(1);

		ActionListener listener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				serverBtn.repaint();
			}
		};
		Timer timer = new Timer(40, listener);
		timer.start();

//		Image theme = new ImageIcon("./gui_src/Theme.png").getImage();
//		JImageComponent icTheme = new JImageComponent(theme, 0, 0);
//		frame.getContentPane().add(icTheme);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmWebServer = new JFrame();
		frmWebServer.setTitle("Web Server");
		frmWebServer.setIconImage(Toolkit.getDefaultToolkit()
				.getImage("D:\\programin\\Git\\ServerSocket\\JavaServer\\gui_src\\Logo2.png"));
		frmWebServer.setBounds(100, 100, 700, 400);
		frmWebServer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmWebServer.getContentPane().setLayout(new BorderLayout(0, 0));

		JLayeredPane layeredPane = new JLayeredPane();
		frmWebServer.getContentPane().add(layeredPane, BorderLayout.CENTER);
		layeredPane.setLayout(null);

		JPanel backgroundPn = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				this.setBounds(0, 0, this.getParent().getWidth(), this.getParent().getHeight());
			}
		};
		backgroundPn.setLayout(new BorderLayout(0, 0));
//		backgroundPn.setBackground(new Color(123,21,54));
		backgroundPn.add(icTheme, BorderLayout.CENTER);
		backgroundPn.setBounds(1, 1, 1, 1);

		layeredPane.setLayer(backgroundPn, 1);
		layeredPane.add(backgroundPn);

		serverBtn = new JButton("OPEN SERVER") {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				int w = this.getParent().getWidth() / 8;
				int h = this.getParent().getHeight() / 35;
				int x = w * 1;
				int y = h * 12;
				this.setBounds(x, y, w * 2, h * 11);
			}
		};
		serverBtn.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (serverBtn.getText().equals("OPEN SERVER")) {
					serverBtn.setText("CLOSE SERVER");
					serverConnection = new Server();
					server = new Thread(serverConnection);
					server.start();

				} else if (serverBtn.getText().equals("CLOSE SERVER")) {
					serverBtn.setText("OPEN SERVER");
					serverConnection.stopServer();
				}
			}
		});
		serverBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		serverBtn.setForeground(new Color(248, 248, 255));
		serverBtn.setBackground(new Color(30, 30, 30));
		serverBtn.setFont(new Font("Comic Sans MS", Font.BOLD, 16));
		layeredPane.setLayer(serverBtn, 2);
		serverBtn.setBounds(51, 43, 190, 115);
		layeredPane.add(serverBtn);

		customBorder = new BorderDiagGradient_GUI(3, new Color(50, 8, 8), new Color(200, 12, 12));
		serverBtn.setBorder(customBorder);
		serverBtn.setFocusPainted(false);
	}
}
