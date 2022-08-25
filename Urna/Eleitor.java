package Urna;

import javax.swing.JOptionPane;
import java.net.Socket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Eleitor {
	
	private Socket soquete;
	private ObjectOutputStream saida;
	private ObjectInputStream entrada;
	public Object soquete_servidor;
	
	public Eleitor(String endereco, int porta) throws Exception {
		super();
		this.soquete = new Socket(endereco, porta);
		this.saida = new ObjectOutputStream(this.soquete.getOutputStream()); 
		this.entrada = new ObjectInputStream(this.soquete.getInputStream());
	}

	public void enviar_mensagem(Object mensagem) throws Exception {
		this.saida.writeObject(mensagem);
	}
	
	public Object receber_mensagem() throws Exception {
		return this.entrada.readObject();
	}
	
	public void finalizar() throws IOException {
		this.soquete.close();
	}
	
	public static void main(String[] args) throws Exception {
		JOptionPane.showMessageDialog(null, "Bem vindo ao sistema Votaçao Online");
		int ans = JOptionPane.showConfirmDialog(null, "Deseja votar Agora?");
        if (ans == JOptionPane.YES_OPTION) {

			Eleitor cliente = new Eleitor("127.0.0.1", 15500);

			String cpf = JOptionPane.showInputDialog("Qual seu cpf?");

			cliente.enviar_mensagem(cpf);
			System.out.println((String)cliente.receber_mensagem());
			// if ((String)cliente.receber_mensagem()=="CPF Já utilizado:"){

			// }
			String inputValue = JOptionPane.showInputDialog("Em qual Candidato voce quer votar? \n - 17 - Boisonaro \n - 13 - Lulete");

			cliente.enviar_mensagem(inputValue);
			System.out.println((String)cliente.receber_mensagem());
		
			cliente.finalizar();
		}
		
	}

}
