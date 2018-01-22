package org.osbot.jailbreak.ui;

import org.osbot.jailbreak.data.Constants;
import org.osbot.jailbreak.utils.NetUtils;

import javax.swing.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.net.HttpURLConnection;

public class DownloadView extends JPanel {

	private final JProgressBar progressBar;
	private final LauncherController controller;
	private final Downloader downloader;


	public DownloadView(LauncherController controller, String source, String filename) {
		this.controller = controller;

		Box layout = Box.createVerticalBox();

		this.progressBar = new JProgressBar(0, 100);
		this.progressBar.setStringPainted(true);
		this.progressBar.setString("Loading environment...");
		this.progressBar.setPreferredSize(new Dimension(300, 50));
		layout.add(progressBar);

		downloader = new Downloader(source, filename);
		downloader.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					progressBar.setValue((Integer) evt.getNewValue());
				}
			}
		});
		add(layout);
	}

	public void start() {
		if (downloader != null) {
			downloader.execute();
		}
	}

	private class Downloader extends SwingWorker<Void, Void> {

		private int length, written;
		private final String source, fileName;

		public Downloader(String source, String fileName) {
			this.source = source;
			this.fileName = fileName;
		}

		@Override
		protected Void doInBackground() throws Exception {
			OutputStream output;
			InputStream input;
			HttpURLConnection connection;
			try {
				connection = NetUtils.getConnection(source);
				length = connection.getContentLength();
				final File destinationDirectory = new File(Constants.DIRECTORY_PATH);
				output = new FileOutputStream(destinationDirectory.getPath() + File.separator + fileName + ".jar");
				input = connection.getInputStream();
				final byte[] data = new byte[1024];
				int read;
				while ((read = input.read(data)) != -1) {
					output.write(data, 0, read);
					written += read;
					this.setProgress((100 * written) / length);
				}
				output.flush();
				output.close();
				input.close();
			} catch (IOException a) {
				System.out.println("Error downloading file!");
				a.printStackTrace();
			}
			return null;
		}

		@Override
		protected void done() {
			controller.showControlView();
		}
	}


}
