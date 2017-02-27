/*
 * payloadType0: update register table => type, nickName, ip, port 
 * payloadType1: ack from server => type
 * payloadType2: ack from client => type
 */

import java.util.*;

public class Payload{
    public int type;
    public String nickName;
    public String ip;
    public int port;

    Payload(){
        type = 0;
        nickName = "";
        ip = "";
        port = 0;
    }
}
