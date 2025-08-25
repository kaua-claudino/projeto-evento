import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Evento {
    private String nome;
    private String endereco;
    private String categoria;
    private LocalDateTime horario;
    private String descricao;

    public Evento(String nome, String endereco, String categoria, LocalDateTime horario, String descricao) {
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
}

public String getNome() {
    return nome;
}

public String getEndereco() {
    return endereco;
}

public String getCategoria() {
    return categoria;
}

public LocalDateTime getHorario() {
    return horario;
}

public String getDescricao() {
    return descricao;
}

@Override
public String toString() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    return "Evento: " + nome +
           "\n Endereco: " + endereco +
           "\n Categoria: " + categoria +
           "\n Horário: " + horario.format(formatter) +
           "\n Descrição: " + descricao;
}

public String paraFormatoArquivo() {
    DateTimeFormatter formatterArquivo = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    return String.join(";",
        this.nome,
        this.endereco,
        this.categoria,
        this.horario.format(formatterArquivo),
        this.descricao
    );
  }
}
