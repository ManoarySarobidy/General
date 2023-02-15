/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package baseobject;

import helpers.Utilities;
import annotation.Column;
import annotation.Table;
import baseconnection.Connect;
import baseconnection.QueryStatement;
import baseconnection.Result;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.util.Vector;
import java.sql.ResultSet;
import java.sql.Time;

public class DataObject<T> {

    Field primary;
    Field[] fields;
    String pcol;
    
    Vector<String> columns;
    Vector<Field> secondField;

    public DataObject() {
        this.setFields();  // Take all the class fields 
        this.setSecondField();  // Instanciate a new Vector for storing fields
        this.setColumns(); // Set the column name from the database
    }

/// Select Operation
    public Vector<T> select() throws Exception {
        String table = String.valueOf(this.getClass().getAnnotation(Table.class).table());
        String sql = " Select * from " + table;
        return selectOperation(sql);
    }
    
    public Vector<T> select( String sql ) throws Exception {
        return selectOperation(sql);
    }

    public <T> Vector<T> selectOperation(String query) throws Exception {
        Vector<T> list = new Vector<>();
        QueryStatement statement = new QueryStatement(query);
        String database = ((this.getClass().isAnnotationPresent(Table.class)) ? this.getClass().getAnnotation(Table.class).database() : "postgres");
        try ( Connect c = new Connect( this.getClass().getAnnotation(Table.class).driver() , database)) {
            statement.setConnection(c);
            statement.executeQuery();
            Result r = statement.getResult();
            while (r.getResult().next()) 
                list.add( createObject(r) );
            r.getResult().close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally{
            statement.close();
        }
        return list;
    }
/// Select End

/// Insert Section
        public void insert(Connection connection) throws Exception {
        String table = this.getClass().getAnnotation(Table.class).table();
        String query = "Insert into " + table + " values ( ";
        // amboarina tsara ilay izy hoe atreto no alaina
        // afaka tenenina ihany koa ilay izy hoe ataovy serial na varchar ilay atao primary key
        // raha par rapport an'ilay type an'ilay objet no anaovana azy hoe serial na varchar avec prefix ilay izy
        // Ndao ary hoe atao fonction izany 
    }
        
        
     public String getPrimaryValuesInsert(){
         if( this.getPrimary() != null ){
             // anontaniana izy hoe string ve ianao 
               return "";
         }
         return "";
     }
        
    
    // Inona no atao amorona objet
    public <T> T createObject(Result r) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, Exception {
        T ob = (T) this.getClass().getConstructor().newInstance();
        if( this.getPrimary() != null ){
            Method s = this.getClass().getDeclaredMethod("set"+Utilities.toUpperFirst(this.getPrimary().getName()), this.getPrimary().getType());
           s.invoke(ob, getObject( this.getPrimary() , r.getResult() , this.getPcol() ) );
        }
        for ( int j = 0 ; j < this.getSecondField().size() ;  j++) {
            Field f = this.getSecondField().get(j);
            String prefix = (Utilities.checkIfBoolean(f)) ? "is" : "set";
            Method setter = this.getClass().getMethod(prefix + Utilities.toUpperFirst(f.getName()) , f.getType() );
            Object value = this.getObject(f, r.getResult(), this.getColumns().get(j));
            setter.invoke(ob, value);
        }
        return ob;
    }

    public Object getObject(Field f, ResultSet set, String colname) throws Exception {
        if (Utilities.checkIfNumber(f)) {
            return this.getNumber(f, set, colname);
        } else if (Utilities.checkIfBoolean(f)) {
            return set.getBoolean(colname);
        } else if (Utilities.checkIfDateAndTime(f)) {
            return this.getDateOrTime(f, set, colname);
        }
        return set.getString(colname);
    }

    public Object getNumber(Field field, ResultSet result, String index) throws Exception {
        if (field.getType() == Integer.TYPE) {
            return result.getInt(index);
        } else if (field.getType() == Double.TYPE) {
            return result.getDouble(index);
        }
        return null;
    }

    public Object getDateOrTime(Field field, ResultSet result, String index) throws Exception {
        if (field.getType() == java.util.Date.class) {
            return result.getDate(index);
        } else if (field.getType() == Time.class) {
            return result.getTime(index);
        }
        return null;
    }

// Getters and setters section
    public Field getPrimary() {
        return primary;
    }

    public void setPrimary(Field primary) {
        this.primary = primary;
    }

    public Field[] getFields() {
        return fields;
    }

    void setFields() {
        Field[] f = this.getClass().getDeclaredFields();
        this.fields = f;
    }

    public void setColumns() {
        Field[] fs = this.getFields();
        Vector<String> cols = new Vector<>();
        for (Field f : fs) {
            if (f.isAnnotationPresent(Column.class)) {
                Column c = f.getAnnotation(Column.class);
                String col = (c.name().trim().isEmpty()) ? f.getName() : c.name();
                if (c.isPrimary()) {
                    this.setPrimary(f);
                    this.setPcol(col);
                } else {
                    this.getSecondField().add(f);
                    cols.add(col);
                }
            }
        }
        this.setColumns(cols);
    }

    public void setColumns(Vector<String> cols) {
        this.columns = cols;
    }
    public Vector<String> getColumns() {
        return this.columns;
    }
    public String getPcol() {
        return pcol;
    }
    public void setPcol(String pcol) {
        this.pcol = pcol;
    }
    public Vector<Field> getSecondField() {
        return secondField;
    }
    public void setSecondField() {
        this.secondField = new Vector<Field>();
    }
}
