package Tools;

import Exceptions.AuthenticationFailedException;
import Exceptions.XorLengthException;

import java.io.IOException;
import java.util.Arrays;

import static Tools.CCMPImpl.decryptData;
import static Tools.HelperFunctions.*;

public class EncryptedFrame extends Frame{
    byte[] calculatedMic;
    byte[] decData;

    public EncryptedFrame(AssociatedData header, byte[] encData, byte[] originalMic) throws IOException, AuthenticationFailedException, XorLengthException{
        this.header =  header;
        this.data = encData;
        this.mic = originalMic;
        this.calculatedMic = calculateMic();
        if (!Arrays.equals(calculatedMic, mic)){
            System.out.println("calculated mic: "+ Arrays.toString(calculatedMic));
            System.out.println("original mic: "+ Arrays.toString(mic));
            throw new AuthenticationFailedException();
        }
    }

    private byte[] calculateNonce() throws IOException{
        return addPaddingBytes(concatenateBytes(this.header.getPackageNum(),this.header.getSourceMAC()));
    }

    public byte[] calculateMic() throws IOException, XorLengthException{
        decData = decryptData(this);
        byte[] wholeFrame = concatenateBytes(this.header.getEntireHeader(),decData);
        byte[] mic;
        AESImpl aes = new AESImpl();
        aes.setTheKey(secretKey);
        mic = aes.encrypt(calculateNonce());
        mic = ClearTextFrame.commonMicHelper(wholeFrame, mic, aes);
        this.setMic(mic);
        return mic;
    }

    public AssociatedData getHeader(){
        return header;
    }

    public byte[] getEncData(){
        return data;
    }

    @Override
    public String toString() {
        String s = new String(decData);
        return String.format("%s",s.trim());
    }
}
