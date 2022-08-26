import java.net.Socket;
import java.net.ServerSocket;
import java.io.IOException;

public class Servidor {

    public static Object cpf;
    private ServerSocket soquete_servidor;

    public Servidor(int porta) throws Exception {
        super();
        this.soquete_servidor = new ServerSocket(porta);
    }

    public void finalizar() throws IOException {
        this.soquete_servidor.close();
    }

    public static void main(String[] args) throws Exception {
        //Cria variavel com a servidor  
        Servidor servidor = new Servidor(15500);

        Socket soqueteCliente = null;

        while (true) {
            try {

                //Aceita a nova conexao, acria e inicia uma nova thread
                soqueteCliente = servidor.soquete_servidor.accept();
                new Thread( new TrataEleitor(soqueteCliente)).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
