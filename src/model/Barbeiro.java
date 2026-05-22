package model;

public class Barbeiro extends Pessoa {

    private String especialidade;
    private int id;

    public Barbeiro(String nome, String telefone, String email, String especialidade, String senha) {
        super(nome, telefone, email, senha);
        this.especialidade = especialidade;
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return "Barbeiro{" +
                "nome='" + nome + '\'' +
                ", especialidade='" + especialidade + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public void mostrarInformacoes(Agendamento agendamento){

        System.out.println(
                "\n=== AGENDA DO BARBEIRO ===" +
                "\nCliente: " + agendamento.getCliente().getNome() +
                "\nServiço: " + agendamento.getServico().getNome()
        );
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
