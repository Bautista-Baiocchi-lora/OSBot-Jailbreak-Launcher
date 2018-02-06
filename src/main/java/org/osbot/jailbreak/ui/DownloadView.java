package org.osbot.jailbreak.ui;

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
	private final File file;

	public DownloadView(LauncherController controller, String name, File file, HttpURLConnection connection) {
		this.controller = controller;
		this.file = file;
		this.progressBar = new JProgressBar(0, 100);
		this.progressBar.setStringPainted(true);
		this.progressBar.setString("Downloading " + name + "...");
		this.progressBar.setPreferredSize(new Dimension(300, 50));

		downloader = new Downloader(file, connection);
		downloader.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent evt) {
				if ("progress".equals(evt.getPropertyName())) {
					progressBar.setValue((Integer) evt.getNewValue());
				} else {

				}
			}
		});
		add(progressBar);
	}

	public void start() {
		if (downloader != null) {
			downloader.execute();
		}
	}

	public boolean isDownloading() {
		return !downloader.isDone() && !downloader.isCancelled();
	}

	private class Downloader extends SwingWorker<Void, Void> {

		private final File file;
		private final HttpURLConnection connection;
		private int length, written;

		public Downloader(File file, HttpURLConnection connection) {
			this.file = file;
			this.connection = connection;
		}

		@Override
		protected Void doInBackground() throws Exception {
			OutputStream output;
			InputStream input;
			try {
				length = connection.getContentLength();
				output = new FileOutputStream(file);
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
			if (file.getName().endsWith("zip")) {
				NetUtils.unZip(file.getAbsolutePath());
				controller.environmentValidated();
			} else {
				controller.osbotJarValidated();
			}
		}
	}


}
