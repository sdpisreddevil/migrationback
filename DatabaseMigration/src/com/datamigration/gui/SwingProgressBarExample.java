package com.datamigration.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import java.awt.Font;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class SwingProgressBarExample extends JPanel implements ActionListener {

    private JButton btnCancel;
    private JButton btnNext;
    private JPanel rootPanel;
    private JPanel progressPanel;
    private JProgressBar jb;
    static final int MY_MINIMUM = 0;
    static final int MY_MAXIMUM = 200;

    public SwingProgressBarExample(JPanel rootPanel) {

        setBorder(new LineBorder(new Color(0, 0, 0)));
        this.rootPanel = rootPanel;
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        btnNext = new JButton("Next");
        btnNext.setBounds(290, 284, 89, 23);
        add(btnNext);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(418, 284, 89, 23);
        add(btnCancel);

        progressPanel = new JPanel();
        progressPanel.setVisible(true);
        progressPanel.setBounds(140, 10, 250, 25);
        progressPanel.setBackground(Color.WHITE);
        add(progressPanel);

        jb = new JProgressBar();
        jb.setMinimum(MY_MINIMUM);
        jb.setMaximum(MY_MAXIMUM);
        jb.setBounds(0, 0, 250, 25);
        jb.setValue(0);
        jb.setStringPainted(true);
        progressPanel.add(jb);

        for (int i = MY_MINIMUM; i <= MY_MAXIMUM; i++) {
            final int percent = i;
            try {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        progressPanel.repaint();
                        jb.setValue(percent);
                    }
                });
                Thread.sleep(200);
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }

        btnNext.addActionListener(this);
        btnCancel.addActionListener(this);

    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == btnNext) {
            rootPanel.remove(1);
            rootPanel.add(new MsAccessPanel(rootPanel), 1);
            rootPanel.repaint();
        } else if (arg0.getSource() == btnCancel) {
            System.exit(0);
        }
    }
}
