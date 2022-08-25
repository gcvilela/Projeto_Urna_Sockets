package Urna;

import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TrataEleitor implements Runnable {

		private Socket soquete_cliente;
		private ObjectOutputStream saida;
		private ObjectInputStream entrada;
		static int voto1 = 0, voto2 =0;
		static ArrayList cpf = new ArrayList<String>();
		
		
		public TrataEleitor(Socket soquete_cliente) throws Exception {
			super();
			this.soquete_cliente = soquete_cliente;
			this.saida = new ObjectOutputStream(this.soquete_cliente.getOutputStream()); 
			this.entrada = new ObjectInputStream(this.soquete_cliente.getInputStream());
		}
		
		public void enviar_mensagem(Object mensagem) throws Exception {
			this.saida.writeObject(mensagem);
		}
		
		public Object receber_mensagem() throws Exception {
			return this.entrada.readObject();
		}
		
		public void finalizar() throws IOException {
			this.soquete_cliente.close();
		}

		@Override
		public void run() {
			try {
				
				// Leu o CPF e verificou
				String mensagem = (String)receber_mensagem();
				
				if(cpf.size()== 0){
					cpf.add(mensagem);
				}else{
					for (int i = 0; i < cpf.size(); i++) {
						if(cpf.get(i)==mensagem){
							enviar_mensagem("CPF Já utilizado:");
							finalizar();
						}
					}
					cpf.add(mensagem);
				}
					
				
				
				enviar_mensagem("CPF cadastrado");

				

				for (int i = 0; i < cpf.size(); i++) {
					System.out.println(cpf.get(i));
				}

				System.out.println(cpf.size());
				
				// Inserir voto 
				String escolha = (String) receber_mensagem();

				int voto = Integer.parseInt(escolha);
				if( voto == 17){
					voto1 += 1 ;
				}else if(voto == 13){
					voto2 += 1 ;
				}else{
					enviar_mensagem("Voto não reconhecido");
					finalizar();
				}
			
				enviar_mensagem("Obrigado pelo Voto");

				System.out.println("O  Boisonaro teve :" + voto1 + " votos, e o Lulete:  "+ voto2 + " votos");

				finalizar();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
