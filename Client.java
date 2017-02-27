import java.io.*;
import java.net.*;
import java.util.*;


public class Client{
    String nickName;
    String serverIp;
    int serverPort;
    int clientPort;
    Table localTbl;
    DatagramSocket clientSocket;

    private class ReceiveThread implements Runnable {
        private Thread t;
        private String threadName;
       
        ReceiveThread(String name){
            threadName = name;
            System.out.println("[Client] Creating thread:" +  threadName);
        }
       
        public void run() {
            System.out.println("Running " +  threadName );
            try {
                for(;;) {
                    System.out.println("[Client] Thread: " + threadName);
                    // Let the thread sleep for a while.
                    Thread.sleep(1000);
                }
            }catch (InterruptedException e) {
                System.out.println("[Client] Thread " +  threadName + " interrupted.");
            }
            System.out.println("[Client] Thread " +  threadName + " exiting.");
        }
       
        public void start () {
            System.out.println("[Client] Starting " +  threadName );
            if (t == null) {
                t = new Thread (this, threadName);
                t.start ();
            } 
        }
    }

    public Client(String nickName, String serverIp, int serverPort, int clientPort) throws Exception{
        this.nickName = nickName;
        this.serverIp = serverIp;
        this.serverPort = serverPort;
        this.clientPort = clientPort;
        this.localTbl = new Table();
        this.clientSocket = new DatagramSocket(this.clientPort);

        System.out.println("[Client] nickName:" + this.nickName);
        System.out.println("[Client] serverIp:" + this.serverIp);
        System.out.println("[Client] serverPort:" + this.serverPort);
        System.out.println("[Client] clientPort:" + this.clientPort);
    }
    
    public void send(String msg, InetAddress ipAddress, int port) throws Exception{
        DatagramPacket sendPacket;
        
        ipAddress = InetAddress.getByName("localhost");
        sendPacket = new DatagramPacket(msg.getBytes(), msg.getBytes().length, ipAddress, port);
        clientSocket.send(sendPacket);
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
        clientSocket.receive(receivePacket);
        msg = new String(receivePacket.getData());
        System.out.println("[Client] msg:" + msg);
        payload = serial.deserialize(msg);

        return payload;
    }

    public void register() throws Exception{
        String msg = "" ;
        InetAddress ipAddress;
        Payload payload;
        Serial serial;
        
        serial = new Serial();
        msg = "";
        ipAddress = InetAddress.getByName("localhost");

        do{
            payload = new Payload();
            payload.type = 0;
            payload.nickName = "namo";
            payload.ip = ipAddress.getHostAddress();
            payload.port = this.clientPort;
            msg = serial.serialize(payload);
            
            //send register information to server
            send(msg, ipAddress, this.serverPort);
         
            //wait for ack from server
            payload = receive();
            System.out.println("[Client] payload.type:" + payload.type);
            System.out.println(">>> " + payload.msg);
        }while(payload.type != 1);
    }

    public void mainLoop() throws Exception{
        System.out.println("[Client] Hello client mainLoop");

        register();
        ReceiveThread receiveThread = new ReceiveThread("Receive Thread-1");
        receiveThread.start();
    }
}
