package Tools;

import Exceptions.XorLengthException;
import java.util.Arrays;

import static Tools.HelperFunctions.initialCtr;
import static Tools.HelperFunctions.xor;

public class Frame{
    protected AssociatedData header;
    protected byte[] data;
    protected byte[] mic;
    protected byte[] nonce;

    protected static byte[] commonMicHelper(byte[] wholeFrame, byte[] mic, AESImpl aes) throws XorLengthException{
        for (int i = 0; i < wholeFrame.length / 16; i++){
            byte[] block = Arrays.copyOfRange(wholeFrame, i * 16, i * 16 + 16);
            mic = xor(mic, block);
            mic = aes.encrypt(mic);
        }
        mic = Arrays.copyOfRange(mic, 0, mic.length / 2);
        mic = xor(mic, Arrays.copyOfRange(initialCtr, 0, initialCtr.length / 2));
        return mic;
    }

    public AssociatedData getHeader(){
        return header;
    }

    public byte[] getData(){
        return data;
    }

    public void setMic(byte[] mic){
        this.mic = mic;
    }
}
