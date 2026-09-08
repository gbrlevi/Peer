package com.unifor.br.peer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Peer {
    private String username;
    private ServerSocket serverSocket;
    private List<Socket> connections = new ArrayList<>();

    public Peer(String username, int port){
        this.username = username;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Peer : " +username + " esta ouvindo na porta "+port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void start(){
        new Thread(this::listenForconnection).start();
        new Thread(this::listenForUserInput).start();
    }

    private void listenForconnection() {
        while (true){
            try{
                Socket socket = serverSocket.accept();
                connections.add(socket);
                new Thread(()->handleConnections(socket)).start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void handleConnections(Socket socket) {
        try(BufferedReader in =
                    new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
            String mensagem;
            while ((mensagem = in.readLine())!= null){
                System.out.println(mensagem);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void listenForUserInput() {
        try (BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))){
            while (true){
                String mensagem = userInput.readLine();
                broadCastMessage(mensagem);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void broadCastMessage(String mensagem) {
        for (Socket socket: connections){
            try {
                PrintWriter out = new PrintWriter(socket.getOutputStream(),true);
                out.println(username + ": "+mensagem);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void connectionToPeer(String host, int port){
        try {
            Socket socket = new Socket(host,port);
            connections.add(socket);
            new Thread(()-> handleConnections(socket)).start();
            System.out.println("conectado ao Peer: "+host + "na porta: "+port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
