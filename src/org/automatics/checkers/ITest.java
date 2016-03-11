package org.automatics.checkers;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class ITest extends JFrame {

	JPanel mainPanel;
	JLabel dispLabel;
	JPanel bar;
	String dispText = "Internet Connection: ";
	boolean alive, prev;
	Color curr;

	ITest() {
		super("Auto Internet Connection Monitor v1.0b");
		alive = true;
		prev = isInternetReachable();
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		dispLabel = new JLabel(dispText);
		dispLabel.setSize(400, 100);
		bar = new JPanel();
		curr = Color.GREEN;
		bar.setBackground(curr);
		int width = 600;
		int height = 100;
		setSize(width, height);
		mainPanel = new JPanel(new GridLayout(1, 2));
		mainPanel.add(dispLabel);
		mainPanel.add(bar);
		add(mainPanel);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
			}
		});

		setVisible(true);
		setResizable(false);
		runcheck();
	}

	TimerTask task = new TimerTask() {
		@Override
		public void run() {
			alive = isInternetReachable();
			String ts = new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime());
			if (alive) {
				dispLabel.setText(dispText + " working OK! at " + ts);
				curr = Color.GREEN;
				if (!prev) {
					flashwin();
				}
				prev = true;
			} else {
				dispLabel.setText(dispText + " not working! at " + ts);
				curr = Color.RED;
				if (prev) {
					flashwin();
				}
				prev = false;
			}
			bar.setBackground(curr);
			repaint();
		}
	};

	private void flashwin() {
		if (!isActive()) {
			toFront();
		}
	}

	private void runcheck() {
		Timer timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);
	}

	public static boolean isInternetReachable() {
		try {
			URL u = new URL("https://www.google.com");
			HttpURLConnection h = (HttpURLConnection) u.openConnection();
			h.setReadTimeout(1000);
			h.getContent();
		} catch (UnknownHostException e) {
			// e.printStackTrace();
			return false;
		} catch (IOException e) {
			// e.printStackTrace();
			return false;
		}
		return true;
	}

	public static void main(String[] args) {
		new ITest();
	}
}
