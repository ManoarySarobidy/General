/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package exception.confirmationException;

/**
 *
 * @author sarobidy
 */
public class InvalidConfirmationException extends Exception {
     
    public InvalidConfirmationException(String message) {
        super("Désolé cette Confirmation est invalide, cause : ".concat(message));
    }
}
