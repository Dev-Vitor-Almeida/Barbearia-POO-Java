package service;
import model.*;
import model.Servico;
import java.util.ArrayList;

public class AgendamentoService {
    public void listarAgendamentos(ArrayList<Agendamento> agendamentos){
        System.out.println("\n=== AGENDAMENTOS CADASTRADOS ===");

        for (Agendamento a : agendamentos) {
            System.out.println(a);
        }
    }

    public void buscarPorCliente(ArrayList<Agendamento> agendamentos, String nomeBusca){
        boolean encontrado = false;

        System.out.println("\n=== BUSCANDO AGENDAMENTO ===");
        for (Agendamento a : agendamentos) {
            if (a.getCliente().getNome().equals(nomeBusca)) {
                System.out.println("Agendamento encontrado.");
                System.out.println(a);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Nenhum agendamento encontrado para o cliente: " + nomeBusca);
        }
    }

    public void cancelarAgendamento(ArrayList<Agendamento> agendamentos, String nomeCancelamento){
        boolean removido = false;

        System.out.println("\n=== CANCELANDO AGENDAMENTO ===");

        for (Agendamento a : agendamentos) {
            if (a.getCliente().getNome().equals(nomeCancelamento)) {
                agendamentos.remove(a);
                System.out.println("Agendamento cancelado com sucesso.");
                removido = true;
                break;
            }
        }

        if (!removido) {
            System.out.println("Agendamento não encontrado");
        }
    }

    public void atualizarHorario(ArrayList<Agendamento> agendamentos, String nomeCliente, String novoHorario){
        boolean atualizado = false;

        System.out.println("\n=== ATUALIZANDO AGENDAMENTO ===");

        for (Agendamento a : agendamentos) {
            if (a.getCliente().getNome().equals(nomeCliente)) {
                a.setHorario(novoHorario);
                System.out.println("Horário alterado com sucesso.");
                System.out.println(a);
                atualizado = true;
                break;
            }
        }

        if (!atualizado) {
            System.out.println("Agendamento não encontrado");
        }

    }

    public boolean verificarHorarioDisponivel(
            ArrayList<Agendamento> agendamentos,
            Barbeiro barbeiro, String data, String horario
    ){
        for (Agendamento a : agendamentos) {
            if (a.getBarbeiro().getNome().equals(barbeiro.getNome())
                && a.getData().equals(data)
                && a.getHorario().equals(horario))
                return false;
        }
        return true;
    }

    public void adicionarAgendamento(
            ArrayList<Agendamento> agendamentos,
            Agendamento novoAgendamento
    ){
        if(
                verificarHorarioDisponivel(
                        agendamentos,novoAgendamento.getBarbeiro(),
                        novoAgendamento.getData(),
                        novoAgendamento.getHorario()
                )
        ){
            agendamentos.add(novoAgendamento);

            System.out.println("Agendamento realizado com sucesso.");
        } else{
            System.out.println("Horário indisponível.");
        }
    }

    public Cliente buscarClientePorNome(ArrayList<Cliente> clientes, String nomeCliente){
        for (Cliente c : clientes){
            if (c.getNome().equals(nomeCliente)){
                return c;
            }
        }
        return null;
    }

    public Barbeiro buscarBarbeiroPorNome(ArrayList<Barbeiro> barbeiros, String nomeBarbeiro){
        for (Barbeiro b : barbeiros){
            if (b.getNome().equals(nomeBarbeiro)){
                return b;
            }
        }
        return null;
    }

    public Servico buscarServicoPorNome(ArrayList<Servico> servicos, String nomeServico){
        for (Servico s : servicos){
            if (s.getNome().equals(nomeServico)){
                return s;
            }
        }
        return null;
    }

    public Cliente loginCliente(
            ArrayList<Cliente> clientes,
            String email,
            String senha
    ){
        for (Cliente c : clientes) {
            if (c.getEmail().equals(email)
                    &&
                    c.getSenha().equals(senha)
            ){
                return c;
            }
        }
        return null;
    }

    public void listarAgendamentosCliente(
            ArrayList<Agendamento> agendamentos,
            Cliente cliente
    ){
        boolean encontrado = false;
        System.out.println("\n=== Meus agendamentos ===");

        for (Agendamento a : agendamentos) {
            if (a.getCliente().getEmail().equals(cliente.getEmail())
            ) {
                System.out.println(a);
                encontrado = true;
            }
    }
        if (!encontrado) {
        System.out.println("Nenhum agendamento encontrado");
        }
    }

    public Barbeiro loginBarbeiro(
            ArrayList<Barbeiro> barbeiros,
            String email,
            String senha
    ){
        for (Barbeiro b : barbeiros){
            if (b.getEmail().equals(email)
            &&
                    b.getSenha().equals(senha)
            ){
                return b;
            }
        }

        return null;
    }

    public void listarAgendamentosBarbeiro(
            ArrayList<Agendamento> agendamentos,
            Barbeiro barbeiro
    ){
        boolean encontrado = false;

        System.out.println("\n=== Minha agenda ===");
        for (Agendamento a : agendamentos) {
            if (a.getBarbeiro().getEmail().equals(barbeiro.getEmail())
            ){
                System.out.println(a);
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("Nenhum agendamento encontrado");
        }
    }
}
