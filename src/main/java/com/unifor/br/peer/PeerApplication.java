package com.unifor.br.peer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Scanner;

@SpringBootApplication
public class PeerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PeerApplication.class, args);

        Scanner scanner = new Scanner(System.in);

        //Solicitar o nome do usuario
        System.out.println("Digite o nome do usuario: ");
        String userName = scanner.nextLine();

        //Solicitar a porta
        System.out.println("Digite a porta para escutar: ");
        int port = scanner.nextInt();
        scanner.nextLine(); // consumir a nova linha pendente

        //perguntar se deseja conectar a outro peer
        System.out.println("Deseja conectr a outro peer? (S/N)");
        String resposta = scanner.nextLine();

        String peerhost = null;
        int peerPort = 0;

        if (resposta.equalsIgnoreCase("s")){
            System.out.println("Digite o endereço do novo peer (host): ");
            peerhost = scanner.nextLine();

            System.out.println("Digite a porta do peer : ");
            peerPort = scanner.nextInt();
            scanner.nextLine();
        }

        //Iniciar o Peer
        Peer peer = new Peer(userName,port);
        peer.start();

        if (peerhost != null){
            peer.connectionToPeer(peerhost,peerPort);
        }

    }

}
