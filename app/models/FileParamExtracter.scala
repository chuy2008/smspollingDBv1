package models
import java.io.File
import java.io.FileInputStream
import java.util.regex._
import java.util.Scanner

object FileParamExtracter 
{
/*
 * 
 *
  def ConvertFileToScanner(inputParameterFile: String): Scanner = 
          new Scanner(new FileInputStream(new File(inputParameterFile)))
  
    def ConvertScannerToListOfStrings(scanner: Scanner, endTag: String):List[String] = 
    {                      
        def loopExtractLines(acc:List[String], p: Pattern):List[String] =
    	{     
                   if(scanner.hasNextLine())
                   {
                       var lineRead = scanner.nextLine()
        		       var endMatch = p.matcher(lineRead)
			           if (!(endMatch.find()))  loopExtractLines(acc ::: List(lineRead), p) else acc 
                    }
                   else   acc    		      
   	    }
        var endPatt = Pattern.compile(endTag)
        loopExtractLines(List(): List[String], endPatt)
    }  
  *      
  */    

    def EstablishField(inputListOfStrings: List[String], fieldName: String): String = 
    {
        FindInputString(inputListOfStrings, fieldName)
    }

    
    def FindInputString(LofS: List[String], str1: String): String =
	{
	    var str3 =""
		for(i <- LofS)
		{
		    if (DebugVariables.debugM4){println("point 20 - FindInputString, FileParamExtracter.....")}
		    if (DebugVariables.debugM4){println("first iteration in LofS = " + i)}
			var p = Pattern.compile(str1);
			var m = p.matcher(i);
			if (m.find())
			{
			    if (DebugVariables.debugM4){println("point 21 - FoundInputString, FileParamExtracter.....")}
				str3 = find_field_to_search(i);
			}
		}
		str3
	}
          
	def find_field_to_search(str: String): String =
	{
		var n = str.indexOf('"')
		if (DebugVariables.debugM4){println("point 22 - location of first quote = " + n )}
		var m = findlastquote(str)
		if (DebugVariables.debugM4){println("point 23 - location of last quote = " + m)}
		str.substring((n + 1), m)
	}      
       
	def findlastquote(str: String): Int = 
	{
		var n = str.length() - 1
		while (str.charAt(n) != '"')
		{
			n = n - 1
		}
		n
	}     
}