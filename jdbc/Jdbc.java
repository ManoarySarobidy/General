/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package jdbc;

import annotation.Column;
import baseobject.GenericDAO;
import helpers.Utilities;
import event.Event;
import java.lang.reflect.Field;


/**
 *
 * @author sarobidy
 */
public class Jdbc {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        // TODO code application logic here
        Event e = new Event();
        Jdbc j = new Jdbc();
        String sql = "Select * from test where id = 7" ;
        TestAnotation sarobidy = new TestAnotation(1," Test lc eeeeee ");
        GenericDAO.delete(sarobidy , null);
        
//        GenericDAO.save( new TestAnotation(11,"Manoary") , null );
        //GenericDAO.update(sarobidy, null);
    }
    
    

}
