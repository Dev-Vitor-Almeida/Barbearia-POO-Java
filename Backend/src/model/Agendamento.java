package model;

public class Agendamento {
    private Cliente cliente;
    private Barbeiro barbeiro;
    private Servico servico;
    private String data;
    private String horario;

    public Agendamento(Cliente cliente, Barbeiro barbeiro, Servico servico, String data, String horario) {
        this.cliente = cliente;
        this.barbeiro = barbeiro;
        this.servico = servico;
        this.data = data;
        this.horario = horario;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Barbeiro getBarbeiro() {
        return barbeiro;
    }

    public void setBarbeiro(Barbeiro barbeiro) {
        this.barbeiro = barbeiro;
    }

    public Servico getServico() {
        return servico;
    }

    public void setServico(Servico servico) {
        this.servico = servico;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    @Override
    public String toString() {
        return "Agendamento{" +
                "cliente=" + cliente +
                ", barbeiro=" + barbeiro +
                ", servico=" + servico +
                ", data='" + data + '\'' +
                ", horario='" + horario + '\'' +
                '}';
    }
}
