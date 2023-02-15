/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpers;

import annotation.Column;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.Time;
import java.util.Vector;

/**
 *
 * @author sarobidy
 */
public class Utilities {

    public static String toUpperFirst(String e) {
        return e.substring(0, 1).toUpperCase() + e.substring(1);
    }

    public static boolean checkIfDateAndTime(Object field) throws Exception {
        Class<?> fieldClass = field.getClass();
        boolean date = fieldClass.getSuperclass() == java.util.Date.class;
        boolean time = fieldClass == Time.class;
        return (date || time);
    }

    public static boolean checkIfDateAndTime(Field field) throws Exception {
        boolean date = field.getType() == java.util.Date.class;
        boolean date2 = field.getType() == java.sql.Date.class;
        boolean time = field.getType() == Time.class;
        return (date || date2 || time);
    }

    public static boolean checkIfBoolean(Field field) { // for the fields
        boolean bool = (field.getType().getSuperclass() == Boolean.class);
        boolean bools = (field.getType() == Boolean.TYPE);
        return (bool || bools);
    }

    public static boolean checkIfBoolean(Object object) { // for an object
        boolean bool = (object.getClass().getSuperclass() == Boolean.class);
        boolean bools = (object.getClass() == Boolean.TYPE);
        return (bool || bools);
    }

    public static boolean checkIfNumber(Object object) { // for an object
        boolean number = (object.getClass().getSuperclass() == Number.class);
        boolean numbers = (object.getClass() == Integer.TYPE || object.getClass() == Double.TYPE || object.getClass() == Float.TYPE);
        return (number || numbers);
    }

    public static boolean checkIfNumber(Field field) { // for the fields
        boolean number = (field.getType().getSuperclass() == Number.class);
        boolean numbers = (field.getType() == Integer.TYPE || field.getType() == Double.TYPE || field.getType() == Float.TYPE);
        return (number || numbers);
    }

    /**
     *
     * @param object the object to get the column annoted from database
     * @return the list of columns present in the class
     */
    public static Vector<String> getColumnsAnnoted(Object object) {
        Field[] fs = object.getClass().getDeclaredFields();
        Vector<String> cols = new Vector<>();
        for (Field f : fs) {
            if (f.isAnnotationPresent(Column.class)) {
                Column c = f.getAnnotation(Column.class);
                String col = (c.name().trim().isEmpty()) ? f.getName() : c.name();
                cols.add(col);
            }
        }
        return cols;
    }

    public static Vector<String> getColumnValues(Object object) throws Exception {
        Field[] fs = object.getClass().getDeclaredFields();
        Vector<String> v = new Vector<>();
        for (int i = 0; i < fs.length; i++) {
            if (fs[i].isAnnotationPresent(Column.class)) {
                Column c = fs[i].getAnnotation(Column.class);
                String prefix = (Utilities.checkIfBoolean(fs[i])) ? "is" : "get";
                Method m = object.getClass().getDeclaredMethod(prefix + Utilities.toUpperFirst(fs[i].getName()));
                Object o = m.invoke(object);
                if (c.isPrimary()) {
                    if (o.equals(0) && c.isSerial())
                        v.add("default");
                    else
                        v.add("'" + o + "'");
                } else 
                        v.add("'" + o + "'");
            }
        }
        return v;
    }

    public static String toValableSql(Vector<String> c , String delimiter) {
        String values = "";
        for (int i = 0; i < c.size(); i++) {
            values = values + c.get(i);
            values = values + ((i != c.size() - 1) ? delimiter : "");
        }
        return values;
    }

    public static String updateValues(Vector<String> cols, Vector<String> values , String delimiter) {
        String value = "";
        for (int i = 0; i < cols.size(); i++) {
            value = value + cols.get(i) + " = " + values.get(i);
            value = value + (( i != cols.size() - 1) ? delimiter : "");
        }
        return value;
    }

}
