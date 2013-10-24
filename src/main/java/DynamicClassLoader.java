package caseclass.generator;
import java.lang.reflect.*;

//package classLoader;

public class DynamicClassLoader {
  public static Class<?> loadClass(String className, byte[] b)  {
    //override classDefine (as it is protected) and define the class.
    Class<?> clazz = null;
    try {
     // ClassLoader loader = ClassLoader.getSystemClassLoader();
      ClassLoader loader = Thread.currentThread().getContextClassLoader();
      Class<?> cls = Class.forName("java.lang.ClassLoader");
      Method method = cls.getDeclaredMethod("defineClass", new Class[] { String.class, byte[].class, int.class, int.class });

      // protected method invocaton
      method.setAccessible(true);
      try {
        Object[] args = new Object[] { className, b, new Integer(0), new Integer(b.length)};
        clazz = (Class) method.invoke(loader, args);
      } finally {
        method.setAccessible(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
    return clazz;
  }
}
