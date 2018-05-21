/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.util;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 *
 * @author Boss
 */
public class EmailUtil {

//	public static void main(String[] args) {
//		EmailUtil crunchifyCheck = new EmailUtil();
// 
//		// Specify Boolean Flag
//		boolean isValid ;
// 
//		String email = "hello@hamza.com";
//		isValid = crunchifyCheck.emailValidate(email);
// 
//		email = "hello.hamza";
//		isValid = crunchifyCheck.emailValidate(email);
// 
//		email = "hello.hamza@";
//		isValid = crunchifyCheck.emailValidate(email);
//	}
//                
    public static boolean emailValidate(String email) {
        boolean isValid = false;
        try {
            //
            // Create InternetAddress object and validated the supplied
            // address which is this case is an email address.
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
        } catch (AddressException e) {
            System.out.println(email + " is " + (isValid ? "a" : "not a") + " valid email address\n");
        }
        return isValid;
    }

}
