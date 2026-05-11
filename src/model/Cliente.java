package model;

public class Cliente extends Pessoa{
    private String cpf;


    public Cliente(String nome, String telefone, String cpf, String email) {

        super(nome, telefone, email);

        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "cpf='" + cpf + '\'' +
                ", nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public void mostrarInformacoes(Agendamento agendamento){

        System.out.println(
                "\n=== AGENDAMENTO DO CLIENTE ===" +
                "\nBarbeiro: " +agendamento.getBarbeiro().getNome() +
                "\nServiço: " + agendamento.getServico().getNome() +
                "\nData: " + agendamento.getData() +
                "\nHorário: " + agendamento.getHorario()
        );
    }
}

