import model.Cliente;
import model.Barbeiro;
import model.Servico;
import model.Agendamento;
import java.util.ArrayList;
public class Main {
    public static void main(String[] args) {
        Cliente cliente1 = new Cliente(
                "Vitor",
                "(11) 95881-0400",
                "473.414.308-00",
                "almeidavitor028@gmail.com"
        );

        Cliente cliente2 = new Cliente(
                "Davi",
                "(11) 96589-3345",
                "777.777.777-02",
                "divizinho028@gmail.com"
        );

        Cliente cliente3 = new Cliente(
                "Erick",
                "(11) 96589-9989",
                "769.757.896-02",
                "zinho028@gmail.com"
        );

        Barbeiro barbeiro1 = new Barbeiro(
                "David",
                "4482-8922",
                "david@gmail.com",
                "corte"
        );

        Barbeiro barbeiro2 = new Barbeiro(
                "James",
                "5508-2233",
                "james@barbeiro.com",
                "corte, barba"
        );

        Barbeiro barbeiro3 = new Barbeiro(
                "Jappa",
                "6699-8899",
                "jappabarbeiro@.com",
                "Corte, barba e sombracelha"
        );

        Servico servico1 = new Servico(
                "corte",
                35,
                40
        );

        Servico servico2 = new Servico(
                "barba",
                10,
                11
        );

        Servico servico3 = new Servico(
                "sobrancelha",
                5,
                5
        );

        Servico servico4 = new Servico(
                "corte e barba",
                45,
                50
        );

        Servico servico5 = new Servico(
                "corte, barba e sombrancelha",
                50,
                60
        );

        Agendamento agendamento = new Agendamento(
                cliente1,
                barbeiro1,
                servico1,
                "12/05/2026",
                "11:00"
        );

        Agendamento agendamento2 = new Agendamento(
                cliente2,
                barbeiro3,
                servico3,
                "12/05/2026",
                "15:30"
        );

        Agendamento agendamento3 = new Agendamento(
                cliente3,
                barbeiro2,
                servico2,
                "16/05/2026",
                "16:00"
        );

        System.out.println(agendamento);

        cliente1.mostrarInformacoes(agendamento);
        barbeiro1.mostrarInformacoes(agendamento);

        cliente2.mostrarInformacoes(agendamento2);
        barbeiro2.mostrarInformacoes(agendamento2);

        cliente3.mostrarInformacoes(agendamento3);
        barbeiro3.mostrarInformacoes(agendamento3);



        ArrayList<Cliente> clientes = new ArrayList<>();
        ArrayList<Barbeiro> barbeiros = new ArrayList<>();
        ArrayList<Servico> servicos = new ArrayList<>();
        ArrayList<Agendamento> agendamentos = new ArrayList<>();

        clientes.add(cliente1);
        clientes.add(cliente2);
        clientes.add(cliente3);

        barbeiros.add(barbeiro1);
        barbeiros.add(barbeiro2);
        barbeiros.add(barbeiro3);

        servicos.add(servico1);
        servicos.add(servico2);
        servicos.add(servico3);
        servicos.add(servico4);
        servicos.add(servico5);

        agendamentos.add(agendamento);
        agendamentos.add(agendamento2);
        agendamentos.add(agendamento3);

        System.out.println("\n=== CLIENTES CADASTRADOS ===");
        for (Cliente c : clientes) {
            System.out.println(c);
        }

        System.out.println("\n=== BARBEIROS CADASTRADOS ===");
        for (Barbeiro b : barbeiros) {
            System.out.println(b);
        }

        System.out.println("\n=== SERVICOS CADASTRADOS ===");
        for (Servico s : servicos) {
            System.out.println(s);
        }

        System.out.println("\n=== AGENDAMENTO CADASTRADOS ===");
        for (Agendamento a : agendamentos) {
            System.out.println(a);
        }
    }
}

