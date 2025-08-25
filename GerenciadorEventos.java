import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class GerenciadorEventos {
    private static List<Evento> todosOsEventos = new ArrayList<>();
    private static List<Usuario> todosOsUsuarios = new ArrayList<>();
    private static Usuario usuariologado = null;
    private static final String NOME_ARQUIVO = "events.data";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        carregarEventosDoArquivo();

        int opcao = -1;
        while (opcao != 0) {
            exibirMenu();
            try {
                opcao = Integer.parseInt(scanner.nextLine());
                roteador(opcao);
            } catch (NumberFormatException e) {
                System.out.println("Erro: Por favor, digite um número válido.");
            }
        }
        System.out.println("Obrigado por usar o sistema de eventos!");
    }

    private static void exibirMenu() {
        System.out.println("\n===== MENU DE EVENTOS =====");
        if (usuariologado == null) {
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Fazer Login");
        } else {
            System.out.println("Olá, " + usuariologado.getNome() + "!");
            System.out.println("---------------------------");
            System.out.println("3. Cadastrar Novo Evento");
            System.out.println("4. Listar Todos os Eventos");
            System.out.println("5. Participar de um Evento");
            System.out.println("6. Ver Meus Eventos Confirmados");
            System.out.println("7. Cancelar Participação");
            // CORREÇÃO: Adicionado o número 8 para a opção de Logout
            System.out.println("8. Logout");
        }
        System.out.println("0. Sair do Programa");
        System.out.print("Escolha uma opção: ");
    }

    // CORREÇÃO: Lógica do roteador foi completada com o bloco "else"
    private static void roteador(int opcao) {
        if (usuariologado == null) { // Menu para usuário deslogado
            switch (opcao) {
                // CORREÇÃO: Corrigido o erro de digitação de "cadastar" para "cadastrar"
                case 1: cadastrarUsuario(); break;
                case 2: fazerLogin(); break;
                case 0: break;
                default: System.out.println("Opção Inválida!");
            }
        } else { // Menu para usuário logado
            switch (opcao) {
                case 3: cadastrarEvento(); break;
                case 4: listarEventos(); break;
                case 5: participarDeEvento(); break;
                case 6: listarMeusEventos(); break;
                case 7: cancelarParticipacao(); break;
                // CORREÇÃO: Adicionado o case para a opção de Logout
                case 8: usuariologado = null; System.out.println("Logout realizado com sucesso!"); break;
                case 0: break;
                default: System.out.println("Opção Inválida!");
            }
        }
    }

    // CORREÇÃO: Corrigido o erro de digitação no nome do método
    private static void cadastrarUsuario() {
        System.out.print("Digite seu nome: ");
        String nome = scanner.nextLine();
        System.out.print("Digite seu email: ");
        String email = scanner.nextLine();
        todosOsUsuarios.add(new Usuario(nome, email));
        System.out.println("Usuário cadastrado com sucesso!");
    }

    private static void fazerLogin() {
        System.out.print("Digite seu email para login: ");
        String email = scanner.nextLine();
        for (Usuario u : todosOsUsuarios) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                usuariologado = u;
                System.out.println("Login bem-sucedido!");
                return;
            }
        }
        System.out.println("Usuário não encontrado.");
    }

    private static void cadastrarEvento() {
        try {
            System.out.println("--- Cadastro de Evento ---");
            System.out.print("Nome do evento: ");
            String nome = scanner.nextLine();
            System.out.print("Endereço: ");
            String endereco = scanner.nextLine();
            System.out.print("Categoria (Festa, Show, Esportivo): ");
            String categoria = scanner.nextLine();
            System.out.print("Horário (formato AAAA-MM-DDTHH:MM, ex: 2025-10-31T22:00): ");
            LocalDateTime horario = LocalDateTime.parse(scanner.nextLine(), formatter);
            System.out.print("Descrição: ");
            String descricao = scanner.nextLine();

            Evento novo = new Evento(nome, endereco, categoria, horario, descricao);
            todosOsEventos.add(novo);
            salvarEventosNoArquivo(); // Agora este método existe
            System.out.println("Evento cadastrado com sucesso!");
        } catch (DateTimeParseException e) {
            System.out.println("Formato de data e hora inválido. Use o formato AAAA-MM-DDTHH:MM");
        }
    }

    private static void listarEventos() {
        if (todosOsEventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
            return;
        }

        todosOsEventos.sort(Comparator.comparing(Evento::getHorario));
        LocalDateTime agora = LocalDateTime.now();

        System.out.println("\n--- Próximos Eventos ----");
        todosOsEventos.stream()
            .filter(e -> e.getHorario().isAfter(agora))
            .forEach(e -> System.out.println(e + "\n"));

        System.out.println("\n--- Eventos Ocorrendo Agora ---");
        todosOsEventos.stream()
            .filter(e -> e.getHorario().isBefore(agora) && e.getHorario().plusHours(3).isAfter(agora))
            .forEach(e -> System.out.println(e + "\n"));

        System.out.println("\n--- Eventos Passados ---");
        todosOsEventos.stream()
            .filter(e -> e.getHorario().plusHours(3).isBefore(agora))
            .forEach(e -> System.out.println(e + "\n"));
    }

    private static void participarDeEvento() {
        System.out.print("Digite o nome do evento para confirmar presença: ");
        String nome = scanner.nextLine();
        for (Evento e : todosOsEventos) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                usuariologado.confirmarPresenca(e);
                return;
            }
        }
        System.out.println("Evento não encontrado.");
    }

    private static void listarMeusEventos() {
        List<Evento> meusEventos = usuariologado.getEventosConfirmados();
        if (meusEventos.isEmpty()) {
            System.out.println("Você não confirmou presença em nenhum evento.");
        } else {
            System.out.println("--- Meus Eventos ---");
            meusEventos.forEach(e -> System.out.println(e + "\n"));
        }
    }

    private static void cancelarParticipacao() {
        System.out.print("Digite o nome do evento para cancelar a presença: ");
        String nome = scanner.nextLine();
        Evento paraCancelar = null;
        for (Evento e : usuariologado.getEventosConfirmados()) {
            if (e.getNome().equalsIgnoreCase(nome)) {
                paraCancelar = e;
                break;
            }
        }
        if (paraCancelar != null) {
            usuariologado.cancelarPresenca(paraCancelar);
        } else {
            System.out.println("Você não está confirmado neste evento.");
        }
    }

    // CORREÇÃO: Método que faltava foi implementado
    private static void salvarEventosNoArquivo() {
        try (PrintWriter out = new PrintWriter(new FileWriter(NOME_ARQUIVO))) {
            for (Evento evento : todosOsEventos) {
                // Supondo que a classe Evento tenha um método paraFormatoArquivo()
                out.println(evento.paraFormatoArquivo());
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar no arquivo: " + e.getMessage());
        }
    }

    private static void carregarEventosDoArquivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(NOME_ARQUIVO))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.split(";");
                if (partes.length == 5) {
                    Evento evento = new Evento(
                        partes[0],
                        partes[1],
                        partes[2],
                        LocalDateTime.parse(partes[3], formatter),
                        partes[4]
                    );
                    todosOsEventos.add(evento);
                }
            }
        } catch (IOException e) {
            System.out.println("Arquivo de dados não encontrado. Será criado um novo ao salvar o primeiro evento.");
        } catch (DateTimeParseException e) {
            // CORREÇÃO: Adicionado o "+" que faltava para concatenar a string
            System.err.println("Erro ao ler data do arquivo: " + e.getMessage());
        }
    }
}