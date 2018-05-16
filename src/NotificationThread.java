import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.io.IOException;
import java.net.DatagramSocket;
import java.io.*;
import java.util.*;

public class NotificationThread implements Runnable {
    //Notification Join Channel
    private static NotificationJoinChannel notificationJoinChannel;
    private static String ip_not_join = "224.0.0.7";
    private static Integer port_not_join = 8888;
    
    private static String email;

    public NotificationThread(String email){
        this.email = email;
        setNotificationJoinTravelChannel();

    }

    public static void setNotificationJoinTravelChannel() {
        try{
            notificationJoinChannel = new NotificationJoinChannel(ip_not_join, port_not_join);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            notificationJoinChannel.receiveMessage(email);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}