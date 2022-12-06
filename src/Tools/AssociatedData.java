package Tools;

import java.io.IOException;
import static Tools.HelperFunctions.*;

public class AssociatedData{
    private byte[] sourceMAC;
    private byte[] destMAC;
    public static byte[] packageNum;

    public AssociatedData(byte[] sourceMAC, byte[] destMAC, byte[] packageNum){
        this.sourceMAC = sourceMAC;
        this.destMAC = destMAC;
        this.packageNum = packageNum;
    }

    public byte[] getSourceMAC(){
        return sourceMAC;
    }

    public byte[] getPackageNum() {
        return packageNum;
    }

    public void setPackageNum(byte[] packageNum){
        this.packageNum = packageNum;
    }

    public byte[] getEntireHeader() throws IOException{
        return addPaddingBytes(concatenateBytes(sourceMAC,destMAC,packageNum));
    }
}
