package org.example.model.entities;

import java.util.Collection;
import java.util.TreeSet;

/**
 * Classe que representa un cotxe
 */
public class Cotxe {

    private Long id;
    private String nomIMarca;
    private double pes;
    private boolean nou;
    private Collection<Quantitat> quantitat;


    /**
     * Constructor per defecte
     */
    public Cotxe(){}

    /**
     * Constructor amb paràmetres
     * @param nomIMarca nom i marca del cotxe
     * @param pes pes del cotxe
     * @param nou si el cotxe és nou o no
     * @param quantitat quantitat de cotxes per provincia
     */
    public Cotxe(String nomIMarca, double pes, boolean nou, Collection<Quantitat> quantitat) {
        this.nomIMarca = nomIMarca;
        this.pes= pes;
        this.nou=nou;
        this.quantitat= new TreeSet<>();
    }

    /**
     * Constructor amb paràmetres
     * @param id identificador del cotxe
     * @param nomIMarca nom i marca del cotxe
     */
    public Cotxe(Long id, String nomIMarca) {
        this.id = id;
        this.nomIMarca = nomIMarca;
    }

    /**
     * Constructor amb paràmetres
     * @param id identificador del cotxe
     * @param nomIMarca nom i marca del cotxe
     * @param pes pes del cotxe
     */
    public Cotxe(long id, String nomIMarca, double pes) {
        this.id = id;
        this.nomIMarca = nomIMarca;
        this.pes = pes;
    }

    /**
     * Constructor amb paràmetres
     * @param id identificador del cotxe
     * @param nomIMarca nom i marca del cotxe
     * @param pes pes del cotxe
     * @param esNou si el cotxe és nou o no
     * @param quantitat quantitat de cotxes per provincia
     */
    public Cotxe(long id, String nomIMarca, double pes, boolean esNou, TreeSet<Quantitat> quantitat) {
        this.id = id;
        this.nomIMarca = nomIMarca;
        this.pes = pes;
        this.nou = esNou;
        this.quantitat = quantitat;
    }


    /**
     * Agafa la collecció de quantitats
     * @return la collecció de quantitats
     */
    public Collection<Quantitat> getQuantitat() {
        return quantitat;
    }

    private void setQuantitat(Collection<Quantitat> quantitat) {
        this.quantitat = quantitat;
    }


    public Long getId() {
        return id;
    }

    public String getNomIMarca() {
        return nomIMarca;
    }

    public void setNomIMarca(String nomIMarca) {
        this.nomIMarca = nomIMarca;
    }

    public double getPes() {
        return pes;
    }

    public void setPes(double pes) {
        this.pes = pes;
    }

    public boolean isNou() {
        return nou;
    }

    public void setNou(boolean nou) {
        this.nou = nou;
    }



    /**
     * Classe que representa la quantitat de cotxes per provincia
     */
    public static class Quantitat implements Comparable<Quantitat>{


        /**
         * Compara dos quantitats per provincia
         * @param o la quantitat a comparar
         * @return 0 si són iguals, -1 si la quantitat és més petita, 1 si la quantitat és més gran
         */
        @Override
        public int compareTo(Quantitat o) {
            return this.provincia.compareTo(o.getProvincia());
        }

        /**
         * Enumeració de les províncies
         */
        public static enum Provincia {
            //provincies d'espanya
            BARCELONA, GIRONA, LLEIDA, TARRAGONA, MADRID, VALENCIA, ALICANTE, CASTELLO, MURCIA, ALBACETE, CUENCA, GUADALAJARA, TOLEDO, AVILA, BURGOS, LEON, PALENCIA, SALAMANCA, SEGOVIA, SORIA, VALLADOLID, ZAMORA, ALAVA, BIZKAIA, GIPUZKOA, ARABA, NAVARRA, LA_RIOJA, HUESCA, TERUEL, ZARAGOZA, ASTURIAS, CANTABRIA, ALMERIA, CADIZ, CORDOBA, GRANADA, HUELVA, JAEN, MALAGA, SEVILLA, BADAJOZ, CACERES, A_CORUNA, LUGO, OURENSE, PONTEVEDRA, ILLES_BALEARS, LAS_PALMAS, SANTA_CRUZ_DE_TENERIFE, CEUTA, MELILLA, OSCA, LLEO
        }


        private Provincia provincia;
        private int quantitat;


        /**
         * Constructor amb paràmetres
         * @param provincia la provincia
         * @param quantitat la quantitat de cotxes
         */
        public Quantitat(Provincia provincia, int quantitat) {
            this.provincia = provincia;
            this.quantitat = quantitat;
        }

        public Provincia getProvincia() {
            return provincia;
        }

        public void setProvincia(Provincia provincia) {
            this.provincia = provincia;
        }

        public int getQuantitat() {
            return quantitat;
        }

        public void setQuantitat(int quantitat) {
            this.quantitat = quantitat;
        }
    }


}

