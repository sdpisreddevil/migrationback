package com.datamigration.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.datamigration.dbconnection.MSAccessConnect;
import com.datamigration.scripts.AppMessage;
import com.datamigration.scripts.CMNDBConfig;
import javax.swing.ImageIcon;

public class MsAccessPanel extends JPanel implements ActionListener {

	private JButton btnCancel;
	private JButton btnNext;
	private JPanel rootPanel;
	private JLabel lblSelectMsaccessDb;
	private JLabel lblSourceDbPath;
	private JLabel lblPwd;
	private JButton btnBrowse;
	private JLabel lblPath;
        private JLabel lblMsAccessImg;
	private JPasswordField msAccessPwd;
	private String path;
	private JLabel lblNote;

	public MsAccessPanel(JPanel rootPanel) {
		this.rootPanel = rootPanel;
		setBackground(Color.WHITE);
		setBounds(0, 61, 550, 368);
		setLayout(null);

		btnNext = new JButton("Next");
		btnNext.addActionListener(this);
		btnNext.setBounds(290, 284, 89, 23);
		add(btnNext);

		btnCancel = new JButton("Cancel");
                btnCancel.addActionListener(this);
		btnCancel.setBounds(418, 284, 89, 23);
		add(btnCancel);
                
		lblSelectMsaccessDb = new JLabel("Select MS-Access DB:");
		lblSelectMsaccessDb.setBounds(32, 41, 144, 23);
		add(lblSelectMsaccessDb);

		lblSourceDbPath = new JLabel("Source DB:");
		lblSourceDbPath.setBounds(32, 75, 128, 14);
		add(lblSourceDbPath);
		lblSourceDbPath.setVisible(false);

		lblPwd = new JLabel("Enter Password:");
		lblPwd.setBounds(32, 105, 99, 14);
		lblPwd.setVisible(false);
		add(lblPwd);

		btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(this);
		btnBrowse.setBounds(167, 41, 89, 23);
		add(btnBrowse);

		lblPath = new JLabel("Path");
		lblPath.setBounds(167, 75, 300, 14);
		lblPath.setVisible(false);
		add(lblPath);

		msAccessPwd = new JPasswordField();
		msAccessPwd.setBounds(167, 105, 120, 20);
		msAccessPwd.setVisible(false);
		add(msAccessPwd);
		
		lblNote = new JLabel("**Enter Password if password protected!");
		lblNote.setForeground(Color.RED);
		lblNote.setVisible(false);
		lblNote.setBounds(30, 130, 300, 14);
		add(lblNote);
                
                lblMsAccessImg = new JLabel();
                lblMsAccessImg.setBounds(350, 5, 200, 200);
                lblMsAccessImg.setIcon(new ImageIcon("res/1.png"));
                add(lblMsAccessImg);

	}

	@Override
	public void actionPerformed(ActionEvent arg0) {

		if (arg0.getSource() == btnBrowse) {
			JFileChooser fileChooser = new JFileChooser();
			FileNameExtensionFilter filter = new FileNameExtensionFilter("MS-Access Database",
					new String[] { "mdb", "accdb" });
			fileChooser.setFileFilter(filter);
			int returnValue = fileChooser.showOpenDialog(null);

			if (returnValue == JFileChooser.APPROVE_OPTION) {
				File selectedFile = fileChooser.getSelectedFile();
				lblPath.setVisible(true);
				path = selectedFile.getAbsolutePath();
				lblPath.setText(selectedFile.getName());
				lblSourceDbPath.setVisible(true);
				lblPwd.setVisible(true);
				msAccessPwd.setVisible(true);
				lblNote.setVisible(true);
			}
		} else if (arg0.getSource() == btnCancel) {
			System.exit(0);
		} else if (arg0.getSource() == btnNext) {
			String text = "";
			if (!msAccessPwd.getText().isEmpty()) {
				text = msAccessPwd.getText();
			}
			CMNDBConfig.setMSACCESS_PATH(path);
			CMNDBConfig.setMSACCESS_PWD(text);
			AppMessage appMsg = new MSAccessConnect().getCurrentMsaccessConnection();
			if (appMsg.getCODE() != 0) {
				JOptionPane.showMessageDialog(this, appMsg.getMSG());
				return;
			}
			JOptionPane.showMessageDialog(this, appMsg.getMSG());
			rootPanel.remove(1);
			rootPanel.add(new MySQLPanel(rootPanel), 1);
			rootPanel.repaint();
		}
	}

}
