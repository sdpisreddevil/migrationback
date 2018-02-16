package com.datamigration.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.datamigration.dbtabledata.TableColumnMetadata;
import com.datamigration.scripts.AppMessage;

public class IntermediatePanel extends JPanel implements ActionListener {

    private JButton btnCancel;
    private JButton btnNext;
    private JPanel rootPanel;
    private AppMessage appMsg;
    private JTextArea txtlog;

    public IntermediatePanel(JPanel rootPanel) {
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

        txtlog = new JTextArea();
        txtlog.setEditable(false);
        txtlog.setBounds(21, 11, 486, 234);
        add(txtlog);

        btnNext.addActionListener(this);
        btnCancel.addActionListener(this);

        process();
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == btnNext) {
            rootPanel.remove(1);
            rootPanel.add(new SelectTablePanel(rootPanel), 1);
            rootPanel.repaint();
        } else if (arg0.getSource() == btnCancel) {
            System.exit(0);
        }
    }

    public void process() {

        txtlog.append("Please Wait.....\r\n");
        txtlog.append("Fetching Database Metadata.....\r\n");
        appMsg = new TableColumnMetadata().getTableMetadata();
        if (appMsg.getCODE() != 0) {
            txtlog.append(appMsg.getMSG());
        } else {
            txtlog.append(appMsg.getMSG());
        }
    }
}
