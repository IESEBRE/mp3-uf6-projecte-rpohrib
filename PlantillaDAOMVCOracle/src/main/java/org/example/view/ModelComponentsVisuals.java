package org.example.view;

import org.example.model.entities.Cotxe;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ModelComponentsVisuals {

    private DefaultTableModel modelTaulaCotxe;
    private DefaultTableModel modelTaulaQuantitat;
    private ComboBoxModel<Cotxe.Quantitat.Provincia> comboBoxModel;

    //Getters


    public ComboBoxModel<Cotxe.Quantitat.Provincia> getComboBoxModel() {
        return comboBoxModel;
    }

    public DefaultTableModel getModelTaulaCotxe() {
        return modelTaulaCotxe;
    }

    public DefaultTableModel getModelTaulaQuantitat() {
        return modelTaulaQuantitat;
    }

    public ModelComponentsVisuals() {


        //Anem a definir l'estructura de la taula dels cotxes
        modelTaulaCotxe =new DefaultTableModel(new Object[]{"Nom i Marca","Pes","És nou?","Object"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }



            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return String.class;
                    case 1:
                        return Double.class;
                    case 2:
                        return Boolean.class;
                    default:
                        return Object.class;
                }
            }
        };




        //Anem a definir l'estructura de la taula de les quantitats
        modelTaulaQuantitat =new DefaultTableModel(new Object[]{"Província","Quantitat"},0){
            /**
             * Returns true regardless of parameter values.
             *
             * @param row    the row whose value is to be queried
             * @param column the column whose value is to be queried
             * @return true
             * @see #setValueAt
             */
            @Override
            public boolean isCellEditable(int row, int column) {

                //Fem que TOTES les cel·les de la columna 1 de la taula es puguen editar
                //if(column==1) return true;
                return false;
            }

            //Permet definir el tipo de cada columna
            @Override
            public Class getColumnClass(int column) {
                switch (column) {
                    case 0:
                        return Cotxe.Quantitat.Provincia.class;
                    case 1:
                        return Integer.class;
                    default:
                        return Object.class;
                }
            }
        };



        //Estructura del comboBox
        comboBoxModel=new DefaultComboBoxModel<>(Cotxe.Quantitat.Provincia.values());



    }
}
