/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankingsystemclient;

/**
 *
 * @author HP
 */
import EncryptionAlgorithm.AES;
import EncryptionAlgorithm.GenerateKeys;
import com.google.gson.Gson;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.Scanner;
import model.Client;
import model.Response;
import model.Transfer;

public class BankingSystemClient {

    GenerateKeys gk_Client;
    private Socket socket;
    final String secretKey = "Cap10";
    public static ObjectInputStream networkInput;
    public static ObjectOutputStream networkOutput;

    private BankingSystemClient(String serverAddress, int serverPort) throws Exception {
        this.socket = new Socket(serverAddress, serverPort);
        networkOutput = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        networkInput = new ObjectInputStream(socket.getInputStream());
        gk_Client = new GenerateKeys(1024);
        gk_Client.createKeys();
        // System.out.println(gk_Client.getPrivateKey() );
        //System.out.println(gk_Client.getPublicKey());
    }

    private void start() throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException {
        //networkOutput.writeObject(gk_Client.getPublicKey());
        //networkOutput.flush();
        //PublicKey PublicKey = (PublicKey) networkInput.readObject();
        //System.out.println("Public key is :" + PublicKey);

        Client input;
        Scanner s = new Scanner(System.in);
        System.out.println("Enter your name");
        String userName = s.nextLine();
        System.out.println("Username is: " + userName);
        System.out.println("Enter your Password");
        String password = s.nextLine();
        input = new Client(userName, password);
        networkOutput.writeObject(input);
        networkOutput.flush();
        Response Res = (Response) networkInput.readObject();
        System.out.println("Response is :" + Res.isSuccess());
        PublicKey PublicKey = (PublicKey) networkInput.readObject();
        System.out.println("server Public key is :" + PublicKey);

        while (true) {
            if (Res.isSuccess()) {
                System.out.println("Enter your reciever ID");
                int Reciever = s.nextInt();
                System.out.println("Enter Balance");
                int Balance = s.nextInt();
                System.out.println("Enter Description");
                s.nextLine();
                String Description = s.nextLine();
                Transfer MyTransfer = new Transfer(Reciever, Balance, Description);

                
                //generate session key
                SecureRandom r=new SecureRandom();
                byte[] aeskey =new byte[16];
                r.nextBytes(aeskey);
                //AES.setkeybyte(aeskey);
                
                
                
                
                //convert Object Transfer to Json
                Gson gson = new Gson();
                String TransferToJson = gson.toJson(MyTransfer);
                System.out.println(TransferToJson);
                
                ///encrypt transfer by secret key
              //  String encryptedTransfer = AES.encrypt(TransferToJson, secretKey);
                String encryptedTransfer = AES.encrypt(TransferToJson, aeskey.toString());
                System.out.println(encryptedTransfer);
                
                //encrypt secret key by public key server
                String encryptedkey = AES.encrypt2(secretKey, PublicKey);
                networkOutput.writeObject(encryptedTransfer);
                networkOutput.flush();
                networkOutput.writeObject(encryptedkey);
                networkOutput.flush();

                Response Result = (Response) networkInput.readObject();
                System.out.println("Response is :" + Result.getMessage());

            }
//            if (userName.equalsIgnoreCase("Roula")) {
//                System.out.println("blala");



        }
    }

    public static void main(String[] args) throws Exception {

        BankingSystemClient client = new BankingSystemClient("192.168.43.115", 3001);

        System.out.println("\r\nConnected to Server: " + client.socket.getInetAddress());
        client.start();
    }

}
