/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc;

import annotation.Column;
import annotation.Table;


/**
 *
 * @author sarobidy
 */

@Table( table = "test" , database = "ticketing" )

public class TestAnotation {  
    @Column(isPrimary = true , isSerial = true)
    int id;
    @Column
    String nom;

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
   
    public TestAnotation(){
        
    }
    public TestAnotation(String nom){
         this.setNom(nom);
    }
    public TestAnotation(int id ,String nom){
         this.setNom(nom);
         this.setId(id);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Override
    public String toString(){
        String representation = " [ id : " + this.getId() + " , nom : " + this.getNom() +"]";
        return representation;
    }
    
    
}
