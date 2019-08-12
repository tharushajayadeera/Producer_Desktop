package producer;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Tharusha Jayadeera
 */
public class Producer {

    private static Socket socket;
    
    public static void main(String[] args) throws IOException 
    {      
        try
        {
            int port = 4999;
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Server Started and listening to the port ");
            
            //Server is running always. This is done using this while(true) loop
            while(true)
            {
                //Reading the message from the Consumer
                socket = serverSocket.accept();
                InputStream is = socket.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String recievedStringNumberList = br.readLine();
                System.out.println("Whole number recieved from Consumer is : "+recievedStringNumberList);
                
                //Spilt the recieved string and put into an arrayList
                ArrayList<String> NumberArrayList = new ArrayList<>(Arrays.asList(recievedStringNumberList.split(",")));

                String Result="";

                try
                {
                    for (int j = 0; j < NumberArrayList.size(); j++)
                    {
                        //Finding the prime factors for the recieved whole number
                        int number = Integer.parseInt(NumberArrayList.get(j).trim());
                        int i;
                        String returnMessage = "";

                        //Generate Prime Factors
                        ArrayList<Integer> returnValueList = new ArrayList<Integer>();

                        for (i = 2; i < number; i++) {
                            while (number % i == 0) {
                                returnValueList.add(i);
                                number = number / i;
                            }
                        }
                        if (number > 2) {
                            returnValueList.add(number);
                        }

                        //Creating one string returnMessage with all the prime factors generated
                        for (i = 0; i < returnValueList.size(); i++) {
                            returnMessage = returnMessage + returnValueList.get(i) + " x ";
                        }
                        //Remove last x
                        returnMessage = returnMessage.trim().replaceAll(".$","");

                        //Create the final output with all the prime factors corresponding to the Whole number
                        Result = Result + Integer.parseInt(NumberArrayList.get(j)) + " = " + returnMessage + "\t";
                    }
                }
                catch (NumberFormatException e)
                {
                    //When entered numbers are not valid
                    Result = "Please enter whole numbers only\n";
                }
                               
                //Sending the response back to the Consumer.
                OutputStream os = socket.getOutputStream();
                OutputStreamWriter osw = new OutputStreamWriter(os);
                BufferedWriter bw = new BufferedWriter(osw);
                bw.write(Result);
                System.out.println("Prime factors sent from the Producer : "+Result);
                bw.flush();
                socket.close();
            }         
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }   
}
