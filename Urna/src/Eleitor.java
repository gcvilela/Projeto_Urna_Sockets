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
        boolean x = false; 
        while( x == false){
            String cpf = JOptionPane.showInputDialog("Qual seu cpf?");
            
            Eleitor cliente = new Eleitor("127.0.0.1", 15500);
            cliente.enviar_mensagem(cpf);

            String resposta = (String) cliente.receber_mensagem();

            if(resposta.equals("CPF Já utilizado:")){

                JOptionPane.showMessageDialog(null,resposta);
                
                cliente.finalizar();
                
            }else{
                JOptionPane.showMessageDialog(null,resposta);

                String inputValue = JOptionPane.showInputDialog("Em qual Candidato voce quer votar? \n 17 - Boisonaro \n 13 - Lulete \n 00 - Branco \n Outro valor sera Nulo");
                //inputValue = JOptionPane.showInputDialog("");
                // Object[] options = { “OK”, “CANCELAR”,”VOLTAR” };
                // JOptionPane.showOptionDialog(null, “Clique OK para continuar”, “Aviso”,
                // JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE,
                // null, options, options[0]);

                cliente.enviar_mensagem(inputValue);
                resposta = (String)cliente.receber_mensagem();
                JOptionPane.showMessageDialog(null,resposta);

                cliente.finalizar();
                x = true;
            }
        }
    }
}
