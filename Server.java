import java.io.*;
import java.net.*;
import java.util.*;
import java.text.*;

public class Server{
    int port;
    Table globalTbl;
    DatagramSocket serverSocket;
    HashMap<String, List<MessageNode>> offlineMsgTbl;

    private class MessageNode{
        String sender;
        String msg;
        MessageNode(String s, String m){sender = s; msg = m;}
    }

    public Server(int port) throws Exception{
        this.port = port;
        this.globalTbl = new Table();
        serverSocket = new DatagramSocket(this.port);
        offlineMsgTbl = new HashMap<String, List<MessageNode>>();

        System.out.println("[Server] Hello Server, port:" + port);
    }
    
    public void send(String msg, InetAddress ipAddress, int port) throws Exception{
        DatagramPacket sendPacket;
        
        sendPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ipAddress, port);
        serverSocket.send(sendPacket);
    }
    
    public Payload receive() throws Exception{
        String msg;
        byte[] receiveData;
        Payload payload;
        DatagramPacket receivePacket;
        Serial serial;

        serial = new Serial();
        receiveData = new byte[1024];
        receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        msg = new String(receivePacket.getData());
        //System.out.println("[Server] msg:" + msg);
        payload = serial.deserialize(msg);

        return payload;
    }

    public void broadcastTable() throws Exception {
        HashMap<String, ClientInfo> tbl;
        String msg;
        Payload recPayload, sendPayload;
        Serial serial;
        InetAddress ipAddress;
        ClientInfo clientInfo;

        serial = new Serial();
        tbl = globalTbl.tbl;
    retry:
        for(Map.Entry<String, ClientInfo> client: tbl.entrySet()) {
            clientInfo = client.getValue();
            ipAddress = InetAddress.getByName(clientInfo.clientIp);

            if(clientInfo.isOnline == 0){
                continue;
            }
           
            //start update table
            sendPayload = new Payload();
            sendPayload.type = 3;
            sendPayload.msg = "[Start update table.]";
            msg = serial.serialize(sendPayload);
            ipAddress = InetAddress.getByName(clientInfo.clientIp);
            //System.out.println("[Server] start updating table");
            send(msg, ipAddress, clientInfo.clientPort);
                
            //wait for ack from client
            recPayload = receive();
            //System.out.println("[Server] receive ack of start");
            if(recPayload.type != 2){
                continue retry;
            }

            for(Map.Entry<String, ClientInfo> entry: tbl.entrySet()) {
                //send table entry to client
                sendPayload = new Payload();
                sendPayload.type = 0;
                sendPayload.nickName = entry.getKey();
                sendPayload.ip = entry.getValue().clientIp;
                sendPayload.port = entry.getValue().clientPort;
                sendPayload.isOnline = entry.getValue().isOnline;
                msg = serial.serialize(sendPayload);
                send(msg, ipAddress, clientInfo.clientPort);
         
                //wait for ack from client
                recPayload = receive();
                //System.out.println("[Server] receive ack of add");
                if(recPayload.type != 2){
                    continue retry;
                }
            }
            
            //finish table update
            sendPayload = new Payload();
            sendPayload.type = 4;
            msg = serial.serialize(sendPayload);
            send(msg, ipAddress, clientInfo.clientPort);
            
            //wait for ack from client
            recPayload = receive();
            //System.out.println("[Server] receive ack of finish");
            if(recPayload.type != 2){
                continue retry;
            }
        }
    }

    public void dumpOfflineMsg(){
        List<MessageNode> messageList;
        
        //System.out.println("[Server] Dump offline message:");

        for(Map.Entry<String, List<MessageNode>> entry : offlineMsgTbl.entrySet()){
            //System.out.println("[Server] receiver:" + entry.getKey());
            messageList = entry.getValue();
            for(MessageNode mg: messageList){
                //System.out.print("[Server] message:" + mg.msg + ", ");
                //System.out.println("sender:" + mg.sender);
            }
        }
    }

    public void sendOfflineMsg(String nickName) throws Exception {
        HashMap<String, ClientInfo> tbl;
        List<MessageNode> messageList;
        InetAddress ipAddress;
        Payload sendPayload;
        String msg;
        Serial serial;
        int port;
        Iterator<MessageNode> it;

        tbl = globalTbl.tbl;
        serial = new Serial();
        ipAddress = InetAddress.getByName(tbl.get(nickName).clientIp);
        port = tbl.get(nickName).clientPort;

        if(offlineMsgTbl.containsKey(nickName)){
            messageList = offlineMsgTbl.get(nickName);
            if(messageList.size() > 0){
                sendPayload = new Payload();
                sendPayload.type = 10;
                sendPayload.nickName = "server";
                sendPayload.msg = "[You have messages]";
                msg = serial.serialize(sendPayload);
                send(msg, ipAddress, port);
            
                it = messageList.iterator();
                while (it.hasNext()) {
                    MessageNode msgNode = it.next();
                    //System.out.print("[Server] message:" + msgNode.msg + ", ");
                    //System.out.println("sender:" + msgNode.sender);

                    sendPayload = new Payload();
                    sendPayload.type = 10;
                    sendPayload.nickName = msgNode.sender;
                    sendPayload.msg = msgNode.msg;
                    msg = serial.serialize(sendPayload);
                    send(msg, ipAddress, port);
                    it.remove();
                }
            }
        }  
    }
    
    public void mainLoop() throws Exception {
        HashMap<String, ClientInfo> tbl;
        InetAddress ipAddress;
        Payload recPayload, sendPayload;
        String msg;
        Serial serial;
        List<MessageNode> msgList;
        MessageNode messageNode;
        int port;
        DateFormat dateFormat;
        Date date;
        
        tbl = globalTbl.tbl;
        serial = new Serial();

        while(true){
            recPayload = receive();
            //System.out.println("[Server] type:" + recPayload.type);

            switch (recPayload.type){
                //register
                case 0:
                    //System.out.println("[Server] nickName:" + recPayload.nickName);
                    //System.out.println("[Server] ip:" + recPayload.ip);
                    //System.out.println("[Server] port:" + recPayload.port);
                    //System.out.println("[Server] isOnline:" + recPayload.isOnline);
                    globalTbl.insert(recPayload.nickName, recPayload.ip, recPayload.port, recPayload.isOnline);
                    globalTbl.dumpTable();

                    //send ack to client
                    sendPayload = new Payload();
                    sendPayload.type = 1;
                    sendPayload.msg = "[Welcome, You are registered.]";
                    msg = serial.serialize(sendPayload);
                    ipAddress = InetAddress.getByName(recPayload.ip);
                    //System.out.println("ipAddress:" + ipAddress + ", recPayload.port: " + recPayload.port);
                    send(msg, ipAddress, recPayload.port);
                    
                    //broadcast updated table 
                    broadcastTable();

                    //send offline message
                    sendOfflineMsg(recPayload.nickName);
                    break;

                //de-register
                case 8:
                    //System.out.println("[Server] nickName:" + recPayload.nickName);
                    //System.out.println("[Server] ip:" + recPayload.ip);
                    //System.out.println("[Server] port:" + recPayload.port);
                    globalTbl.offLine(recPayload.nickName);
                    globalTbl.dumpTable();
                    
                    //send ack to client
                    sendPayload = new Payload();
                    sendPayload.type = 1;
                    sendPayload.msg = "[You are Offline. Bye.]";
                    msg = serial.serialize(sendPayload);
                    ipAddress = InetAddress.getByName(recPayload.ip);
                    //System.out.println("ipAddress:" + ipAddress + ", recPayload.port: " + recPayload.port);
                    send(msg, ipAddress, recPayload.port);

                    //broadcast updated table 
                    broadcastTable();
                    break;
                
                //receive offline message
                case 9:
                    //System.out.println("[Server] nickName:" + recPayload.nickName);
                    //System.out.println("[Server] msg:" + recPayload.msg);
                    //System.out.println("[Server] offlineAccount:" + recPayload.offlineAccount);
                    
                    dateFormat = new SimpleDateFormat("yyyy.MM.dd-HH:mm:ss");
                    date = new Date();
                    msg = dateFormat.format(date);
                    msg += " ";
                    
                    if(tbl.get(recPayload.offlineAccount).isOnline == 0){
                        if(!offlineMsgTbl.containsKey(recPayload.offlineAccount)){
                            msgList = new ArrayList<MessageNode>();
                            msg += recPayload.msg;
                            messageNode = new MessageNode(recPayload.nickName, msg);
                            msgList.add(messageNode);
                            offlineMsgTbl.put(recPayload.offlineAccount, msgList);
                        }
                        else{
                            msg += recPayload.msg;
                            messageNode = new MessageNode(recPayload.nickName, msg);
                            offlineMsgTbl.get(recPayload.offlineAccount).add(messageNode);
                        }
                    
                        //send ack to client
                        sendPayload = new Payload();
                        sendPayload.type = 1;
                        sendPayload.msg = "";
                        msg = serial.serialize(sendPayload);
                        ipAddress = InetAddress.getByName(tbl.get(recPayload.nickName).clientIp);
                        port = tbl.get(recPayload.nickName).clientPort;
                        send(msg, ipAddress, port);
                    }
                    else{
                        //send ack to client
                        sendPayload = new Payload();
                        sendPayload.type = 1;
                        sendPayload.msg = "[Client " + recPayload.offlineAccount + " exists!!]";
                        msg = serial.serialize(sendPayload);
                        ipAddress = InetAddress.getByName(tbl.get(recPayload.nickName).clientIp);
                        port = tbl.get(recPayload.nickName).clientPort;
                        send(msg, ipAddress, port);
                        
                        //broadcast updated table 
                        broadcastTable();
                    }
                    break;

                default:
                    break;
            }
        }
    }
}
