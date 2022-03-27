object Basics  {
    def main(args: Array[String]) = {
        //defining a value
        val meaningOfLife: Int = 42 // reasignement not allow
        val aBoolean = false // type inference , add by compiler

        // all the type Int, Boolean, Char, Double, Float, Strong
        // String pretting specail
        val aString =  "I love Scala" 
        val aComposedString = "I" + "" + "love" + " " + "Scala" // concat string
        val anInterpolatedString = s"The meaning of life is $meaningOfLife"

        // Expression = Struture thant can be reduced to a value
        val anExpression = 2 + 3

        //if - expression
        val ifExpression = if (meaningOfLife>4) 46 else 999
        val chainedIfExpression  = 
            if (meaningOfLife>43) 56
            else if(meaningOfLife<0) -2
            else if(meaningOfLife>999) 78
            else 0
        
        //code blocks
        val aCodeBlock = {
            //Definition
            val aLocalValue = 67

            //valiue of the block is the value of the last expresion
            aLocalValue + 5
        }

        //define a function
        def myFunction(x: Int, y: String): String  = {
            y + " " + x
        }

        //recursive functions
        def factoriel(n: Int): Int = 
            if(n<=1) 1
            else n * factoriel(n-1)

        // in scala we don't use loops or iteratio, we use RECURSION

        // unit return type  = no meaninngful value==="void" in other language
        println("I love Scala")

        def myUnitReturningFunction(): Unit = {
            println("I Love SCala")
        }
    }
    
}