import java.io.*;
import java.net.*;
import java.util.*;

public class Client{
    String nickName;
    String serverIp;
    int serverPort;
    int clientPort;
    Table localTbl;

    public Client(String nickName, String serverIp, int serverPort, int clientPort){
        this.nickName = nickName;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        this.localTbl = new Table();

        System.out.println("[Client] nickName:" + this.nickName);
        System.out.println("[Client] serverIp:" + this.serverIp);
        System.out.println("[Client] serverPort:" + this.serverPort);
        System.out.println("[Client] clientPort:" + this.clientPort);
    }

    public void register() throws Exception {
        byte[] receiveData;
        String msg = "" ;
        DatagramSocket clientSocket;
        InetAddress ipAddress;
        DatagramPacket sendPacket, receivePacket;
        Serial serial;
        Payload payload;
        
        msg = "";
        clientSocket = new DatagramSocket(this.clientPort);
        ipAddress = InetAddress.getByName("localhost");
        serial = new Serial();
        receiveData = new byte[1024];

        do{
            msg += "1.0";
            msg += "4.namo";
            msg += Integer.toString(ipAddress.getHostAddress().length());
            msg += ".";
            msg += ipAddress.getHostAddress();
            msg += "4.1025";
            
            //send register information to server
            sendPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ipAddress, this.serverPort);
            clientSocket.send(sendPacket);
         
            //wait for ack from server
            receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);
            msg = new String(receivePacket.getData());
            System.out.println("[Client] msg: " + msg);
            payload = serial.deserialize(msg);
            System.out.println("[Client] payload.type:" + payload.type);
        }while(payload.type != 1);
        

        clientSocket.close();
    }

    public void mainLoop() throws Exception{
        System.out.println("[Client] Hello client mainLoop");

        register();
    }
}
