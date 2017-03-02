import java.io.*;
import java.net.*;
import java.util.*;

public class UdpChat{
    public static void main(String args[]) throws Exception{
        String nickName, serverIp;
        int serverPort, clientPort;
        Client client;
        Server server;

        if(args[0].compareTo("-s") == 0){
            server = new Server(Integer.parseInt(args[1]));
            server.mainLoop();
        }
        else if(args[0].compareTo("-c") == 0){
            nickName = args[1];
            serverIp = args[2];
            serverPort = Integer.parseInt(args[3]);
            clientPort = Integer.parseInt(args[4]);

            client = new Client(nickName, serverIp, serverPort, clientPort);
            client.mainLoop();
        }
        else{
            System.err.println("Wrong usage");
        }
    }
}
