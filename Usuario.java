import java.util.ArrayList;
import java.util.List;  

public class Usuario {
    private String nome;
    private String email;
    private List<Evento> eventosConfirmados;

    public Usuario(String nome, String email) {
        this.nome = nome;
        this.email = email;
        this.eventosConfirmados = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public List<Evento> getEventosConfirmados() {
        return eventosConfirmados;
    }

    public void confirmarPresenca(Evento evento) {
        if (!eventosConfirmados.contains(evento)) {
            eventosConfirmados.add(evento);
            System.out.println("Presença confirmada no evento: " + evento.getNome());
        } else {
            System.out.println("Você não havia confirmado presença neste evento.");
        }
    }

     public void cancelarPresenca(Evento evento) {
        if (eventosConfirmados.contains(evento)) {
            eventosConfirmados.remove(evento);
            System.out.println("Presença cancelada no evento: " + evento.getNome());
        } else {
            System.out.println("Você não havia confirmado presença neste evento para poder cancelar.");
        }
    }

    @Override
    public String toString() {
        return "Usuário: " + nome + " (Email: " + email + ")";
    }
} 
