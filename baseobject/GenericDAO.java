package baseobject;

import helpers.Utilities;
import annotation.Column;
import annotation.Table;
import baseconnection.Connect;
import exception.daoexception.InvalidModelException;
import exception.daoexception.SqlQueryException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Vector;
import helpers.Converter;

/**
 *
 * @author sarobidy
 * @param <T>
 */
public class GenericDAO<T> {

    /**
     *
     * @param <T>
     * @param object an instance of the object that will act as a model for the
     * database data
     * @param c the connection to access the database
     * @return
     * @throws InvalidModelException if the @param object is null
     * @throws java.sql.SQLException
     *
     */
    public static <T> Vector<T> getAll(Object object, Connection c) throws InvalidModelException, SQLException, Exception {
        if (object == null) {
            throw new InvalidModelException(" The object parameter is null ");
        }
        if (!object.getClass().isAnnotationPresent(Table.class)) {
            throw new InvalidModelException("The object : " + object.getClass().getSimpleName() + " is not a model from the database");
        }
        Table table = object.getClass().getAnnotation(Table.class); 
        String query = "Select * from " + table.table();
        Vector<T> result = (Vector<T>) get( query , object , c );
        return result;
    }

    private static <T> Vector<T> get(String query, Object o, Connection c) throws Exception {
        boolean wasOpen = (c != null);
        Connect connect = new Connect(o.getClass().getAnnotation(Table.class).driver(), o.getClass().getAnnotation(Table.class).database());
        try {
            c = (wasOpen) ? c : connect.getConnection();
            Statement statement = c.createStatement();
            ResultSet result = statement.executeQuery(query);
            return (Vector<T>) create(c, result, o);
        } catch (Exception e) {
            e.printStackTrace();
            return new Vector<T>();
        } finally {
            if (!wasOpen) {
                try {
                    c.close();
                    connect.close();
                } catch (Exception e) {
                }
            }
        }
    }
    private static <T> Vector<T> create(Connection con, ResultSet result, Object object) throws Exception {
        Vector<String> columns = Utilities.getColumnsAnnoted(object);   // Get the column mapping from database
        Vector<T> lists = new Vector<>();
        try {
            while (result.next()) {
                lists.add(createObject((Class<T>) object.getClass(), result, columns));
            }
            return lists;
        } catch (Exception e) {
            throw e;
        }
    }

    private static <T> T createObject(Class<T> instance, ResultSet result, Vector<String> cols) throws Exception {
        T ob = (T) instance.getConstructor().newInstance();
        try {
            for (int j = 0; j < instance.getDeclaredFields().length; j++) {
                Field f = instance.getDeclaredFields()[j];
                if (f.isAnnotationPresent(Column.class)) {
                    String prefix = "set";
                    Method setter = instance.getMethod(prefix + Utilities.toUpperFirst(f.getName()), f.getType());
                    Object value = Converter.getObject(f, result, cols.get(j));
                    setter.invoke(ob, value);
                }
            }
        } catch (Exception e) {
            throw e;
        }
        return ob;
    }

    public static void save(Object object, Connection c) throws InvalidModelException, SQLException, Exception {
        if (object == null) {
            throw new InvalidModelException(" The object parameter is null ");
        }
        if (!object.getClass().isAnnotationPresent(Table.class)) {
            throw new InvalidModelException("The object : " + object.getClass().getSimpleName() + " is not a model from the database"); 
        }
        Table table = object.getClass().getAnnotation(Table.class);
        Vector<String> columns = Utilities.getColumnsAnnoted(object);
        Connect connect = new Connect(table.driver(), table.database());
         String values = Utilities.toValableSql(Utilities.getColumnValues(object) , ",");
         String column = Utilities.toValableSql(columns , ",");
         String insert = "Insert into " + table.table() + "(" + column + ") values ( " + values + ")";
         GenericDAO.execute(insert, object, c);
        
    }

