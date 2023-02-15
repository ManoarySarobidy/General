/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/AnnotationType.java to edit this template
 */
package annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author sarobidy
 *  Created for a database object generalized
 */

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)

public @interface Column {
    
    String name() default "";
    boolean isPrimary() default false;
    boolean isForeignKey() default false;
    boolean isSerial() default false;
    String prefix() default "";
    // Inona ihany koa no ilain'ny colone
    // Mila ny Type id izy
}
