/* 
 * Encode format: msg0Length.msg0 + msg1Length.msg1 + msg2Length.msg2 ...
 */

import java.util.*;

public class Serial{
    Serial(){
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
                break;
            default:
                break;
        }

        return payload;
    }
}
