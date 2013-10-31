package player;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class LCM {
    
    public List<Integer> denominators;
    
    /**
     * Initializes the list of denominators from Fraction, to determine the LCM.
     * @param denominators List<Integer>; denominators != null.
     */
    public LCM(List<Integer> denominators){
        this.denominators = denominators;
    }
    
    /**
     * Returns the LCM.
     */
    public int getLCM(){        
        while (denominators.size() > 1){
            int num1 = denominators.get(0); // convert denominator to a String
            int num2 = denominators.get(1);
            int lcm = getLCM2(num1, num2);
            denominators.remove(0);
            denominators.remove(0);
            denominators.add(lcm);
        }
        return denominators.get(0);
    }
    
    /**
     * Returns the LCM of two numbers - a sub-method of getLCM
     * @param a int;
     * @param b int;
     */
    public int getLCM2(int a, int b){
        return a * (b / getGCD(a, b));
    }
    
    /**
     * Returns the GCD - a sub-method of 
     * @param a int;
     * @param b int;
     */
    public int getGCD(int a, int b) {
        while (b > 0)
        {
            int temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }
    
}