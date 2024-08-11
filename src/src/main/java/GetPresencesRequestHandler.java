import java.net.*;
import java.io.*;
import java.util.*;

public class GetPresencesRequestHandler extends Thread {
	Socket ligacao;
	Presences presences;
	BufferedReader in;
	PrintWriter out;

	public GetPresencesRequestHandler(Socket ligacao, Presences presences) {
		this.ligacao = ligacao;
		this.presences = presences;
		try
		{	
			this.in = new BufferedReader (new InputStreamReader(ligacao.getInputStream()));
			
			this.out = new PrintWriter(ligacao.getOutputStream());
		} catch (IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}
	}
	
	public void run() {                
		try {
			System.out.println("Aceitou ligacao de cliente no endereco " + ligacao.getInetAddress() + " na porta " + ligacao.getPort());
			
			String response;
			String msg = in.readLine();
			System.out.println("Request=" + msg);
			
			StringTokenizer tokens = new StringTokenizer(msg);
			String metodo = tokens.nextToken();
			if (metodo.equals("get")) {
				response = "101\n";
				String ip = tokens.nextToken();
				Vector<String> ipList = presences.getPresences(ip);
				response += ipList.size() + "\n";
				for (Iterator<String> it = ipList.iterator(); it.hasNext();){
					String next = it.next(); 
					response += next + ";"; 
				}
				System.out.println(response);
				out.println(response);
			}
			else 
				out.println("201;method not found");
				
			out.flush();
			in.close();
			out.close();
			ligacao.close();
		} catch (IOException e) {
			System.out.println("Erro na execucao do servidor: " + e);
			System.exit(1);
		}
	}
}
