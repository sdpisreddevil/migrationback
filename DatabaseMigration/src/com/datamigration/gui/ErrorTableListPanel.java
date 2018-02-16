package com.datamigration.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.datamigration.dbtabledata.ErrorColumnMetaExtractor;
import com.datamigration.dbtabledata.TableColumnMetaExtractor;
import com.datamigration.scripts.AppMessage;
import com.datamigration.scripts.Utils;
import java.awt.Font;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class ErrorTableListPanel extends JPanel {

    private JPanel rootPanel;
    private JLabel lblErrorTbl;

    private JFrame jFrame;
    private JList errorTableJList;
    private DefaultListModel m1;
    private int len = 0;
    private JScrollPane js;
    private Utils util;
    private ArrayList<String> errorTableList;
    private ArrayList<String> itemList;
    private ArrayList<String> colData;
    private int index = 0;
    private JButton btnCancel;

    public ErrorTableListPanel(JPanel rootPanel) {

        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);
        this.rootPanel = rootPanel;

        errorTableList = new ErrorColumnMetaExtractor().getTableName("error_log.txt");

        // to remove duplicate values in list
        Set<String> set = new HashSet<>();
        set.addAll(errorTableList);
        errorTableList.clear();
        errorTableList.addAll(set);

        m1 = new DefaultListModel();

        lblErrorTbl = new JLabel("Table Containing Errors");
        lblErrorTbl.setForeground(Color.DARK_GRAY);
        lblErrorTbl.setBounds(190, 0, 250, 50);
        lblErrorTbl.setFont(new Font("Cambria Math", Font.PLAIN, 20));
        add(lblErrorTbl);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(418, 284, 89, 23);
        add(btnCancel);

        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                rootPanel.remove(1);
                rootPanel.add(new ThankYouPanel(rootPanel), 1);
                rootPanel.repaint();
            }
        });

        for (int i = 0; i < errorTableList.size(); i++) {
            m1.add(i, errorTableList.get(i));
        }
        errorTableJList = new JList(m1);
        js = new JScrollPane(errorTableJList);
        js.setBorder(null);
        js.setEnabled(true);
        js.setVisible(true);
        js.setBounds(190, 40, 100, 200);
        add(js);

        errorTableJList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                errorTableJList(evt);
            }
        });

    }

    public void errorTableJList(MouseEvent evt) {
        JList list = (JList) evt.getSource();
        if (evt.getClickCount() == 2) {
            index = list.locationToIndex(evt.getPoint());
            System.out.println(errorTableList.get(index));

            // Double-click detected
            itemList = new TableColumnMetaExtractor().getColumnMetaDataArrayList(errorTableList.get(index));
            util = new Utils(errorTableList.get(index));
            JDialog dialog = new JDialog(jFrame);
            dialog.setTitle("Table containing errors");
            ImageIcon icon = new ImageIcon("res/logo.jpg");
            dialog.setIconImage(icon.getImage());
            dialog.setBounds(450, 150, 600, 200);
            dialog.setLayout(new BorderLayout());

            JPanel panel = new JPanel();
            JTable table = new JTable();
            table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
            table.setRowHeight(20);

            JScrollPane jsp = new JScrollPane(table);
            jsp.setPreferredSize(new Dimension(470, 300));

            JButton button = new JButton("Update");
            button.setSize(100, 100);
            button.setBounds(310, 450, 100, 100);

            JButton btnCancel = new JButton("Cancel");
            btnCancel.setSize(100, 100);
            btnCancel.setBounds(310, 600, 100, 100);
            btnCancel.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialog.dispose();
                }
            });
            DefaultTableModel model = new DefaultTableModel();

            for (int i = 0; i < itemList.size(); i++) {
                model.addColumn(itemList.get(i));
            }

            // Fetching columnData
            colData = new ErrorColumnMetaExtractor().getColumnMetaDataArrayList("error_log.txt",
                    errorTableList.get(index));
            for (int i = 0; i < colData.size(); i++) {
                String[] aa = colData.get(i).split(", ");
                // length of array
                len = aa.length;

                // Splitting column values
                for (int j = 0; j < len; j++) {
                    model.addRow(new Object[0]);
                    int length = aa[j].length(); // length of string
                    if (aa[j].contains("'")) {
                        model.setValueAt(aa[j].substring(1, length - 1), i, j);

                    } else {
                        model.setValueAt(aa[j], i, j);
                    }

                    length = 0;
                }

            }

            // new window for error correction
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AppMessage querySts = null;
                    for (int i = 0; i < colData.size(); i++) {
                        String tempColVal = "";
                        
                        // Splitting column values
                        for (int j = 0; j < len; j++) {

                            // checking for timestamp
                            if (util.isTimeStamp(table.getColumnName(j))) {
                                if (!util.isDateFormatCorrect(table.getValueAt(i, j).toString())) {
                                    JOptionPane.showMessageDialog(null,
                                            "Sorry cannot accept " + table.getValueAt(i, j).toString());
                                    return;
                                }
                                tempColVal += "'" + table.getValueAt(i, j) + "'";
                            } else {
                                // checking for invalid data inserted
                                if (!util.isValid(table.getValueAt(i, j).toString())) {
                                    JOptionPane.showMessageDialog(null,
                                            "Sorry cannot accept " + table.getValueAt(i, j).toString());
                                    return;
                                }
                                // checking for varchar,char
                                int flg = util.checkVarchar(table.getColumnName(j));
                                if (flg == 1) {
                                    tempColVal += "'" + table.getValueAt(i, j) + "'";
                                } else {
                                    tempColVal += table.getValueAt(i, j);
                                }
                            }
                            // building column values
                            if (j < len - 1) {
                                tempColVal += ",";
                            } else {
                                tempColVal += "";
                            }
                        } // end of inner for loop
                        querySts = util.dynSqlQuery(tempColVal);
                    } // end of outer for loop
                    if (querySts.getCODE() == 0) {
                        System.out.println(index);
                        JOptionPane.showMessageDialog(null, querySts.getMSG());

                        // deletes files from list and arrraylist
                        m1.remove(index);
                        errorTableList.remove(index);
                        // if editing error done then close the current frame and display thank you
                        // frame
                        if (errorTableList.isEmpty()) {
                            rootPanel.remove(1);
                            rootPanel.add(new ThankYouPanel(rootPanel), 1);
                            rootPanel.repaint();
                        }
                        dialog.dispose();
                    } else {
                        JOptionPane.showMessageDialog(null, querySts.getMSG());
                    }
                } // end of action performed
            });

            table.setModel(model);
            table.setEnabled(true);
            panel.add(jsp, BorderLayout.NORTH);
            panel.setVisible(true);
            panel.add(button, BorderLayout.SOUTH);
            panel.add(btnCancel, BorderLayout.SOUTH);
            dialog.add(panel);
            dialog.setSize(500, 400);
            dialog.setModal(true);
            dialog.setVisible(true);
        }
    }
}
