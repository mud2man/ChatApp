import java.util.*;

class ClientInfo{
    String clientIp;
    int clientPort;
    boolean isOnline;

    ClientInfo(String ip, int port, boolean i){
        clientIp = ip;
        clientPort = port;
        isOnline = i;
    }
}

public class Table{
    public HashMap<String, ClientInfo> tbl;

    public Table(){
        tbl = new HashMap<String, ClientInfo>();
    }

    public void dumpTable(){
        ClientInfo ci;
        System.out.println("[Table] dump:");

        for(Map.Entry<String, ClientInfo> entry : tbl.entrySet()){
            ci = entry.getValue();
            System.out.print(entry.getKey() + ", ");
            System.out.print(ci.clientIp + ", ");
            System.out.print(ci.clientPort + ", ");
            System.out.println(ci.isOnline );
        }

    }

    public void insert(String nickName, String ip, int port){
        ClientInfo ci;

        ci = new ClientInfo(ip, port, false);
        if(!tbl.containsKey(nickName)){
            tbl.put(nickName, ci);
        }
        else{
            System.err.println("nickName:" + nickName + "already existed, insert fail!!!!!");
        }
    }
    
    public void delete(String nickName){
        tbl.remove(nickName);
    }
    
    public void onLine(String nickName){
        ClientInfo ci;
        
        ci = tbl.get(nickName);
        ci.isOnline = true;
    }
    
    public void offLine(String nickName){
        ClientInfo ci;
        
        ci = tbl.get(nickName);
        ci.isOnline = false;
    }
}
