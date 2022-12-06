package Tools;

import Exceptions.AuthenticationFailedException;
import Exceptions.XorLengthException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Tools.HelperFunctions.*;

public class CCMPImpl{
    public static List<byte[]> divideArray(byte[] source) {
        List<byte[]> result = new ArrayList<byte[]>();
        int start = 0;
        while (start < source.length) {
            int end = Math.min(source.length, start + 16);
            result.add(Arrays.copyOfRange(source, start, end));
            start += 16;
        }
        return result;
    }

    public static byte[] encryptData(ClearTextFrame ctf) throws IOException, XorLengthException{
        AESImpl aes = new AESImpl();
        aes.setTheKey(secretKey);
        byte[] clearData = addPaddingBytes(ctf.getClearData());
        byte[] counter = initialCtr.clone();
        byte[] encData = new byte[0];
        List<byte[]> parts = divideArray(clearData);
        return modData(aes, counter, encData, parts);
    }

    public static EncryptedFrame encryptFrame(ClearTextFrame ctf) throws IOException, AuthenticationFailedException, XorLengthException{
        EncryptedFrame ef = new EncryptedFrame(ctf.getHeader(),encryptData(ctf),null);
        byte[] encMic = ef.calculateMic();
        ef.setMic(encMic);
        return ef;
    }

    public static byte[] decryptData(EncryptedFrame ef) throws IOException, XorLengthException{
        AESImpl aes = new AESImpl();
        aes.setTheKey(secretKey);
        byte[] counter = initialCtr.clone();
        byte[] decData = new byte[0];
        List<byte[]> parts = divideArray(ef.data);
        return modData(aes, counter, decData, parts);
    }

    private static byte[] modData(AESImpl aes, byte[] counter, byte[] newData, List<byte[]> parts) throws XorLengthException, IOException{
        byte[] encCtr;
        for(int i = 0; i<parts.size(); i++){
            byte[] part = parts.get(i);
            counter = incrementByteArray(counter);
            encCtr = aes.encrypt(counter);
            part = xor(part,encCtr);
            newData = concatenateBytes(newData,part);
        }
        return newData;
    }

    public static ClearTextFrame decryptFrame(EncryptedFrame ef) throws IOException, XorLengthException{
        ClearTextFrame ctf = new ClearTextFrame(ef.getHeader(), new String(decryptData(ef)));
        byte[] clearMic = ctf.calculateMic();
        ctf.setMic(clearMic);
        return ctf;
    }
}
