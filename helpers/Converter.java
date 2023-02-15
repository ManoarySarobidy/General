/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helpers;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.Time;

/**
 *
 * @author sarobidy
 */
public class Converter {
    public static Object getObject(Field f, ResultSet set, String colname) throws Exception {
        if (Utilities.checkIfNumber(f)) {
            return Converter.getNumber(f, set, colname);
        } else if (Utilities.checkIfBoolean(f)) {
            return set.getBoolean(colname);
        } else if (Utilities.checkIfDateAndTime(f)) {
            return Converter.getDateOrTime(f, set, colname);
        }
        return set.getString(colname);
    }

    private static Object getNumber(Field field, ResultSet result, String index) throws Exception {
        if (field.getType() == Integer.TYPE) {
            return result.getInt(index);
        } else if (field.getType() == Double.TYPE) {
            return result.getDouble(index);
        }
        return null;
    }
    private static Object getDateOrTime(Field field, ResultSet result, String index) throws Exception {
        if (field.getType() == java.util.Date.class) {
            return result.getDate(index);
        } else if (field.getType() == Time.class) {
            return result.getTime(index);
        }
        return null;
    }  
}
