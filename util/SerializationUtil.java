package Blockchain.util;

import java.io.*;

public class SerializationUtil {
    private SerializationUtil() {}

    public static void serialize(Object obj, String fileName) throws IOException {
        try (var fos = new FileOutputStream(fileName);
             var bos = new BufferedOutputStream(fos);
             var oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
        }
    }

    public static Object deserialize(String fileName) throws IOException, ClassNotFoundException {
        Object obj;
        try (var fis = new FileInputStream(fileName);
             var bis = new BufferedInputStream(fis);
             var ois = new ObjectInputStream(bis)) {
            obj = ois.readObject();
        }
        return obj;
    }
}
