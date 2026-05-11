package servico;
import model.Agendamento;
import java.util.ArrayList;

public class AgendamentoServico {
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
            System.out.println("Nenhum agendamento encontrado para o cliene: " + nomeBusca);
        }
    }
}
