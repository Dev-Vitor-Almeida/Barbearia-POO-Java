package model;

public class Cliente extends Pessoa{
    private String cpf;
    private int id;


    public Cliente(String nome, String telefone, String cpf, String email, String senha) {

        super(nome, telefone, email, senha);

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
                "nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}

