import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class TrataEleitor implements Runnable {

    private Socket soquete_cliente;
    private ObjectOutputStream saida;
    private ObjectInputStream entrada;
    
    // Variaveis utilizadas para Armazenar os votos 
    static int voto1 = 0, voto2 =0, votob = 0, voton = 0;  

    //ArrayList para armazenar os CPFs
    static ArrayList<String> cpf = new ArrayList<String>();

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

            // recebe o CPF tranforma em string e guarda numa variavel mensagem do tipo string
            String mensagem = (String)receber_mensagem();

            //variavel para testar se O CPF não foi repetido 
            boolean confere = false;
                // Loop de Verificação de CPF
                for (int i = 0; i < cpf.size(); i++) {
                    if (cpf.get(i).equals(mensagem)) {
                        enviar_mensagem("CPF Já utilizado ou Invalido:");
                        confere = true;
                    }
                }
            if(mensagem.equals("")){
                enviar_mensagem("CPF Já utilizado ou Invalido:");
                confere = true;
            } 
            // Condicinal em caso de cpf não estiver repetido 
            if(confere == false){

                enviar_mensagem("CPF AUTORIZADO");

                // Inserir voto
                String escolha = (String) receber_mensagem();

                // Transforma o voto que vem como string em inteiro
                int voto = Integer.parseInt(escolha);

                //Faz a verificação dos votos e retorna a mensagem referente ao voto
                if( voto == 17){
                    voto1 += 1 ;
                    enviar_mensagem("Obrigado pelo Voto");
                }else if(voto == 13){
                    voto2 += 1 ;
                    enviar_mensagem("Obrigado pelo Voto");
                }else if(voto == 0){
                    votob += 1; 
                    enviar_mensagem("Voto Branco");
                }else{
                    voton +=1;
                    enviar_mensagem("Voto anulado");
                }

                // Adiciona o CPF no ArrayList
                 cpf.add(mensagem);
               
                // Apenas controle do Servidor
                System.out.println("----------- LISTA DOS CPFS CADASTRADOS------------");

                //Verifica e mostra todos os CPFs cadastrados no Array CPF
                for (int i = 0; i < cpf.size(); i++) {
                    System.out.println(cpf.get(i));
                }
                System.out.println("**************************************************");
                System.out.println("----------- LISTA DOS CPFS CANDIDATOS-------------");
                System.out.println("Boisonaro: " + voto1 + " votos \nLulete: "+ voto2 + " votos");
                System.out.println("Branco: " + votob + " votos");
                System.out.println("Nulos: " + voton + " votos");
                System.out.println("**************************************************");
            }
            finalizar();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}