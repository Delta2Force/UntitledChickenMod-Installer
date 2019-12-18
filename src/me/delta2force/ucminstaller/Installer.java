package me.delta2force.ucminstaller;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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
import java.util.ArrayList;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

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

	/**
	 * Create the frame.
	 */
	public Installer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setTitle("Untitled Chicken Mod Installer/Updater");
		setBounds(100, 100, 662, 349);
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
		
		JLabel lblNewLabel_1 = new JLabel("Installed Version: " + uccVersion);
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JLabel lblLatestUntitledChicken = new JLabel("Latest Version: PLACEHOLDER");
		lblLatestUntitledChicken.setHorizontalAlignment(SwingConstants.CENTER);
		lblLatestUntitledChicken.setForeground(Color.WHITE);
		lblLatestUntitledChicken.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		JButton btnNewButton = new JButton("Install / Update");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
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
					.addComponent(lblNewLabel_1, GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblLatestUntitledChicken, GroupLayout.DEFAULT_SIZE, 626, Short.MAX_VALUE)
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 193, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel_1)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblLatestUntitledChicken, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnNewButton, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE))
		);
		contentPane.setLayout(gl_contentPane);
	}
}
