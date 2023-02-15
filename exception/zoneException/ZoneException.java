/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception.zoneException;

public class ZoneException extends Exception{
            /**
             * 
             * @param cause The cause of the exception 
             */
        public ZoneException(String cause){
            super("Désolé cette zone n'est pas valide : " + cause);
        }
}
