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
    
    private class ReceiveThread implements Runnable {
        private Thread thread;
        private String threadName;
       
        ReceiveThread(String name){
            threadName = name;
            thread = new Thread (this, threadName);
        }
       
        public void run() {
            InetAddress ipAddress;
            Payload recPayload, sendPayload;
            String msg;
            Serial serial;

            System.out.println("[Client] Running " +  threadName + " on port:"+ serverPort);
            serial = new Serial();
            recPayload = null;

            while(true){
                try{
                    System.out.println("[Client] wait for packet...");
                    recPayload = receive();
                }
                catch(Exception e){
                }
                
                switch (recPayload.type){
                    case 0:
                        //add table entry
                        System.out.println("[Client] nickName:" + recPayload.nickName);
                        System.out.println("[Client] ip:" + recPayload.ip);
                        System.out.println("[Client] port:" + recPayload.port);
                        System.out.println("[Client] isOnline:" + recPayload.isOnline);
                        localTbl.insert(recPayload.nickName, recPayload.ip, recPayload.port, recPayload.isOnline);
                        localTbl.dumpTable();

                        //send ack to server
                        sendPayload = new Payload();
                        sendPayload.type = 2;
                        sendPayload.nickName = nickName;
                        msg = serial.serialize(sendPayload);
                        try{
                            ipAddress = InetAddress.getByName(serverIp);
                            send(msg, ipAddress, serverPort);
                        }
                        catch(Exception e){
                        }
                        break;

                    case 3:
                        //reset local table
                        localTbl = new Table();

                        //send ack to server
                        sendPayload = new Payload();
                        sendPayload.type = 2;
                        sendPayload.nickName = nickName;
                        msg = serial.serialize(sendPayload);
                        try{
                            ipAddress = InetAddress.getByName(serverIp);
                            send(msg, ipAddress, serverPort);
                        }
                        catch(Exception e){
                        }
                        System.out.println("[Client] start update table...");
                        break;
                    case 4:
                        //TODO add table unlock
                        //send ack to server
                        sendPayload = new Payload();
                        sendPayload.type = 2;
                        sendPayload.nickName = nickName;
                        msg = serial.serialize(sendPayload);
                        try{
                            ipAddress = InetAddress.getByName(serverIp);
                            send(msg, ipAddress, serverPort);
                        }
                        catch(Exception e){
                        }
                        System.out.println(">>> [Client table updated.]");
                        break;
                    case 7:
                        break;
                }
            }
        }
       
        public void start () {
            thread.start ();
        }
    }

    public void send(String msg, InetAddress ipAddress, int port) throws Exception{
        DatagramPacket sendPacket;
        
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
        ipAddress = InetAddress.getByName(this.serverIp);

        do{
            //send register information to server
            payload = new Payload();
            payload.type = 0;
            payload.nickName = this.nickName;
            payload.ip = ipAddress.getHostAddress();
            payload.port = this.clientPort;
            payload.isOnline = 1;
            msg = serial.serialize(payload);
            send(msg, ipAddress, this.serverPort);
         
            //wait for ack from server
            payload = receive();
            System.out.println("[Client] payload.type:" + payload.type);
            System.out.println(">>> " + payload.msg);
        }while(payload.type != 1);
    }
    
    public void chat(String nickName, String msg) throws Exception{
        InetAddress ipAddress;

        if(!socalTbl.contains(nickName)){
            System.err.println(">>> nickName:" + nickName + " not existed!!!");
        }
    }

    public void mainLoop() throws Exception{
        ReceiveThread receiveThread;
        BufferedReader br;
        String command, strLine, msg, nickName; 

        register();
        receiveThread = new ReceiveThread("Receive thread");
        receiveThread.start();
        Thread.sleep(100); 
        
        while(true){
            br = new BufferedReader(new InputStreamReader(System.in));
            System.out.print(">>> ");
            strLine = br.readLine();

            if(strLine.indexOf(' ') == -1){
                System.err.println("Wrong format !!!");
                continue; 
            }

            command = strLine.substring(0, strLine.indexOf(' '));
            strLine = strLine.substring(strLine.indexOf(' ') + 1);
            
            if(command.compareTo("send") == 0){
                nickName = strLine.substring(0, strLine.indexOf(' '));
                msg = strLine.substring(strLine.indexOf(' ') + 1);
                chat(nickName, msg);
            }
            else if(command.compareTo("dereg") == 0){
                System.out.println("[Client] dereg " + strLine);
            }
            else{
                System.err.println("Wrong format !!!");
            }
        }
    }
}
