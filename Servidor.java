import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CopyOnWriteArrayList;

public class Servidor {
    private static final int PORTA = 5000;
    private static final CopyOnWriteArrayList<Jogador> jogadores =
            new CopyOnWriteArrayList<>();
    private static final Object travaJogo = new Object();
    private static int numeroSecreto;
    private static boolean encerrado = false;
    private static String mensagemFinal;

    public static void main(String[] args) throws IOException {
        // TODO 1: sortear UMA VEZ um inteiro de 0 a 1000, inclusive.
        // Dica: java.util.Random e nextInt(1001).
        try (ServerSocket servidor = new ServerSocket(PORTA)) {
            System.out.println("Servidor na porta " + PORTA);
            while (true) {
                Socket socket = servidor.accept();
                // TODO 2: criar um Jogador e iniciar uma nova Thread.
                // Nesta base, a conexão é fechada até você implementar o TODO.
                socket.close(); // Remova ao implementar o TODO 2.
            }
        }
    }

    private static void enviarParaTodos(String mensagem) {
        // TODO 3: percorrer jogadores e chamar enviar(mensagem).
    }

    private static class Jogador implements Runnable {
        private final Socket socket;
        private final PrintWriter saida;
        private final String ip;
        private final String hostname;

        Jogador(Socket socket) throws IOException {
            this.socket = socket;
            this.saida = new PrintWriter(new OutputStreamWriter(
                    socket.getOutputStream(), StandardCharsets.UTF_8), true);
            this.ip = socket.getInetAddress().getHostAddress();
            this.hostname = socket.getInetAddress().getHostName();
        }

        // Evita que duas threads escrevam simultaneamente neste cliente.
        synchronized void enviar(String mensagem) {
            saida.println(mensagem);
        }

        @Override
        public void run() {
            try (Socket conexao = socket;
                 BufferedReader entrada = new BufferedReader(new InputStreamReader(
                         conexao.getInputStream(), StandardCharsets.UTF_8))) {

                // TODO 4: dentro de synchronized(travaJogo):
                // - Se encerrado, enviar mensagemFinal e retornar.
                // - Caso contrário, adicionar este jogador à lista e dar boas-vindas.

                String linha;
                while ((linha = entrada.readLine()) != null) {
                    // TODO 5: aceitar SAIR; converter o palpite para inteiro.
                    // Tratar texto inválido e valores fora de 0 a 1000.

                    // TODO 6: dentro de synchronized(travaJogo):
                    // - Verificar encerrado ANTES de comparar o palpite.
                    // - Se palpite < numeroSecreto: enviar MAIOR.
                    // - Se palpite > numeroSecreto: enviar MENOR.
                    // - Se igual: marcar encerrado = true, construir mensagemFinal
                    //   com VENCEDOR, ip, hostname e número; enviar para todos.
                    // - Ao encerrar, fechar sockets de todos os jogadores para
                    //   liberar as threads bloqueadas em readLine().
                    // Somente o PRIMEIRO acerto pode definir o vencedor.
                }
            } catch (IOException e) {
                System.out.println("Conexão encerrada: " + ip);
            } finally {
                // TODO 7: remover este jogador da lista.
            }
        }
    }
}
