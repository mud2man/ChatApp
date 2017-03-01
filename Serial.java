/* 
 * Encode format: msg0Length.msg0 + msg1Length.msg1 + msg2Length.msg2 ...
 */

import java.util.*;

public class Serial{
    Serial(){
    }

    public String serialize(Payload payload){
        String msg, typeStr, portStr, payloadMsg, isOnlineStr;
        
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

                //encode isOnline
                isOnlineStr = Integer.toString(payload.isOnline);
                msg += Integer.toString(isOnlineStr.length());
                msg += ".";
                msg += isOnlineStr;
                break;
            case 1:
                //encode message
                msg += Integer.toString(payload.msg.length());
                msg += ".";
                msg += payload.msg;
                break;

            case 7:
                //encode nickName
                msg += Integer.toString(payload.nickName.length());
                msg += ".";
                msg += payload.nickName;

                //encode message
                msg += Integer.toString(payload.msg.length());
                msg += ".";
                msg += payload.msg;
                break;
            
            case 8:
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
            
            case 9:
                //encode nickName
                msg += Integer.toString(payload.nickName.length());
                msg += ".";
                msg += payload.nickName;

                //encode message
                msg += Integer.toString(payload.msg.length());
                msg += ".";
                msg += payload.msg;
                
                //encode offlineAccount
                msg += Integer.toString(payload.offlineAccount.length());
                msg += ".";
                msg += payload.offlineAccount;

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
                msg = msg.substring(len);

                //decode isOnline
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                subStr = msg.substring(0, len);
                payload.isOnline = Integer.parseInt(subStr);

                System.out.println("[Serial] nickName:" + payload.nickName);
                System.out.println("[Serial] ip:" + payload.ip);
                System.out.println("[Serial] port:" + payload.port);
                System.out.println("[Serial] isOnline:" + payload.isOnline);
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
            
            case 7:
                //decode nickName
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                payload.nickName = msg.substring(0, len);
                msg = msg.substring(len);

                //decode message
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                payload.msg = msg.substring(0, len);
                msg = msg.substring(len);
                
                System.out.println("[Serial] nickName:" + payload.nickName);
                System.out.println("[Serial] msg:" + payload.msg);
                break;
            
            case 8:
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
                msg = msg.substring(len);

                System.out.println("[Serial] nickName:" + payload.nickName);
                System.out.println("[Serial] ip:" + payload.ip);
                System.out.println("[Serial] port:" + payload.port);
                break;

            case 9:
                //decode nickName
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                payload.nickName = msg.substring(0, len);
                msg = msg.substring(len);

                //decode message
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                payload.msg = msg.substring(0, len);
                msg = msg.substring(len);
                
                //decode offlineAccount
                subStr = msg.substring(0, msg.indexOf('.'));
                msg = msg.substring(msg.indexOf('.') + 1);
                len = Integer.parseInt(subStr);
                payload.offlineAccount = msg.substring(0, len);
                msg = msg.substring(len);
                
                System.out.println("[Serial] nickName:" + payload.nickName);
                System.out.println("[Serial] msg:" + payload.msg);
                System.out.println("[Serial] offlineAccount:" + payload.offlineAccount);
                break;
            
            default:
                break;
        }

        return payload;
    }
}
