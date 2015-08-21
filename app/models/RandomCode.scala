package models

import java.security.SecureRandom

object RandomCode 
{
   val numb = Array("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
  
   def generateRandomCode(length: Int): String = 
   {
      val rand = new SecureRandom
      val accumList = for (i <- 1 to length) yield numb(rand.nextInt(numb.length))
      accumList.foldLeft("")(_++_)  
  }
  
}