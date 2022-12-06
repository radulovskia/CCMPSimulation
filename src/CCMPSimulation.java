import Tools.AssociatedData;
import Tools.CCMPImpl;
import Tools.ClearTextFrame;
import Tools.EncryptedFrame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

import static Tools.HelperFunctions.incrementByteArray;

public class CCMPSimulation{
    public static void main(String[] args) throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        byte[] packageNumber = "1234".getBytes();
        while(true) {
            //generated using https://miniwebtool.com/mac-address-generator/
            AssociatedData frameHeader = new AssociatedData("C90CEBB772B6".getBytes(),
                    "018183EFE82D".getBytes(),
                    packageNumber);
            System.out.println("Enter a message or \"q\" to end the simulation:");
            String originalString = br.readLine();
            if (originalString.equals("q"))
                break;
            try {
                ClearTextFrame ctf = new ClearTextFrame(frameHeader, originalString);
                System.out.println("Original text:\t"+ctf);
                EncryptedFrame ef = CCMPImpl.encryptFrame(ctf);
                System.out.println("Encrypted text:\t"+ Base64.getEncoder().encodeToString(ef.getEncData()));
                System.out.println("Decrypted text:\t"+ ef);
                System.out.println("Package Number:\t"+ Byte.hashCode(packageNumber[packageNumber.length-1]) + "\n");
                incrementByteArray(packageNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