    public static void update(Object object, Connection c) throws InvalidModelException, SQLException, Exception {
        if (object == null) {
            throw new InvalidModelException(" The object parameter is null ");
        }
        if (!object.getClass().isAnnotationPresent(Table.class)) {
            throw new InvalidModelException("The object : " + object.getClass().getSimpleName() + " is not a model from the database");
        }
        Table table = object.getClass().getAnnotation(Table.class);
        Vector<String> columns = Utilities.getColumnsAnnoted(object);
         Vector<String> values = Utilities.getColumnValues(object);
         String u = Utilities.updateValues(columns, values ,",");
         String update = "Update " + table.table() + " set " + u + " where " + GenericDAO.getPrimaryValues(object);
        GenericDAO.execute(update, object, c);
    }

    public static <T> Vector<T> findBySQL(Object object, String sql, Connection con) throws SqlQueryException, InvalidModelException, Exception {
        if( sql == null || sql.trim().equals("") ){
            throw new SqlQueryException(" Invalid query");
        }
        if( object == null ){
            throw new InvalidModelException( "The object parameter is null" );
        }
        if (!object.getClass().isAnnotationPresent(Table.class)) {
            throw new InvalidModelException("The object : " + object.getClass().getSimpleName() + " is not a model from the database");  // Check if it's a model from the database
        }
        Vector<T> results = GenericDAO.get(sql, object, con);
        return results;
    }
    
    public static <T> T getObject( Object object , Connection c ) throws Exception{
        if( object == null ){
            throw new InvalidModelException("The object parameter is null");
        }
        if (!object.getClass().isAnnotationPresent(Table.class)) {
            throw new InvalidModelException("The object : " + object.getClass().getSimpleName() + " is not a model from the database");  // Check if it's a model from the database
        }
        Table table = object.getClass().getDeclaredAnnotation(Table.class);
        String sql = "Select * from " + table.table() + " where 1=1 and " + Utilities.updateValues(Utilities.getColumnsAnnoted(object), Utilities.getColumnValues(object), " and ");
        T t = (T) GenericDAO.get(sql, object, c).get(0);
        return t;
        // Amboarina ny vecteur valiny
    }
    
    public static <T> T findById(Object object , Connection c) throws Exception{
        Table table = object.getClass().getAnnotation(Table.class);
        String sql = "Select * from " + table.table() + " where " + GenericDAO.getPrimaryValues(object);
        System.out.println(sql);
        T t = (T) GenericDAO.get(sql, object, c).get(0);
        return t;
    }
    
    public static String getPrimaryValues( Object object ) throws Exception{
        Field[] fields = object.getClass().getDeclaredFields();
        String primary = "";
        for( Field f : fields ){
            if( f.isAnnotationPresent(Column.class) ){
                Column c = f.getAnnotation(Column.class);
                if( c.isPrimary() ){
                    try{
                        Method p = object.getClass().getDeclaredMethod("get" + Utilities.toUpperFirst(f.getName()) , (Class<?>[]) null);
                        primary = ((c.name().trim().equals("")) ? f.getName() : c.name()) + " = '" + String.valueOf(p.invoke( object )) +"'";
                    }catch(IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException e){
                        e.printStackTrace();
                    }
                }
            }
        }
        if( primary.trim().equals("") ){
            throw new Exception("The primary field doesn't exist");
        }
        return primary;
    }
    
    public  static void delete(Object object , Connection c) throws Exception {
        if( object == null ){
            throw new InvalidModelException("The object parameter is null");
        }
        if (!object.getClass().isAnnotationPresent(Table.class)) {
            throw new InvalidModelException("The object : " + object.getClass().getSimpleName() + " is not a model from the database");  // Check if it's a model from the database
        }
        Table table = object.getClass().getAnnotation(Table.class);
        String sql = "delete from " + table.table() + " where " + GenericDAO.getPrimaryValues(object);
        GenericDAO.execute( sql , object , c );
    }
    
    private static void execute(String sql ,Object object ,Connection c) throws SQLException{
        boolean wasOpen = ( c != null ); 
        Table table = object.getClass().getAnnotation(Table.class);
        Connect connect = new Connect(table.driver(), table.database());
        try{
            c = (wasOpen) ? c : connect.getConnection();
            System.out.println(sql);
            c.createStatement().executeUpdate( sql );
            c.commit();
        }catch(Exception e){
            c.rollback();
            e.printStackTrace();
        }finally{
            try{
                if( !wasOpen ) c.close();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }
    
}
