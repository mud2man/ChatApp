/* 
 * Encode format: msg0Length.msg0 + msg1Length.msg1 + msg2Length.msg2 ...
 */

import java.util.*;

public class Serial{
    Serial(){
    }

    public String serialize(Payload payload){
        String msg, typeStr, portStr, payloadMsg;
        
        msg = "";

        //encoe type
        typeStr = Integer.toString(payload.type);
        msg += Integer.toString(typeStr.length());
        msg += ".";
        msg += typeStr;
        
        switch (payload.type){
            case 0:
                //encode nickName
                msg += Integer.toString(payload.nickName.length());
                msg += ".";
                msg += payload.nickName;
                
                //encode ip
                msg += Integer.toString(payload.ip.length());
                msg += ".";
                msg += payload.ip;
                
                //encode port
                portStr = Integer.toString(payload.port);
                msg += Integer.toString(portStr.length());
                msg += ".";
                msg += portStr;

                break;
            case 1:
                //encode message
                msg += Integer.toString(payload.msg.length());
                msg += ".";
                msg += payload.msg;
                break;

            default:
                break;
        }
        System.out.println("[Serial] msg:" + msg);
        return msg;
    }

    public Payload deserialize(String msg){
        int len;
        String subStr;
        Payload payload = new Payload();
        
        //decode type
        subStr = msg.substring(0, msg.indexOf('.'));
        msg = msg.substring(msg.indexOf('.') + 1);
        len = Integer.parseInt(subStr);
        subStr = msg.substring(0, len);
        msg = msg.substring(len);
        payload.type = Integer.parseInt(subStr);
        System.out.println("[Serial] type:" + payload.type);

        switch (payload.type){
            case 0:
                //decode nickName
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                payload.nickName = msg.substring(0, len);
                msg = msg.substring(len);

                //decode ip
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                payload.ip = msg.substring(0, len);
                msg = msg.substring(len);

                //decode port
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                subStr = msg.substring(0, len);
                payload.port = Integer.parseInt(subStr);

                System.out.println("[Serial] nickName:" + payload.nickName);
                System.out.println("[Serial] ip:" + payload.ip);
                System.out.println("[Serial] port:" + payload.port);
                break;

            case 1:
                //decode message
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                payload.msg = msg.substring(0, len);
                msg = msg.substring(len);
                System.out.println("[Serial] msg:" + payload.msg);
                break;

            default:
                break;
        }

        return payload;
    }
}
