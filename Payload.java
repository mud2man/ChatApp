/*
 * payloadType0: add register table entry => type, nickName, ip, port, isOnline 
 * payloadType1: ack from server => type, message
 * payloadType2: ack from client => type
 * payloadType3: start updat table from server => type
 * payloadType4: finish updat table from server => type
 * payloadType5: nack from server => type
 * payloadType6: nack from client => type, nickName
 * payloadType7: message from client => type, nickName, message
 * payloadType8: deregister => type, nickName, ip, port
 */

import java.util.*;

public class Payload{
    public int type;
    public String nickName;
    public String ip;
    public int port;
    public String msg;
    public int isOnline;

    Payload(){
        type = 0;
        nickName = "";
        ip = "";
        port = 0;
        msg = "";
        isOnline = 0;
    }
}
