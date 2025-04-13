package ru.primer.primeapi.tcp;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TCPClient {

  String host;
  int port;

  public String sendTCPPacket(String message) {
    try (Socket socket = new Socket(host, port);
         PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
      out.println(message);
      return in.readLine();
    } catch (IOException e) {
      System.out.println(e.getMessage());
    }

    return null;
  }
}
