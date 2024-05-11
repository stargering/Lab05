import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.ServerSocket;

public class Main {
    public static void main(String[] args) {

        Thread clientThread = new Thread(() -> {
            String hostName = "localhost"; // Server IP address
            int port = 8000; // Server port

            System.out.println("Connecting to server on port " + port);

            try (Socket echoSocket = new Socket(hostName, port);
                 PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                 BufferedReader stdInput = new BufferedReader(new InputStreamReader(System.in))) {

                // Thread to handle server messages
                Thread readThread = new Thread(() -> {
                    try {
                        String serverMessage;
                        while ((serverMessage = in.readLine()) != null) {
                            System.out.println("Message from server: " + serverMessage);
                        }
                    } catch (IOException e) {
                        System.out.println("Error reading from server: " + e.getMessage());
                    }
                });

                readThread.start();

                System.out.println("Enter your message:");
                String userInput;
                while ((userInput = stdInput.readLine()) != null) {
                    out.println(userInput);
                    System.out.println("Echo from server: " + in.readLine());


                }
            } catch (IOException e) {
                System.out.println("Couldn't get I/O for the connection to " +
                        hostName);
                System.out.println(e.getMessage());
            }
        });



        Thread serverThread = new Thread(() -> {
            int port = 8000; // Default port number
            System.out.println("Server is starting...");

            try (ServerSocket serverSocket = new ServerSocket(port)) {
                System.out.println("Listening on port " + port);
                clientThread.start();




                while (true) {
                    try (Socket clientSocket = serverSocket.accept()) {
                        System.out.println("Client connected.");


                        BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

                        String inputLine;
                        while ((inputLine = in.readLine()) != null) {
                            System.out.println("Received from client: " + inputLine);
                            out.println("Echoing back: " +inputLine); // Echo the same line back to the client



                            PrintWriter out2 = new PrintWriter(clientSocket.getOutputStream(), true);
                            BufferedReader serverInput = new BufferedReader(new InputStreamReader(System.in));

                            System.out.println("Enter messages to send to client:");
                           // String message;

                            out2.println(serverInput.readLine());



                        }


                        // Setting up output to client and input from server console

                    } catch (IOException e) {
                        System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
                        System.out.println(e.getMessage());
                    }
                }
            } catch (IOException e) {
                System.out.println("Could not listen on port: " + port);
                System.out.println(e.getMessage());
            }
        });


        serverThread.start();
//

      //  t.start();
    }
}