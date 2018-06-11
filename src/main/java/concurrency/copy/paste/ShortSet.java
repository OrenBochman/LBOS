package concurrency.copy.paste;
import java.util.Set;
import java.util.HashSet;


/**
 * this is an example to check if error prone is working
 * @author Oren Bochman
 *
 */
public class ShortSet {
  public static void main (String[] args) {
    Set<Short> s = new HashSet<>();
    for (short i = 0; i < 100; i++) {
      s.add(i);
      s.remove(i-1);
    }
    System.out.println(s.size());
  }
}