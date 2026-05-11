package model;

public class Barbeiro extends Pessoa {

    private String especialidade;

    public Barbeiro(String nome, String telefone, String email, String especialidade) {
        super(nome, telefone, email);
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
                "especialidade='" + especialidade + '\'' +
                ", nome='" + nome + '\'' +
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
}
