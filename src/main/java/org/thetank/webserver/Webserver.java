package org.thetank.webserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.StringTokenizer;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 *  Simple HTTP server for a coding challenge
 */
public class Webserver implements Runnable{ 
	
	static final File CWD = new File(".");
	static final String API_ENDPOINT = "/api/encode";
	static final int PORT = 23456;
	private Socket connect;

	enum MethodType{
		GET, HEAD, POST, PUT, DELETE, CONNECT, OPTION, TRACE 
	}
	
	public Webserver(Socket socket) {
		connect = socket;
	}
	
	public static void main(String[] args) {
		ServerSocket serverConnect = null;
		try {
			serverConnect = new ServerSocket(PORT);
			System.out.println("Server started.\nListening on port : " + PORT + " ...\n");
			
			//Endless loop since this is a server just waiting for connections
			while (true) {
				Webserver server = new Webserver(serverConnect.accept());
				System.out.println("Connecton opened. (" + new Date() + ")");
							
				// Thread for each connection
				Thread thread = new Thread(server);
				thread.start();
			}
			
		} catch (IOException e) {
			System.err.println("Server Connection Error : " + e.getMessage());
		} finally{
			try {
				serverConnect.close();
			} catch (IOException e) {
				System.err.println("Error closing stream : " + e.getMessage());
			}
		}
	}

	@Override
	public void run() {
		BufferedReader in = null; 
		PrintWriter out = null; 
		BufferedWriter writer = null;
		String apiRequested = null;
		JsonObject jsonReturnObj = new JsonObject();
		jsonReturnObj.addProperty("EncodedMessage", "");
		
		try {
			// input stream from client
			in = new BufferedReader(new InputStreamReader(connect.getInputStream()));
			// output stream to client
			out = new PrintWriter(connect.getOutputStream());
			//Writer used to write cipher to disk
			FileWriter fileWriter = new FileWriter("cipher.txt");
			writer = new BufferedWriter(fileWriter);
			// get first line of the request from the client
			String input = in.readLine();
			// we parse the request with a string tokenizer
			StringTokenizer parse = new StringTokenizer(input);
			// Get the HTTP method of the client
			MethodType method = Webserver.MethodType.valueOf(parse.nextToken().toUpperCase()); 
			// Get requested endpoint
			apiRequested = parse.nextToken().toLowerCase();
			
			// currently only support POST, return 501 otherwise
			switch(method){
				case POST:
					if(apiRequested.equals(API_ENDPOINT)){
						//Correct Method and Endpoint
						//Lets read  rest off the headers, should probably do something with them but dont really need them for this exercise
						while((in.readLine()).length() != 0){
						}
						StringBuilder content = new StringBuilder();
						while(in.ready()){
							content.append((char) in.read());
						}

						//Validate and Parse the JSON
						JsonObject jsonObject = new JsonParser().parse(content.toString()).getAsJsonObject();
						int shift = jsonObject.get("Shift").getAsInt();
						String message = jsonObject.get("Message").getAsString();
						ShiftCipher shiftCipher = new ShiftCipher(shift, message);
						String cipherMessage = shiftCipher.execute();
						//JSON validated now process cipher and create return object
						jsonReturnObj.addProperty("EncodedMessage", cipherMessage);
						// send HTTP Headers
						ResponseHandler response = new ResponseHandler(out, "200 OK", jsonReturnObj.toString());
						response.sendResponse();

						//Write cipher to file
						
						writer.write(cipherMessage);
						
					} else { 
						// Unknown Endpoint
						ResponseHandler response = new ResponseHandler(out, "500 Unknown Endpoint", jsonReturnObj.toString());
						response.sendResponse();
					}
					break;
				default:
					//Currently do not support anything other than POST
					System.out.println("501 Not Implemented : " + method.toString() + " method.");
					ResponseHandler response = new ResponseHandler(out, "501 Not Implemented", jsonReturnObj.toString());
					response.sendResponse();
					break;
			}
			
		} catch (IOException e) {
			System.err.println("IO error : " + e);
			ResponseHandler response = new ResponseHandler(out, "500 IO Error", jsonReturnObj.toString());
			response.sendResponse();
		} catch (NumberFormatException e){
			System.err.println("NFE error : " + e);
			ResponseHandler response = new ResponseHandler(out, "500 NFE Error", jsonReturnObj.toString());
			response.sendResponse();
		} catch (JsonParseException e){
			System.err.println("Parser error : " + e);
			ResponseHandler response = new ResponseHandler(out, "500 Parser Error", jsonReturnObj.toString());
			response.sendResponse();
		} catch (CipherException e) {
			System.err.println("Cipher error : " + e);
			ResponseHandler response = new ResponseHandler(out, "500 Cipher Error", jsonReturnObj.toString());
			response.sendResponse();
		} finally {// Clean up the Reader, Writers, and connection
			try {
				in.close();
				out.close();
				writer.close();
				connect.close(); // we close socket connection
			} catch (Exception e) {
				System.err.println("Error closing stream : " + e.getMessage());
			} 
			System.out.println("Connection closed.\n");
		}
		
	}
	
}