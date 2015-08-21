object wksht1
{;import org.scalaide.worksheet.runtime.library.WorksheetSupport._; def main(args: Array[String])=$execute{;$skip(59); 
  println("Welcome to the Scala worksheet");$skip(31); 

  val phone = "5215539993531";System.out.println("""phone  : String = """ + $show(phone ));$skip(15); val res$0 = 
  phone.length;System.out.println("""res0: Int = """ + $show(res$0));$skip(35); val res$1 = 
  phone.substring(3, phone.length);System.out.println("""res1: String = """ + $show(res$1));$skip(35); val res$2 = 
  phone.substring(0, phone.length);System.out.println("""res2: String = """ + $show(res$2));$skip(39); val res$3 = 
  phone.substring(0, phone.length - 1);System.out.println("""res3: String = """ + $show(res$3));$skip(11); val res$4 = 
  phone(0);System.out.println("""res4: Char = """ + $show(res$4));$skip(11); val res$5 = 
  phone(1);System.out.println("""res5: Char = """ + $show(res$5));$skip(12); val res$6 = 
  phone(12);System.out.println("""res6: Char = """ + $show(res$6))}
}
