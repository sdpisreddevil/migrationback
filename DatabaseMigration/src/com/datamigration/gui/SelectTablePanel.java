package com.datamigration.gui;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import com.datamigration.dbtabledata.TableColumnMetaExtractor;
import com.datamigration.dbtabledata.TableDataExtractor;
import java.awt.Component;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class SelectTablePanel extends JPanel implements ActionListener {

    private JButton btnCancel;
    private JButton btnNext;
    private JPanel panel;

    static ArrayList<String> allTables;
    private ArrayList<String> selectedTables;
    private JButton btnSelectAll;
    private JButton btnReset;
    private JTable table;
    private final DefaultTableModel model;

    public SelectTablePanel(JPanel rootPanel) {

        allTables = new TableColumnMetaExtractor().getTableName();

        panel = rootPanel;
        setBackground(Color.WHITE);
        setBounds(0, 72, 550, 368);
        setLayout(null);

        // ADD SCROLLPANE
        JScrollPane scroll = new JScrollPane();
        scroll.setBounds(38, 30, 258, 199);
        add(scroll);

        // for buttons
        btnSelectAll = new JButton("Select All");
        btnSelectAll.setBounds(418, 30, 100, 30);
        add(btnSelectAll);

        btnReset = new JButton("Reset");
        btnReset.setBounds(418, 93, 100, 30);
        add(btnReset);

        btnNext = new JButton("Next");
        btnNext.setBounds(290, 284, 89, 23);
        add(btnNext);

        btnCancel = new JButton("Cancel");
        btnCancel.setBounds(418, 284, 89, 23);
        add(btnCancel);

        // THE TABLE
        table = new JTable();
        table.setRowHeight(22);
        scroll.setViewportView(table);
        model = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                //to make cell non-editable
                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Boolean.class;
                    case 1:
                        return String.class;

                    default:
                        return String.class;
                }

            }
        };

        // ASSIGN THE MODEL TO TABLE
        table.setModel(model);
        model.addColumn("Select");
        model.addColumn("Table Name");

        // THE ROW VALUES
        for (int i = 0; i < allTables.size(); i++) {
            model.addRow(new Object[0]);
            model.setValueAt(false, i, 0);
            model.setValueAt(allTables.get(i), i, 1);
        }

        // listening events for btns
        btnSelectAll.addActionListener(this);
        btnReset.addActionListener(this);
        btnNext.addActionListener(this);
        btnCancel.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (arg0.getSource() == btnSelectAll) {
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(true, i, 0);
            }
        } else if (arg0.getSource() == btnReset) {
            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(false, i, 0);
            }

        } else if (arg0.getSource() == btnCancel) {
            System.exit(0);
        } else if (arg0.getSource() == btnNext) {
            selectedTables = new ArrayList<>();
            // GET SELECTED ROW
            for (int i = 0; i < table.getRowCount(); i++) {
                Boolean checked = Boolean.valueOf(table.getValueAt(i, 0).toString());
                String col = table.getValueAt(i, 1).toString();
                // DISPLAY
                if (checked) {
                    selectedTables.add(col);
                }
            }
            for (int j = 0; j < selectedTables.size(); j++) {
                System.out.println(selectedTables.get(j));
            }
            if (selectedTables.size() > 0) {
                TableDataExtractor tblExtract = new TableDataExtractor(selectedTables, panel);
                tblExtract.start();
            } else {
                JOptionPane.showMessageDialog(this, "NO TABLE SELECTED");
            }
        }

    }

}
