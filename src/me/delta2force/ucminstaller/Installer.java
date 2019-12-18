package me.delta2force.ucminstaller;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.JSeparator;
import javax.swing.LookAndFeel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JButton;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.time.Instant;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JProgressBar;

public class Installer extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Installer frame = new Installer();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static GithubResponse response = null;
	public Thread workThread;

	/**
	 * Create the frame.
	 */
	public Installer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Untitled Chicken Mod Installer/Updater");
		setBounds(100, 100, 662, 340);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(60, 107, 215));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(Installer.class.getResource("/me/delta2force/ucminstaller/logo.png")));
		
		JSeparator separator = new JSeparator();
		
		String workingDirectory = "";
		String OS = (System.getProperty("os.name")).toUpperCase();

		if (OS.contains("WIN"))
		{
		    workingDirectory = System.getenv("AppData");
		}
		else
		{
		    workingDirectory = System.getProperty("user.home");
		    if(OS.contains("MAC")) {
			    workingDirectory += "/Library/Application Support";
		    }
		}
		
		File minecraftDirectory = new File(workingDirectory, ".minecraft");
		if(!minecraftDirectory.exists()) {
			JOptionPane.showMessageDialog(null, "You need to own the Java Edition of Minecraft to install/use the Untitled Chicken Mod!\nClosing setup.");
			System.exit(0);
		}
		
		File versions = new File(minecraftDirectory, "versions");
		File profiles = new File(minecraftDirectory, "launcher_profiles.json");
		
		String uccVersion = "None";
		ArrayList<File> chickenModInstallations = new ArrayList<>();
		
		for(File f : versions.listFiles()) {
			if(f.getName().contains("UntitledChickenMod-") && f.isDirectory()) {
				chickenModInstallations.add(f);
			}
		}
		for(File f : chickenModInstallations) {
			String ver = f.getName().replace("UntitledChickenMod-", "");
			if(uccVersion.equals("None")) {
				uccVersion = ver;
			}else {
				if(Float.parseFloat(ver) > Float.parseFloat(uccVersion)) {
					uccVersion = ver;
				}
			}
		}
		
		JLabel installedVersionLabel = new JLabel("Installed Version: " + uccVersion);
		installedVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		installedVersionLabel.setForeground(Color.WHITE);
		installedVersionLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		try {
			URL request = new URL("https://api.github.com/repos/Delta2Force/UntitledChickenMod/releases/latest");
			InputStream inst = request.openStream();
			response = new Gson().fromJson(new InputStreamReader(inst), GithubResponse.class);
			inst.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		String latestVersion = "";
		boolean thingWorks = true;
		if(response == null || response.tag_name == null) {
			latestVersion = "Connection failed. Please restart.";
			thingWorks = false;
		}else {
			latestVersion = response.getVersion();
		}
		
		JLabel latestVersionLabel = new JLabel("Latest Version: " + latestVersion);
		latestVersionLabel.setHorizontalAlignment(SwingConstants.CENTER);
		latestVersionLabel.setForeground(Color.WHITE);
		latestVersionLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JButton btnNewButton = new JButton("Install / Update");
		btnNewButton.setEnabled(thingWorks);
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				workThread = new Thread(new Runnable() {
					@Override
					public void run() {
						installedVersionLabel.setText("");
						installMod(btnNewButton, latestVersionLabel, versions, profiles);
					}
				});
				workThread.start();
			}
		});
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 646, Short.MAX_VALUE)
				.addComponent(separator, Alignment.TRAILING, GroupLayout.DEFAULT_SIZE, 646, Short.MAX_VALUE)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(231)
					.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 181, Short.MAX_VALUE)
					.addGap(234))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(installedVersionLabel, GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(latestVersionLabel, GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(installedVersionLabel)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(latestVersionLabel, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 40, GroupLayout.PREFERRED_SIZE))
		);
		contentPane.setLayout(gl_contentPane);
	}
		
	public static void installMod(JButton btnNewButton, JLabel progressText, File versions, File profiles) {
		btnNewButton.setEnabled(false);
		btnNewButton.setText("Working...");
		progressText.setText("Initialising...");
		ZipInputStream zis = null;
		try {
			InputStream is = new URL(response.assets[0].browser_download_url).openStream();
			progressText.setText("Downloading... (This will take a while)");
			zis = new ZipInputStream(is);
		} catch (MalformedURLException e4) {
			e4.printStackTrace();
		} catch (IOException e4) {
			e4.printStackTrace();
		}
		ZipEntry nextEntry;
        try {
        	if(new File(versions, "UntitledChickenMod-" + response.getVersion()).exists()) {
        		JOptionPane.showMessageDialog(null, "You already have that version! Closing setup.");
        		System.exit(0);
        	}
        	progressText.setText("Extracting downloaded file...");
			while ((nextEntry = zis.getNextEntry()) != null) {
			    final String name = nextEntry.getName();
			    if (!name.endsWith("/")) {
			    	File f = new File(versions, "UntitledChickenMod-" + response.getVersion());
			    	f.mkdirs();
			        final File nextFile = new File(f, name);

			        final File parent = nextFile.getParentFile();
			        if (parent != null) {
			            parent.mkdirs();
			        }

			        // write file
			        try (OutputStream targetStream = new FileOutputStream(nextFile)) {
			            copy(zis, targetStream);
			        } catch (FileNotFoundException e1) {
						e1.printStackTrace();
						return;
					} catch (IOException e2) {
						e2.printStackTrace();
						return;
					}
			    }
			}
		} catch (IOException e3) {
			e3.printStackTrace();
		}
        progressText.setText("Creating launcher profile...");
        try {
			LauncherProfiles lp = new Gson().fromJson(new FileReader(profiles), LauncherProfiles.class);
			ProfileInfo pi = new ProfileInfo();
			pi.icon = "data:image/png;base64,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";
			pi.name = "Untitled Chicken Mod " + response.getVersion();
			pi.created = Instant.now().toString();
			pi.lastUsed = Instant.now().toString();
			pi.lastVersionId = "UntitledChickenMod-" + response.getVersion();
			pi.type = "custom";
			lp.profiles.put("Untitled Chicken Mod " + response.getVersion(), pi);
			profiles.delete();
			profiles.createNewFile();
			FileWriter fw = new FileWriter(profiles);
			new Gson().toJson(lp, fw);
			fw.flush();
			fw.close();
		} catch (JsonSyntaxException | IOException | JsonIOException e5) {
			e5.printStackTrace();
		}
        
        JOptionPane.showMessageDialog(null, "Installed Untitled Chicken Mod " + response.getVersion() + "!\nMake sure to restart the Launcher if you haven't\nalready. Have fun!");
        System.exit(0);
	}
	
	private static void copy(final InputStream source, final OutputStream target) throws IOException {
        final int bufferSize = 4 * 1024;
        final byte[] buffer = new byte[bufferSize];

        int nextCount;
        while ((nextCount = source.read(buffer)) >= 0) {
            target.write(buffer, 0, nextCount);
        }
    }
	
	static class GithubResponse{
		public String tag_name;
		public String getVersion() {
			return tag_name;
		}
		public GithubAsset[] assets;
	}
	
	static class GithubAsset{
		public String name;
		public String browser_download_url;
	}
	
	static class LauncherProfiles{
		public int analyticsFailCount;
		public String analyticsToken;
		public HashMap<Object, Object> authenticationDatabase;
		public String clientToken;
		public Object launcherVersion;
		public HashMap<String, ProfileInfo> profiles;
		public HashMap<Object, Object> selectedUser;
		public HashMap<Object, Object> settings;
	}
	
	static class ProfileInfo {
		public String created;
		public String javaArgs;
		public String lastUsed;
		public String lastVersionId;
		public String name;
		public String type;
		public String icon;
	}
}
