package org.example.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class CotxeView extends JFrame{
    private JTabbedPane pestanyes;
    private JTable taulaCotxe;
    private JScrollPane scrollPane1;
    private JButton insertarButton;
    private JButton modificarButton;
    private JButton borrarButton;
    private JTextField campNomIModel;
    private JTextField campPes;
    private JCheckBox caixaEsNou;
    private JPanel panel;
    private JTable taulaQuantitat;
    private JComboBox comboProvincia;
    private JTextField campQuantitat;


    //Getters
    public JTable getTaulaQuantitat() {
        return taulaQuantitat;
    }

    public JComboBox getComboProvincia() { return comboProvincia;}

    public JTextField getCampQuantitat() {
        return campQuantitat;
    }

    public JTabbedPane getPestanyes() {
        return pestanyes;
    }

    public JTable getTaulaCotxe() {
        return taulaCotxe;
    }

    public JButton getBorrarButton() {
        return borrarButton;
    }

    public JButton getModificarButton() {
        return modificarButton;
    }

    public JButton getInsertarButton() {
        return insertarButton;
    }

    public JTextField getCampNomIModel() {
        return campNomIModel;
    }

    public JTextField getCampPes() {
        return campPes;
    }

    public JCheckBox getCaixaEsNou() {
        return caixaEsNou;
    }


    //Constructor de la classe
    public CotxeView() {


        //Per poder vore la finestra
        this.setContentPane(panel);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(false);
    }

        private void createUIComponents() {
        // TODO: place custom component creation code here
        scrollPane1 = new JScrollPane();
        taulaCotxe = new JTable();
        pestanyes = new JTabbedPane();
        taulaCotxe.setModel(new DefaultTableModel());
        taulaCotxe.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        scrollPane1.setViewportView(taulaCotxe);

    }
}
