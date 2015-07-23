/**
 * Created by tokitake on 2015/07/23.
 */

import java.util.ArrayList;
import java.util.List;

public class Foo {
  enum TYPE {AAA, BBB, CCC};

  public static void main(String[] args) {
    List<String> list = new ArrayList<String>();
    list.add("Foo");
    list.add("Bar");
    list.add("Baz");

    for (String str : list) {
      System.out.println(str + " ");
    }

    for (TYPE type : TYPE.values()) {
      System.out.println(type.name() + " ");
    }
  }

  public void method1(String[] arg0, int arg 1) {
    list.add("");
  }
  public void method2(int arg0, int arg2, int arg3) {
    list.add("");
  }
}
