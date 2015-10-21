package ru.thevhod.picquest.data;


import java.io.Serializable;

public class ServerObj implements Serializable {

    private static final String MESSAGE_IP = "ip address = ";
    private static final String MESSAGE_NAME = "hostname = ";
    private static final String MESSAGE_MAC = "mac address = ";
    private String ipAddress;

    private String macAddress;

    private String name;

    public ServerObj() {
    }

    public ServerObj(String stringToParse) {
        int endIndex;
        endIndex = stringToParse.indexOf(",");
        ipAddress = stringToParse.substring(stringToParse.indexOf(MESSAGE_IP) + MESSAGE_IP.length(), endIndex);
        stringToParse = stringToParse.substring(endIndex+2);
        endIndex = stringToParse.indexOf(",");
        name = stringToParse.substring(stringToParse.indexOf(MESSAGE_NAME)+MESSAGE_NAME.length(), endIndex);
        stringToParse = stringToParse.substring(endIndex+2);
        macAddress= stringToParse.substring(stringToParse.indexOf(MESSAGE_MAC)+MESSAGE_MAC.length());
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
