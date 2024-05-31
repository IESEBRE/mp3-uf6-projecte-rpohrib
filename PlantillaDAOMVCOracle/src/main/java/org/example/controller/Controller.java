package org.example.controller;

import org.example.model.entities.Cotxe;
import org.example.model.exceptions.DAOException;
import org.example.model.entities.Cotxe.Quantitat;
import org.example.view.ModelComponentsVisuals;
import org.example.model.impls.CotxeDAOJDBCOracleImpl;
import org.example.view.CotxeView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.TreeSet;

public class Controller implements PropertyChangeListener { //1. Implementació de interfície PropertyChangeListener


    private ModelComponentsVisuals modelComponentsVisuals =new ModelComponentsVisuals();
    private CotxeDAOJDBCOracleImpl dadesCotxe;
    private CotxeView view;

    public Controller(CotxeDAOJDBCOracleImpl dadesCotxe, CotxeView view) {
        this.dadesCotxe = dadesCotxe;
        this.view = view;


        lligaVistaModel();

        afegirListeners();

        //5. Necessari per a que Controller reaccione davant de canvis a les propietats lligades
        canvis.addPropertyChangeListener(this);

        //Si ens surt algun error al inciar l'aplicació no mostrarem la finestra
        if(getExcepcio()==null){
            view.setVisible(true);
        }

        //Si no ens se conectar a la base de dades ens surt un missatge d'excepció
        if(getExcepcio()!=null){
            setExcepcio(new DAOException(0));
        }

    }

    private void lligaVistaModel() {

        //Carreguem la taula de menjar en les dades de la BD
        try {
            setModelTaulaCotxe(modelComponentsVisuals.getModelTaulaCotxe(), dadesCotxe.getAll());
        } catch (DAOException e) {
            this.setExcepcio(e);
        }

        //Fixem el model de la taula de cotxes
        JTable taulaCotxe = view.getTaulaCotxe();
        taulaCotxe.setModel(this.modelComponentsVisuals.getModelTaulaCotxe());

        //Amago la columna que conté l'objecte alumne
        taulaCotxe.getColumnModel().getColumn(3).setMinWidth(0);
        taulaCotxe.getColumnModel().getColumn(3).setMaxWidth(0);
        taulaCotxe.getColumnModel().getColumn(3).setPreferredWidth(0);

        //Fixem el model de la taula de quantitat
        JTable taulaQuantitat = view.getTaulaQuantitat();
        taulaQuantitat.setModel(this.modelComponentsVisuals.getModelTaulaQuantitat());

        //Posem valor a el combo de les provincies
        view.getComboProvincia().setModel(modelComponentsVisuals.getComboBoxModel());

        //Desactivem la pestanya de la quantitat
        view.getPestanyes().setEnabledAt(1, false);
        view.getPestanyes().setTitleAt(1, "Quantitat de ...");

    }

    private void setModelTaulaCotxe(DefaultTableModel modelTaulaCotxe, List<Cotxe> all) {

        // Fill the table model with data from the collection
        for (Cotxe cotxe : all) {
            modelTaulaCotxe.addRow(new Object[]{cotxe.getNomIMarca(), cotxe.getPes(), cotxe.isNou(), cotxe});
        }
    }

