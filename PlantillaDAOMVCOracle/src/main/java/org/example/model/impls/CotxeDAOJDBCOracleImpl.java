package org.example.model.impls;

import org.example.model.daos.DAO;
import org.example.model.entities.Cotxe;
import org.example.model.exceptions.DAOException;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeSet;

/**
 * Implementació de la interfície DAO per a l'entitat Cotxe
 */
public class CotxeDAOJDBCOracleImpl implements DAO<Cotxe> {

    /**
     * Implementació del mètode get de la interfície DAO per a l'entitat Cotxe usant JDBC
     */
    static {
        try {
            createTables();
        } catch (DAOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Aquest mètode retorna un cotxe a partir del seu identificador
     * @param id identificador del cotxe
     * @return el cotxe amb l'identificador passat per paràmetre
     * @throws DAOException si es produeix un error en l'accés a la base de dades
     * */
    @Override
    public Cotxe get(Long id) throws DAOException {
        ResourceBundle rb = ResourceBundle.getBundle("database");

        String url = rb.getString("DB_PATH");
        String user = rb.getString("DB_USER");
        String password = rb.getString("DB_PASSWORD");
        String driver = rb.getString("driver");

        //Declaració de variables del mètode
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        Cotxe cotxe = null;

        //Accés a la BD usant l'API JDBC
        try {

            Class.forName(driver);

            con = DriverManager.getConnection(
                    url,
                    user,
                    password
            );
            st = con.createStatement();

            rs = st.executeQuery("SELECT * FROM COTXES");


            TreeSet<Cotxe.Quantitat> quantitats = new TreeSet<Cotxe.Quantitat>();

            if (rs.next()) {
                cotxe = new Cotxe(Long.valueOf(rs.getString(1)), rs.getString(2), rs.getDouble(3), rs.getBoolean(4), quantitats);
            }
        } catch (SQLException e) {
            throw new DAOException(1);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(0);
            }

        }
        return cotxe;
    }

    /**
     * Aquest mètode retorna tots els cotxes de la base de dades
     * @return una llista amb tots els cotxes de la base de dades
     * @throws DAOException si es produeix un error en l'accés a la base de dades
     */
    @Override
    public List<Cotxe> getAll() throws DAOException {
        ResourceBundle rb = ResourceBundle.getBundle("database");

        String url = rb.getString("DB_PATH");
        String user = rb.getString("DB_USER");
        String password = rb.getString("DB_PASSWORD");

        //Declaració de variables del mètode
        List<Cotxe> cotxes = new ArrayList<>();

        //Accés a la BD usant l'API JDBC
        try (Connection con = DriverManager.getConnection(
                url,
                user,
                password
        );
             PreparedStatement st = con.prepareStatement("SELECT * FROM COTXES");
             ResultSet rs = st.executeQuery();
        ) {

            TreeSet<Cotxe.Quantitat> quantitats = new TreeSet<Cotxe.Quantitat>();

            while (rs.next()) {
                cotxes.add(new Cotxe(rs.getLong("id"), rs.getString("marca_i_model"),rs.getDouble("pes"), rs.getBoolean("es_nou"),
                        quantitats));
            }
        } catch (SQLException throwables) {
            int tipoError = throwables.getErrorCode();
            //System.out.println(tipoError+" "+throwables.getMessage());
            switch(throwables.getErrorCode()){
                case 17002: //l'he obtingut posant un sout en el throwables.getErrorCode()
                    tipoError = 0;
                    break;
                default:
                    tipoError = 1;  //error desconegut
            }
            throw new DAOException(tipoError);
        }


        return cotxes;
    }

    @Override
    public void save(Cotxe obj) throws DAOException {

    }

    /**
     * Inserta un cotxe a la base de dades
     * @param cotxe el cotxe a inserir
     * @throws DAOException si es produeix un error en l'accés a la base de dades
     * */
    //Creem el metode per inserir un cotxe
    public void insert(Cotxe cotxe) throws DAOException {
        ResourceBundle rb = ResourceBundle.getBundle("database");

        String url = rb.getString("DB_PATH");
        String user = rb.getString("DB_USER");
        String password = rb.getString("DB_PASSWORD");
        String driver = rb.getString("driver");


        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;

        //Accés a la BD usant l'API JDBC
        try {

            //Carreguem el driver
            Class.forName(driver);

            con = DriverManager.getConnection(
                    url,
                    user,
                    password
            );
            st = con.prepareStatement("INSERT INTO COTXES (MARCA_I_MODEL, PES, ES_NOU) VALUES (?, ?, ?)");
            st.setString(1, cotxe.getNomIMarca());
            st.setDouble(2, cotxe.getPes());
            st.setBoolean(3, cotxe.isNou());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(1);
        } catch (ClassNotFoundException e) {
            throw new DAOException(2295);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);

            }

        }
    }

    /**
     * Actualitza un cotxe a la base de dades
     * @param cotxe el cotxe a actualitzar
     * @throws DAOException si es produeix un error en l'accés a la base de dades
     * */
    //Creem el metode per actualitzar un cotxe
    public void update(Cotxe cotxe) throws DAOException {
        ResourceBundle rb = ResourceBundle.getBundle("database");

        String url = rb.getString("DB_PATH");
        String user = rb.getString("DB_USER");
        String password = rb.getString("DB_PASSWORD");
        String driver = rb.getString("driver");

        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;

        //Accés a la BD usant l'API JDBC
        try {
            Class.forName(driver);

            con = DriverManager.getConnection(
                    url,
                    user,
                    password
            );
            st = con.prepareStatement("UPDATE COTXES SET MARCA_I_MODEL=?, PES=?, ES_NOU=? WHERE id=?");
            st.setString(1, cotxe.getNomIMarca());
            st.setDouble(2, cotxe.getPes());
            st.setBoolean(3, cotxe.isNou());
            st.setLong(4, cotxe.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(1);
        } catch (ClassNotFoundException e) {
            throw new DAOException(2295);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new DAOException(1);
            }

        }
    }

    /**
     * Esborra un cotxe de la base de dades
     * @param cotxe el cotxe a esborrar
     * @throws DAOException si es produeix un error en l'accés a la base de dades
     * */
    //Creem el metode per esborrar un cotxe
    public void delete(Cotxe cotxe) throws DAOException {
        ResourceBundle rb = ResourceBundle.getBundle("database");

        String url = rb.getString("DB_PATH");
        String user = rb.getString("DB_USER");
        String password = rb.getString("DB_PASSWORD");
        String driver = rb.getString("driver");

        //Declaració de variables del mètode
        Connection con = null;
        PreparedStatement st = null;

        //Accés a la BD usant l'API JDBC
        try {
            Class.forName(driver);

            con = DriverManager.getConnection(
                    url,
                    user,
                    password
            );
            st = con.prepareStatement("DELETE FROM COTXES WHERE id=?");
            st.setLong(1, cotxe.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(1);
        } catch (ClassNotFoundException e) {
            throw new DAOException(2295);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {

                throw new DAOException(1);
            }

        }
    }

    /**
     * Crea les taules i el trigger a la base de dades si no existeixen
     * @throws DAOException si es produeix un error en l'accés a la base de dades
     */
    //Metode per crear les taules i el trigger si no ho tenim a la base de dades
    public static void createTables() throws DAOException {
        ResourceBundle rb = ResourceBundle.getBundle("database");

        String url = rb.getString("DB_PATH");
        String user = rb.getString("DB_USER");
        String password = rb.getString("DB_PASSWORD");
        String driver = rb.getString("driver");

        //Declaració de variables del mètode
        Connection con = null;
        Statement st = null;

        //Accés a la BD usant l'API JDBC
        try {
            Class.forName(driver);

            con = DriverManager.getConnection(
                    url,
                    user,
                    password
            );
            st = con.createStatement();

            // Llegeix el contingut del fitxer pl.sql des del directori de recursos
            String script = readScript("/pl.sql");

            // Divideix el script en sentències individuals si és necessari
            String[] sqlStatements = script.split("/");

            // Executa cada sentència SQL
            for (String sql : sqlStatements) {
                if (sql.trim().length() > 0) {
                    st.execute(sql);
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + " " + e.getMessage());
            e.printStackTrace();
            throw new DAOException(1);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new DAOException(2295);
        } catch (IOException e) {
            e.printStackTrace();
            throw new DAOException(1);
        } finally {
            try {
                if (st != null) st.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new DAOException(1);
            }
        }
    }

    /**
     * Llegeix el contingut d'un fitxer i el retorna com una cadena
     * @param resourcePath la ruta del fitxer
     * @return el contingut del fitxer com una cadena
     * @throws IOException si es produeix un error en la lectura del fitxer
     * */
    // Mètode per llegir el contingut d'un fitxer i retornar-lo com una cadena
    private static String readScript(String resourcePath) throws IOException {
        StringBuilder script = new StringBuilder();
        // Llegeix el fitxer línia per línia
        try (InputStream inputStream = CotxeDAOJDBCOracleImpl.class.getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;

            // Llegeix cada línia del fitxer i l'afegeix a la cadena de text
            while ((line = reader.readLine()) != null) {
                script.append(line).append("\n");
            }
        }
        return script.toString();
    }

}
