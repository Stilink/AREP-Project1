package edu.eci.arep.aws;

import java.net.*;

public class URLReader {
    public static void main(String[] args) throws Exception {
        URL url = new URL(args[0]);
        int n = Integer.parseInt(args[1]);
        for (int i = 0; i < n; i++) {
            ClientThread th = new ClientThread(url);
            th.start();
        }
    }
}