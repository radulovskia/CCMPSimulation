package Tools;

import Exceptions.XorLengthException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class HelperFunctions{
    public static final byte[] secretKey = "SuperSecretKey".getBytes();
    public static final byte[] initialCtr = "MyInitialCounter".getBytes();

    public static byte[] xor(byte[] seq1, byte[] seq2) throws XorLengthException{
        if(seq1.length!=seq2.length)
            throw new XorLengthException();
        byte[] result = new byte[seq1.length];
        for(int i=0;i<seq1.length;i++){
            result[i]=(byte)(seq1[i]^seq2[i]);
        }
        return result;
    }

    public static byte[] concatenateBytes(byte[]... b) throws IOException{
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        for (byte[] bytes : b)
            stream.write(bytes);
        return stream.toByteArray();
    }

    public static byte[] incrementByteArray(byte[] ctr){
        ctr[ctr.length-1] = (byte)  (ctr[ctr.length-1]+1);
        return ctr;
    }

    public static byte[] addPaddingBytes(byte[] bytes) throws IOException {
        if (bytes.length%16==0)
            return bytes;
        return concatenateBytes(bytes,new byte[16-bytes.length%16]);
    }
}
