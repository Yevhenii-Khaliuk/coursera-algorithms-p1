/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

public class HelloGoodbye {
    public static void main(String[] args) {
        String helloMessage = String.format("Hello %s and %s.", args[0], args[1]);
        String goodbyeMessage = String.format("Goodbye %s and %s.", args[1], args[0]);
        System.out.println(helloMessage);
        System.out.println(goodbyeMessage);
    }
}
