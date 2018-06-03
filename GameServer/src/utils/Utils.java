package utils;

import java.io.*;

public class Utils
{
    public static Object createObject(byte[] bytes)
    {
        Object object = null;
        try
        {
            ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
            ObjectInputStream inputStream = new ObjectInputStream(byteInput);

            object = inputStream.readObject();

            inputStream.close();
            byteInput.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        } catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return object;
    }

    public static byte[] createByteArray(Object object)
    {
        byte[] bytes = null;
        try
        {
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            ObjectOutputStream outputStream = new ObjectOutputStream(byteOut);

            outputStream.writeObject(object);
            outputStream.flush();
            bytes =  byteOut.toByteArray();

            outputStream.close();
            byteOut.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return bytes;
    }
}
