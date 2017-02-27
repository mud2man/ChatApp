import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    int port;
    Table globalTbl;
    DatagramSocket serverSocket;

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
    
    public void mainLoop() throws Exception {
        InetAddress ipAddress;
        Payload payload;
        String msg;

        while(true){
            payload = receive();
            System.out.println("[Server] type:" + payload.type);

            switch (payload.type){
                case 0:
                    System.out.println("[Server] nickName:" + payload.nickName);
                    System.out.println("[Server] ip:" + payload.ip);
                    System.out.println("[Server] port:" + payload.port);
                    globalTbl.insert(payload.nickName, payload.ip, payload.port);
                    globalTbl.onLine(payload.nickName);
                    globalTbl.dumpTable();

                    //send ack to client
                    msg = "";
                    msg += "1.1";
                    msg += "30.[Welcome, You are registered.]";
                    ipAddress = InetAddress.getByName(payload.ip);
                    send(msg, ipAddress, payload.port);
                    break;

                case 1:
                    break;
                default:
                    break;
            }
        }
    }
}
