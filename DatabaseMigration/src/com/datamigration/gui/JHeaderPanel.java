package com.datamigration.gui;

import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;

public class JHeaderPanel extends JPanel {
    
	public JHeaderPanel() {
		setBounds(0, 0, 550, 72);
		setBackground(Color.LIGHT_GRAY);
		setLayout(null);
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setIcon(new ImageIcon("res/banner.jpg"));
		lblNewLabel.setBounds(0, 0, 550, 71);
		add(lblNewLabel);
		
		

	}
}
