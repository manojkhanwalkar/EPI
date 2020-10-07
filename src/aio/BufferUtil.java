package aio;

import java.nio.ByteBuffer;

public class BufferUtil {


    public static String getString(ByteBuffer readBuffer, int num)
    {

        byte[] arr = new byte[num];
        System.arraycopy(readBuffer.array(), 0, arr, 0, num);


        return  new String(arr) ;
    }
}
