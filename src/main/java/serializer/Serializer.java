package serializer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Serializer {
    public boolean writeMessage(DataOutputStream out, byte[] msg, int msgLen) {
        try{
            out.writeInt(msgLen);
            out.write(msg, 0, msgLen);
            out.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public byte[] readMessage(DataInputStream in) {
        byte[] msg = null;
        try{
            int msgLen = in.readInt();
            msg = new byte[msgLen];
            in.readFully(msg);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return msg;
    }
}
