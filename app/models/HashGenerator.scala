package models

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

object HashGenerator 
{
    def generateEncryptedPwd(passwordToHash: String): String =
    {
/*
        def getSalt(): String =
        {
           var sr = SecureRandom.getInstance("SHA1PRNG");
           var saltt: Array[Byte] = Array(0)
           sr.nextBytes(saltt);
           saltt.toString();
        } 
 *                                             
 */                                            //> getSalt: ()String
        def get_SHA_1_SecurePassword(passwordToHash: String, salt: String): String =
        {
           var generatedPassword = ""
             var md: MessageDigest = MessageDigest.getInstance("SHA-1");
             md.update(salt.getBytes());
             var bytes = md.digest(passwordToHash.getBytes());
             var sb = new StringBuilder()
             for(i <- 0 to bytes.length - 1) sb.append(Integer.toString((bytes(i) & 0xff) + 0x100, 16).substring(1))
             generatedPassword = sb.toString
             generatedPassword;
        }                                             
//        var salt = getSalt                                
        var salt = "[B@37d62d02"
        get_SHA_1_SecurePassword(passwordToHash, salt);                                              
    }
    //Add salt    
}