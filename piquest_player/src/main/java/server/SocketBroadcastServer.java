package server;


import java.io.IOException;
import java.net.*;

public class SocketBroadcastServer extends Thread {

    protected DatagramSocket socket = null;
    private static final String MESSAGE_FORMAT = "ip address = %s, hostname = %s, mac address = %s";
    private boolean run;

    public SocketBroadcastServer(String name) throws IOException {
        super(name);
        socket = new DatagramSocket(1900);
    }

    @Override
    public synchronized void start() {
        super.start();
        run = true ;
    }

    public void stopServer() {
        run = false;
    }

    @Override
    public void run() {
        while (run) {
            try {
                byte[] buf = new byte[256];

                String dString = getComputerInfo().concat("$$$");
                buf = dString.getBytes();

                InetAddress group = InetAddress.getByName("239.255.255.250");
                DatagramPacket packet;
                packet = new DatagramPacket(buf, buf.length, group, 1900);
                socket.send(packet);
                System.out.println("sended: " + dString);
                try {
                    sleep(1000*2);
                } catch (InterruptedException e) {
                }
            }catch (Exception e){
                socket.close();
            }
        }
    }

    private String getComputerInfo() {
        String ipAddress = "Unknown";
        String hostname = "Unknown";
        String macAddress = "Unknown";
        try {
            InetAddress addr;
            addr = InetAddress.getLocalHost();
            hostname = addr.getHostName();
            ipAddress = addr.getHostAddress();
            addr.getAddress();
            NetworkInterface network = NetworkInterface.getByInetAddress(addr);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            macAddress = sb.toString();

        } catch (UnknownHostException e) {
            System.out.println("Hostname can not be resolved");
        } catch (SocketException e) {
            System.out.println("MAC address can not be resolved");
        }
        return String.format(MESSAGE_FORMAT, ipAddress, hostname, macAddress);
    }
}