    private void afegirListeners() {

        ModelComponentsVisuals modelo = this.modelComponentsVisuals;
        DefaultTableModel modelCotxe = modelo.getModelTaulaCotxe();
        DefaultTableModel modelQuantitat = modelo.getModelTaulaQuantitat();
        JTable taulaCotxe = view.getTaulaCotxe();
        JTable taulaQuantitat = view.getTaulaQuantitat();
        JButton insertarButton = view.getInsertarButton();
        JButton modificarButton = view.getModificarButton();
        JButton borrarButton = view.getBorrarButton();
        JTextField campNomIModel = view.getCampNomIModel();
        JTextField campPes = view.getCampPes();
        JCheckBox caixaEsNou = view.getCaixaEsNou();
        JTabbedPane pestanyes = view.getPestanyes();

        //Botó insertar
        view.getInsertarButton().addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField campNomIMarca = view.getCampNomIModel();
                        JTextField campPes = view.getCampPes();
                        JCheckBox caixaEsNou = view.getCaixaEsNou();

                        if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de cotxes
                            // Comprovem que totes les caselles continguen informació
                            if (campNomIMarca.getText().isBlank() || campPes.getText().isBlank()) {
                                JOptionPane.showMessageDialog(null, "Falta omplir alguna dada!!");
                            } else {
                                try {
                                    NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault()); // Creem un número que entèn la cultura que utilitza l'aplicació
                                    double pes = num.parse(campPes.getText().trim()).doubleValue(); // Intentem convertir el text a double
                                    //Comprovem que el nom i la marca no siguin duplicats
                                    for (int i = 0; i < modelCotxe.getRowCount(); i++) {
                                        if (campNomIMarca.getText().equals(modelCotxe.getValueAt(i, 0))) {
                                            setExcepcio(new DAOException(2293));
                                            return;
                                        }
                                    }

                                    //Comprovem que le nom i marcar sigue correcte
                                    if(!campNomIMarca.getText().matches("^[a-zA-Z0-9 ]+$")){
                                        setExcepcio(new DAOException(2296));
                                    }else if (pes < 1 || pes > 100000) {
                                        setExcepcio(new DAOException(2291));
                                    } else {
                                        Cotxe cotxe = new Cotxe(campNomIMarca.getText(), pes, caixaEsNou.isSelected(), new TreeSet<Quantitat>());
                                        modelCotxe.addRow(new Object[]{campNomIMarca.getText(), pes, caixaEsNou.isSelected(), cotxe});

                                        // Inserim el cotxe a la BD
                                        dadesCotxe.insert(cotxe);

                                        //Actualitzem la taula amb el metode que ja tenim
                                        actualitzarTaulaCotxes();

                                        campNomIMarca.setText("Opel");
                                        campNomIMarca.setSelectionStart(0);
                                        campNomIMarca.setSelectionEnd(campNomIMarca.getText().length());
                                        campPes.setText("100");
                                        campNomIMarca.requestFocus(); // Intentem que el foco vagi al camp del nom i marca
                                    }
                                } catch (ParseException ex) {
                                    setExcepcio(new DAOException(1722));
                                    campPes.setSelectionStart(0);
                                    campPes.setSelectionEnd(campPes.getText().length());
                                    campPes.requestFocus();
                                } catch (DAOException ex) {
                                    System.out.println(ex.getMessage());
                                }
                            }
                        }
                    }
                }
        );

        //Boto modificar
        modificarButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JTextField campNomIMarca = view.getCampNomIModel();
                        JTextField campPes = view.getCampPes();
                        JCheckBox caixaEsNou = view.getCaixaEsNou();

                        if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de cotxes
                            // Comprovem que totes les caselles continguen informació
                            if (campNomIMarca.getText().isBlank() || campPes.getText().isBlank()) {
                                JOptionPane.showMessageDialog(null, "Falta omplir alguna dada!!");
                            } else {
                                try {
                                    NumberFormat num = NumberFormat.getNumberInstance(Locale.getDefault()); // Creem un número que entén la cultura que utilitza l'aplicació
                                    double pes = num.parse(campPes.getText().trim()).doubleValue(); // Intentem convertir el text a double
                                    //Comprovem que el nom i la marca no siguin duplicats
                                    for (int i = 0; i < modelCotxe.getRowCount(); i++) {
                                        if (campNomIMarca.getText().equals(modelCotxe.getValueAt(i, 0))) {
                                            setExcepcio(new DAOException(2293));
                                            return;
                                        }
                                    }

                                    //Comprovem que le nom i marcar sigue correcte
                                    if(!campNomIMarca.getText().matches("^[a-zA-Z0-9 ]+$")){
                                        setExcepcio(new DAOException(2296));
                                    } else if (pes < 1 || pes > 100000) {
                                        setExcepcio(new DAOException(2291));
                                    } else {
                                        // Obtenim l'ID del cotxe seleccionat
                                        int selectedRow = taulaCotxe.getSelectedRow();
                                        if (selectedRow == -1) {
                                            JOptionPane.showMessageDialog(null, "Has de seleccionar un cotxe!!");
                                            return;
                                        }
                                        Cotxe al = (Cotxe) modelCotxe.getValueAt(selectedRow, 3);
                                        al.setNomIMarca(campNomIMarca.getText());
                                        al.setPes(pes);
                                        al.setNou(caixaEsNou.isSelected());

                                        // Modifiquem el cotxe a la BD
                                        dadesCotxe.update(al);

                                        //Actualitzem la taula amb el metode que ja tenim
                                        actualitzarTaulaCotxes();

                                        // Actualitzem la taula
                                        modelCotxe.setValueAt(campNomIMarca.getText(), selectedRow, 0);
                                        modelCotxe.setValueAt(pes, selectedRow, 1);
                                        modelCotxe.setValueAt(caixaEsNou.isSelected(), selectedRow, 2);
                                    }
                                } catch (ParseException ex) {
                                    setExcepcio(new DAOException(1722));
                                    campPes.setSelectionStart(0);
                                    campPes.setSelectionEnd(campPes.getText().length());
                                    campPes.requestFocus();
                                } catch (DAOException ex) {
                                    throw new RuntimeException(ex);
                                }
                            }
                        }
                    }
                }
        );

        //Boto borrar
        borrarButton.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        if (pestanyes.getSelectedIndex() == 0) { // Si estem a la pestanya de cotxes
                            // Obtenim l'ID del cotxe seleccionat
                            int selectedRow = taulaCotxe.getSelectedRow();
                            if (selectedRow == -1) {
                                setExcepcio(new DAOException(2294));
                                return;
                            }
                            Cotxe al = (Cotxe) modelCotxe.getValueAt(selectedRow, 3);

                            // Eliminem el cotxe de la BD
                            try {
                                dadesCotxe.delete(al);
                            } catch (DAOException ex) {
                                throw new RuntimeException(ex);
                            }

                            // Eliminem el cotxe de la taula
                            modelCotxe.removeRow(selectedRow);

                            //Actualitzem la taula amb el metode que ja tenim
                            actualitzarTaulaCotxes();

                        }
                    }
                }
        );





        taulaCotxe.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             *
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                //Obtenim el número de la fila seleccionada
                int filaSel = taulaCotxe.getSelectedRow();

                if (filaSel != -1) {        //Tenim una fila seleccionada
                    //Posem els valors de la fila seleccionada als camps respectius
                    campNomIModel.setText(modelCotxe.getValueAt(filaSel, 0).toString());
                    campPes.setText(modelCotxe.getValueAt(filaSel, 1).toString().replaceAll("\\.", ","));
                    caixaEsNou.setSelected((Boolean) modelCotxe.getValueAt(filaSel, 2));

                    //Activem la pestanya de la quantitat de cotxes seleccionat
                    view.getPestanyes().setEnabledAt(1, true);
                    view.getPestanyes().setTitleAt(1, "Quantitat de " + campNomIModel.getText());

                    ompliQuantitat((Cotxe) modelCotxe.getValueAt(filaSel, 3),modelQuantitat);
                } else {                  //Hem deseleccionat una fila
                    //Posem els camps de text en blanc
                    campNomIModel.setText("");
                    campPes.setText("");

                    //Desactivem pestanyes
                    view.getPestanyes().setEnabledAt(1, false);
                    view.getPestanyes().setTitleAt(1, "Quantitat de ...");
                }
            }
        });
    }



    /**
     * Mètode per actualitzar la taula de cotxes
     */
    //Creem un metode per actualitzar la taula de cotxes
    private void actualitzarTaulaCotxes() {
        DefaultTableModel modelCotxe = modelComponentsVisuals.getModelTaulaCotxe();
        try {
            modelCotxe.setRowCount(0);
            setModelTaulaCotxe(modelCotxe, dadesCotxe.getAll());
        } catch (DAOException e) {
            this.setExcepcio(e);
        }
    }

    private static void ompliQuantitat(Cotxe al, DefaultTableModel modelQuantitat) {
        //Omplim el model de la taula de quantitat de cotxes seleccionat
        modelQuantitat.setRowCount(0);
        // Fill the table model with data from the collection
        for (Cotxe.Quantitat quantitat: al.getQuantitat()) {
            modelQuantitat.addRow(new Object[]{quantitat.getProvincia(), quantitat.getQuantitat()});
        }
    }


    //TRACTAMENT D'EXCEPCIONS

    //2. Propietat lligada per controlar quan genero una excepció
    public static final String PROP_EXCEPCIO="excepcio";
    private DAOException excepcio;

    public DAOException getExcepcio() {
        return excepcio;
    }

    public void setExcepcio(DAOException excepcio) {
        DAOException valorVell=this.excepcio;
        this.excepcio = excepcio;
        canvis.firePropertyChange(PROP_EXCEPCIO, valorVell,excepcio);
    }


    //3. Propietat PropertyChangesupport necessària per poder controlar les propietats lligades
    PropertyChangeSupport canvis=new PropertyChangeSupport(this);


    //4. Mètode on posarem el codi de tractament de les excepcions --> generat per la interfície PropertyChangeListener
    /**
     * This method gets called when a bound property is changed.
     *
     * @param evt A PropertyChangeEvent object describing the event source
     *            and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        DAOException rebuda=(DAOException)evt.getNewValue();

        try {
            throw rebuda;
        } catch (DAOException e) {
            //Aquí farem ele tractament de les excepcions de l'aplicació
            switch(evt.getPropertyName()){
                case PROP_EXCEPCIO:

                    switch(rebuda.getTipo()){
                        case 0:
                        case 1:
                        case 2:
                        default:
                            JOptionPane.showMessageDialog(null, rebuda.getMessage());
                            break;
                    }


            }
        }
    }

}
