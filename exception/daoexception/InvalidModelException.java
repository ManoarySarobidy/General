/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception.daoexception;

/**
 *
 * @author sarobidy
 */
public class InvalidModelException  extends Exception {
        public InvalidModelException(String message){
            super( " InvalidModelException : " + message );
        }
}
