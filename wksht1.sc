object wksht1
{
  println("Welcome to the Scala worksheet")       //> Welcome to the Scala worksheet

  val phone = "5215539993531"                     //> phone  : String = 5215539993531
  phone.length                                    //> res0: Int = 13
  phone.substring(3, phone.length)                //> res1: String = 5539993531
  phone.substring(0, phone.length)                //> res2: String = 5215539993531
  phone.substring(0, phone.length - 1)            //> res3: String = 521553999353
  phone(0)                                        //> res4: Char = 5
  phone(1)                                        //> res5: Char = 2
  phone(12)                                       //> res6: Char = 1
}