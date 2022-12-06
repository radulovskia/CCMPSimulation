package Tools;

import Exceptions.XorLengthException;
import java.io.IOException;

import static Tools.HelperFunctions.*;

public class ClearTextFrame extends Frame{

    public ClearTextFrame(AssociatedData frameHeader,String clearData) throws IOException, XorLengthException{
        this.data = clearData.getBytes();
        this.header =  frameHeader;
        this.nonce = calculateNonce();
        this.mic = calculateMic();
    }

    private byte[] calculateNonce() throws IOException{
        return addPaddingBytes(concatenateBytes(this.header.getPackageNum(), this.header.getSourceMAC()));
    }

    public byte[] calculateMic() throws IOException, XorLengthException{
        byte[] wholeFrame = addPaddingBytes(concatenateBytes(this.header.getEntireHeader(), this.getData()));
        byte[] mic;
        AESImpl aes = new AESImpl();
        aes.setTheKey(secretKey);
        mic = aes.encrypt(nonce);
        mic = commonMicHelper(wholeFrame, mic, aes);
        this.mic = mic;
        return mic;
    }

    public AssociatedData getHeader(){
        return header;
    }

    public void setHeader(AssociatedData header){
        this.header = header;
    }

    public byte[] getClearData(){
        return data;
    }

    public byte[] getMic(){
        return mic;
    }

    public void setMic(byte[] mic){
        this.mic = mic;
    }

    public byte[] getNonce(){
        return nonce;
    }

    public void setNonce(byte[] nonce){
        this.nonce = nonce;
    }
    @Override
    public String toString(){
        return String.format("%s", new String(data));
    }
}
