/*
 * payloadType0: add register table entry => type, nickName, ip, port 
 * payloadType1: ack from server => type, message
 * payloadType2: ack from client => typ, nickNamee
 * payloadType3: start updat table client => type
 * payloadType4: finish updat table client => type
 * payloadType5: nack from server => type
 * payloadType6: nack from client => type, nickName
 */

import java.util.*;

public class Payload{
    public int type;
    public String nickName;
    public String ip;
    public int port;
    public String msg;

    Payload(){
        type = 0;
        nickName = "";
        ip = "";
        port = 0;
        msg = "";
    }
}
