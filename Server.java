import java.io.*;
import java.net.*;
import java.util.*;

public class Server{
    int port;
    Table globalTbl;

    public Server(int port){
        this.port = port;
        this.globalTbl = new Table();

        System.out.println("[Server] Hello Server, port:" + port);
    }
    
    public void mainLoop() throws Exception {
        DatagramSocket serverSocket;
        DatagramPacket receivePacket, sendPacket;
        InetAddress ipAddress;
        byte[] receiveData;
        byte[] sendData;
        Serial serial;
        Payload payload;
        String msg;

        serverSocket = new DatagramSocket(port);
        receiveData = new byte[1024];
        sendData = new byte[1024];
        serial = new Serial();


        while(true){
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            msg = new String(receivePacket.getData());
            System.out.println("[Server] " + msg);

            payload = serial.deserialize(msg);
            System.out.println("[Server] type:" + payload.type);

            switch (payload.type){
                case 0:
                    System.out.println("[Server] nickName:" + payload.nickName);
                    System.out.println("[Server] ip:" + payload.ip);
                    System.out.println("[Server] port:" + payload.port);
                    globalTbl.insert(payload.nickName, payload.ip, payload.port);
                    globalTbl.dumpTable();

                    //send ack to client
                    msg = "";
                    msg += "1.1";
                    ipAddress = InetAddress.getByName(payload.ip);
                    sendPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ipAddress, payload.port);
                    serverSocket.send(sendPacket);
                    break;

                case 1:
                    break;
                default:
                    break;
            }
        }
    }
}
