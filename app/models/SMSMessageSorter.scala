package models

object SMSMessageSorter 
{
     def isortMinOnTop(xs: List[SMSMessage]): List[SMSMessage] = xs match
     {
         case List() => List()
         case x :: xs1 => insertMinOnTop(x, isortMinOnTop(xs1))
      }                                                 

      def insertMinOnTop(x: SMSMessage, xs: List[SMSMessage]): List[SMSMessage] = xs match
      {
          case List() =>
            {
              List(x)
            }
          case y :: ys => 
            {           
              if (x.internalTweetCons <= y.internalTweetCons) x :: xs 
                      else y :: insertMinOnTop(x, ys)
            }
       }        
}