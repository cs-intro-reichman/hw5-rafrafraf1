/**
 * A library of string functions.
 */
public class MyString {
    public static void main(String args[]) {
        String hello = "hello";
        System.out.println(countChar(hello, 'h'));
        System.out.println(countChar(hello, 'l'));
        System.out.println(countChar(hello, 'z'));
        System.out.println(spacedString(hello));

        System.out.println(subsetOf("sap","space")); // returns true
        System.out.println(subsetOf("spa","space")); // returns true
        System.out.println(subsetOf("pass","space")); // returns false
        System.out.println(subsetOf("c","space")); // returns true

        System.out.println(randomStringOfLetters(5));

        System.out.println(remove("committee","meet")); // returns "comit" 
    }

    /**
     * Returns the number of times the given character appears in the given string.
     * Example: countChar("Center",'e') returns 2 and countChar("Center",'c') returns 0. 
     * 
     * @param str - a string
     * @param c - a character
     * @return the number of times c appears in str
     */
    public static int countChar(String str, char ch) {
        if (!(str instanceof String)) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            count += (str.charAt(i) == ch ? 1 : 0);
        }
        return count;
    }

    /** Returns true if str1 is a subset string str2, false otherwise
     *  Examples:
     *  subsetOf("sap","space") returns true
     *  subsetOf("spa","space") returns true
     *  subsetOf("pass","space") returns false
     *  subsetOf("c","space") returns true
     *
     * @param str1 - a string
     * @param str2 - a string
     * @return true is str1 is a subset of str2, false otherwise
     */
    public static boolean subsetOf(String str1, String str2) {
        for (int j = 0; j < str1.length(); j++) {
            int curr = str2.indexOf(str1.charAt(j));
            if (curr == -1) {
                return false;
            }
            str2 = str2.substring(0,curr) + str2.substring(curr + 1);
        }
        return true;
    }

    /** Returns a string which is the same as the given string, with a space
     * character inserted after each character in the given string, except
     * for the last character. 
     * Example: spacedString("silent") returns "s i l e n t"
     * 
     * @param str - a string
     * @return a string consisting of the characters of str, separated by spaces.
     */
    public static String spacedString(String str) {
        for (int i = 1; i < str.length(); i++) {
            str = str.substring(0,i) + ' ' + str.substring(i);
            i++;
        }
        return str;
    }
  
    /**
     * Returns a string of n lowercase letters, selected randomly from 
     * the English alphabet 'a', 'b', 'c', ..., 'z'. Note that the same
     * letter can be selected more than once.
     * 
     * Example: randomStringOfLetters(3) can return "zoo"
     * 
     * @param n - the number of letter to select
     * @return a randomly generated string, consisting of 'n' lowercase letters
     */
    public static String randomStringOfLetters(int n) {
        String randStr = "";
        for (int i = 0; i < n; i++) {
            // get random num between 122 and 97 including both. (unicode value range for lowercase alphabet)
            int randNum = (int) (Math.random() * (122 - 97 + 1)) + 97;
            randStr = randStr + (char) randNum;
        }
        
        return randStr;
    }

    /**
     * Returns a string consisting of the string str1, minus all the characters in the
     * string str2. Assumes (without checking) that str2 is a subset of str1.
     * Example: remove("meet","committee") returns "comit" 
     * 
     * @param str1 - a string
     * @param str2 - a string
     * @return a string consisting of str1 minus all the characters of str2
     */
    public static String remove(String str1, String str2) {
        int pos;
        for (int i = 0; i < str2.length(); i++) {
            pos = str1.indexOf(str2.charAt(i));
            if (pos != -1) {
                str1 = str1.substring(0, pos) + str1.substring(pos + 1);
            }
        }
        return str1;
    }

    /**
     * Returns a string consisting of the given string, with the given 
     * character inserted randomly somewhere in the string.
     * For example, insertRandomly("s","cat") can return "scat", or "csat", or "cast", or "cats".  
     * @param ch - a character
     * @param str - a string
     * @return a string consisting of str with ch inserted somewhere
     */
    public static String insertRandomly(char ch, String str) {
         // Generate a random index between 0 and str.length()
         int randomIndex = (int) (Math.random() * (str.length() + 1));
         // Insert the character at the random index
         String result = str.substring(0, randomIndex) + ch + str.substring(randomIndex);
         return result;
    }    
}
