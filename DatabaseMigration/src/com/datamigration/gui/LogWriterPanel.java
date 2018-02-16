package com.datamigration.gui;

import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.io.File;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class LogWriterPanel extends JPanel {

    private JPanel rootPanel;
    private JTextArea tArea;
    private JScrollPane pane;

    public LogWriterPanel(JPanel rootPanel) {
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        this.rootPanel = rootPanel;
        
        tArea = new JTextArea(10, 20);
        pane = new JScrollPane(tArea);
        pane.setBounds(5, 5, 530, 320);
        add(pane);
        tArea.setLineWrap(true);
        tArea.setWrapStyleWord(true);
        pane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
            public void adjustmentValueChanged(AdjustmentEvent e) {  
                e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
            }

			
        });
       }
    
    public void writeLog(String s) {
        tArea.append(s);
    }

    public void errorTableFrame() {
        File file = new File("error_log.txt");
        if (file.length() == 0) {
            rootPanel.remove(1);
            rootPanel.add(new ThankYouPanel(rootPanel), 1);
            rootPanel.repaint();
        } else {
        	int input = JOptionPane.showConfirmDialog(this, "Some errors were found... Do you like to proceed to correct error in the tables?");
            if (input != 0) {
                rootPanel.remove(1);
                rootPanel.add(new ThankYouPanel(rootPanel), 1);
                rootPanel.repaint();
            }else{
            System.out.println(input);
            rootPanel.remove(1);
            rootPanel.add(new ErrorTableListPanel(rootPanel), 1);
            rootPanel.repaint();
            }
        }
    }

}
