using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Program_cs
{
    internal class Program
    {
        static void Main(string[] args)
        {
            // variable in c# : container to store information
            string characterName = "John";
            int characterAge;
            characterAge = 35;

            // Data types
            // Plein text
            string phrase = "Giraffe Academy";
            char grade = 'C';
            // Number 
            int age = 34;
            // decimal number
            float fl = 3;
            double db = 3.0;
            decimal dc = 3; // more accurate 
            // boolean
            bool isMale = true;

            Console.WriteLine("     /|");
            Console.WriteLine("    / |");
            Console.WriteLine("   /  |");
            Console.WriteLine("  /   |");
            Console.WriteLine(" /    |");
            Console.WriteLine("/_____|");

            Console.WriteLine("There once was a man named " + characterName);
            Console.WriteLine("He was age " + characterAge + " years old");
            Console.WriteLine("He really liked the name " + characterName);
            Console.WriteLine("But didn't like being " + characterAge);

            // Working with String
            Console.WriteLine("Giraffe \"Academy\"");

            // Calculator
            Console.Write("Enter a number:");
            double num = Convert.ToDouble(Console.ReadLine());
            Console.Write("Enter another number:");
            double num2 = Convert.ToDouble(Console.ReadLine());
            Calculator.addition(num, num2);
    
            Console.ReadLine();

            //Array 
            int[] luckyNumbers = { 4, 8, 15, 16, 23, 42 };
            Console.WriteLine(luckyNumbers[0]);
        }
    }

    // create methods
    if
}
