import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    int port;
    Table globalTbl;
    DatagramSocket serverSocket;
    // 0:
    int state;

    public Server(int port) throws Exception{
        this.port = port;
        this.globalTbl = new Table();
        serverSocket = new DatagramSocket(this.port);

        System.out.println("[Server] Hello Server, port:" + port);
    }
    
    public void send(String msg, InetAddress ipAddress, int port) throws Exception{
        DatagramPacket sendPacket;
        
        ipAddress = InetAddress.getByName("localhost");
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
        System.out.println("[Server] msg: " + msg);
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

        for(Map.Entry<String, ClientInfo> client: tbl.entrySet()) {
            clientInfo = client.getValue();
            ipAddress = InetAddress.getByName(clientInfo.clientIp);
            
            sendPayload = new Payload();
            sendPayload.type = 3;
            sendPayload.msg = "[Start update table.]";
            msg = serial.serialize(sendPayload);
            ipAddress = InetAddress.getByName(recPayload.ip);
            send(msg, ipAddress, recPayload.port);
             
        }
    }
    
    public void mainLoop() throws Exception {
        InetAddress ipAddress;
        Payload recPayload, sendPayload;
        String msg;
        Serial serial;
        
        serial = new Serial();

        while(true){
            recPayload = receive();
            System.out.println("[Server] type:" + recPayload.type);

            switch (recPayload.type){
                //register
                case 0:
                    System.out.println("[Server] nickName:" + recPayload.nickName);
                    System.out.println("[Server] ip:" + recPayload.ip);
                    System.out.println("[Server] port:" + recPayload.port);
                    globalTbl.insert(recPayload.nickName, recPayload.ip, recPayload.port);
                    globalTbl.onLine(recPayload.nickName);
                    globalTbl.dumpTable();

                    //send ack to client
                    sendPayload = new Payload();
                    sendPayload.type = 1;
                    sendPayload.msg = "[Welcome, You are registered.]";
                    msg = serial.serialize(sendPayload);
                    ipAddress = InetAddress.getByName(recPayload.ip);
                    send(msg, ipAddress, recPayload.port);
                    
                    //broadcast updated table 
                    broadcastTable();
                    break;

                case 1:
                    break;
                default:
                    break;
            }
        }
    }
}
