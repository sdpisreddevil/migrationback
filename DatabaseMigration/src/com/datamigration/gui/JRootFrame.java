package com.datamigration.gui;

import java.awt.EventQueue;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class JRootFrame extends JFrame {

    private JPanel contentPane;
    public WelcomePanel welcomePanel;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {

                try {
//                    Thread.sleep(3900);
                    JRootFrame frame = new JRootFrame();
                    frame.setResizable(false);
                    frame.setTitle("Database Migration Wizard v1.0");
                    ImageIcon icon = new ImageIcon("res/logo.jpg");
                    frame.setIconImage(icon.getImage());

                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public JRootFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(350, 150, 550, 440);
        contentPane = new JPanel();
        add(contentPane);
        contentPane.setLayout(null);
        JHeaderPanel headerPanel = new JHeaderPanel();
        headerPanel.setBounds(0, 0, 550, 72);
        contentPane.add(headerPanel);
        welcomePanel = new WelcomePanel(contentPane);
        welcomePanel.setBounds(0, 61, 550, 368);
        contentPane.add(new SwingProgressBarExample(contentPane));
    }
}
