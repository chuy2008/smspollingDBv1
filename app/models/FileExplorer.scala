package models
import java.util.Scanner
import java.util.regex._

object FileExplorer 
{
    def ConvertScannerToListOfStrings(scanner: Scanner, endTag: String):List[String] = 
    {      
                           
        def loopExtractLines(acc:List[String], p: Pattern):List[String] =
    	{     
                   if(scanner.hasNextLine())
                   {
                       var lineRead = scanner.nextLine()
                       if (lineRead.charAt(0) != '#')
                       {  
        		          var endMatch = p.matcher(lineRead)
			              if (!(endMatch.find()))  loopExtractLines(acc ::: List(lineRead), p) else acc 
                       }
                       else{loopExtractLines(acc, p)}
                    }
                   else   acc    		      
   	    }
        var endPatt = Pattern.compile(endTag)
        loopExtractLines(List(): List[String], endPatt)
    }
    
    def ExtractCustTerms(scanner: Scanner, customerName: String, endTag: String):List[String] = 
    {      
        def TermFound(p: Pattern): Boolean =
        {
            def loopTermFound(p: Pattern): Boolean =
            {
               if(scanner.hasNextLine())
               {
                  var lineRead = scanner.nextLine()
//                  println("pos 1 = " + lineRead)
		          var m = p.matcher(lineRead)
		          if (!(m.find())) loopTermFound(p) else true
               }
               else false         
           }
           loopTermFound(p)
       }
                           
        def loopExtractCustTerms(acc:List[String], p: Pattern):List[String] =
    	{     
                   if(scanner.hasNextLine())
                   {
                       var lineRead = scanner.nextLine()
        		       var endMatch = p.matcher(lineRead)
			           if (!(endMatch.find()))  loopExtractCustTerms(acc ::: List(lineRead), p) else acc 
                    }
                   else   acc    		      
   	    }
        var endPatt = Pattern.compile(endTag)
        if (TermFound(Pattern.compile(customerName))) loopExtractCustTerms(List(): List[String], endPatt) else List()
    } 
}