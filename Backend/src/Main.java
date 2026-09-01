import dao.BarbeiroDAO;
import dao.AgendamentoDAO;
import dao.ServicoDAO;
import dao.ClienteDAO;
import model.Cliente;
import model.Barbeiro;
import model.Servico;
import model.Agendamento;
import java.util.ArrayList;
import service.AgendamentoService;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) {
        Cliente cliente1 = new Cliente(
                "Vitor",
                "(11) 95881-0400",
                "473.414.308-00",
                "almeidavitor028@gmail.com",
                "123"
        );

        Cliente cliente2 = new Cliente(
                "Davi",
                "(11) 96589-3345",
                "777.777.777-02",
                "divizinho028@gmail.com",
                "pipoca"

        );

        Cliente cliente3 = new Cliente(
                "Erick",
                "(11) 96589-9989",
                "769.757.896-02",
                "zinho028@gmail.com",
                "coca"
        );

        ClienteDAO clienteDAO = new ClienteDAO();

        //clienteDAO.salvarCliente(cliente1);

        ArrayList<Cliente> clientesDoBanco =
                clienteDAO.listarClientes();

        System.out.println("\n=== CLIENTES DO BANCO ===");

                for (Cliente c : clientesDoBanco) {
                    System.out.println(c);
        }

        BarbeiroDAO barbeiroDAO = new BarbeiroDAO();;

        Barbeiro barbeiro1 = new Barbeiro(
                "David",
                "4482-8922",
                "david@gmail.com",
                "corte",
                "banana"
        );

        Barbeiro barbeiro2 = new Barbeiro(
                "James",
                "5508-2233",
                "james@barbeiro.com",
                "corte, barba",
                "corte"
        );

        Barbeiro barbeiro3 = new Barbeiro(
                "Jappa",
                "6699-8899",
                "jappabarbeiro@.com",
                "Corte, barba e sombracelha",
                "brasa"
        );

        //barbeiroDAO.salvarBarbeiro(barbeiro1);
        //barbeiroDAO.salvarBarbeiro(barbeiro2);
        //barbeiroDAO.salvarBarbeiro(barbeiro3);

        ServicoDAO servicoDAO = new ServicoDAO();

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

        //servicoDAO.salvarServico(servico1);
        //servicoDAO.salvarServico(servico2);
        //servicoDAO.salvarServico(servico3);
        //servicoDAO.salvarServico(servico4);
        //servicoDAO.salvarServico(servico5);

        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();

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

        AgendamentoService serviceAgendamento = new AgendamentoService();

        Scanner scanner = new Scanner(System.in);
        int opcao;
        do {
            System.out.println("""
        
====================================
      SISTEMA DA BARBEARIA
====================================
1 - Listar agendamentos
2 - Buscar agendamento
3 - Cancelar agendamento
4 - Atualizar horário
5 - Novo agendamento
6 - Login cliente
7 - Login barbeiro
8 - Cadastrar cliente
0 - Sair
====================================
Digite uma opção:""");

            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    agendamentoDAO.listarAgendamentos();
                    break;
                case 2:
                    System.out.print("Digite o nome do Cliente: ");
                    String nomeBusca = scanner.nextLine();
                    agendamentoDAO.buscarAgendamentoPorCliente(nomeBusca);
                    break;
                    case 3:
                        System.out.print("Digite o nome do cliente para cancelar: ");
                        String nomeCancelamento = scanner.nextLine();
                        agendamentoDAO.cancelarAgendamentoPorCliente(nomeCancelamento);
                        break;
                case 4:
                    System.out.print("Digite o nome do   Cliente: ");
                    String nomeCliente = scanner.nextLine();

                    System.out.print("Digite o novo horário: ");
                    String novoHorario = scanner.nextLine();
                    agendamentoDAO.atualizarHorarioAgendamento(
                            nomeCliente,
                            novoHorario
                    );

                    break;
                case 5:
                    System.out.print("Nome do cliente:");
                    String nomeNovoCliente = scanner.nextLine();

                    System.out.print("Nome do Barbeiro: ");
                    String nomeBarbeiro = scanner.nextLine();

                    System.out.print("Nome do serviço: ");
                    String nomeServico = scanner.nextLine();

                    System.out.print("Data do agendamento: ");
                    String data = scanner.nextLine();

                    System.out.print("Horário do agendamento: ");
                    String horario = scanner.nextLine();

                    if (data.isEmpty() || horario.isEmpty()) {
                        System.out.println("Data e horário são obrigatórios.");
                        break;
                    }

                    Cliente clienteEncontrado =
                            clienteDAO.buscarClientePorNome(nomeNovoCliente);

                    Barbeiro barbeiroEncontrado =
                            barbeiroDAO.buscarBarbeiroPorNome(
                                    nomeBarbeiro
                            );

                    Servico servicoEncontrado =
                            servicoDAO.buscarServicoPorNome(nomeServico);

                    if (clienteEncontrado != null && barbeiroEncontrado != null && servicoEncontrado != null) {
                        Agendamento novoAgendamento = new Agendamento(
                                clienteEncontrado,
                                barbeiroEncontrado,
                                servicoEncontrado,
                                data,
                                horario
                        );

                        serviceAgendamento.adicionarAgendamento(
                                agendamentos,
                                novoAgendamento
                        );


                        agendamentoDAO.salvarAgendamento(novoAgendamento);

                    } else {
                        if (clienteEncontrado == null) {
                            System.out.println("Cliente não encontrado.");
                        }

                        if (barbeiroEncontrado == null) {
                            System.out.println("Barbeiro não encontrado.");
                        }

                        if (servicoEncontrado == null) {
                            System.out.println("Serviço não encontrado.");
                        }
                    }

                    break;

                case 6:
                    System.out.print("Digite seu email: ");
                    String emailLogin = scanner.nextLine();

                    System.out.println("Digite sua senha: ");
                    String senhaLogin = scanner.nextLine();

                    if (emailLogin.isEmpty() || senhaLogin.isEmpty()) {
                        System.out.println("Digite email e senha.");
                        break;
                    }

                    Cliente clienteLogado =
                            clienteDAO.loginCliente(emailLogin, senhaLogin);

                    if (clienteLogado != null) {

                        System.out.println("\n=== Login realizado com sucesso ===");
                        System.out.println("Bem vindo, " + clienteLogado.getNome());

                        serviceAgendamento.listarAgendamentosCliente(
                                agendamentos,clienteLogado
                        );
                    } else {
                        System.out.println("email ou senha inválidos");
                    }

                    break;

                case 7:
                    System.out.print("Digite seu email: ");
                    String emailBarbeiro = scanner.nextLine();

                    System.out.println("Digite sua senha: ");
                    String senhaBarbeiro = scanner.nextLine();

                    Barbeiro barbeiroLogado =
                            barbeiroDAO.loginBarbeiro(
                                    emailBarbeiro,
                                    senhaBarbeiro
                            );

                    if (barbeiroLogado != null) {
                        System.out.println("\n Login realizado com sucesso");
                        System.out.println("Bem vindo, " + barbeiroLogado.getNome());

                        serviceAgendamento.listarAgendamentosBarbeiro(
                                agendamentos,barbeiroLogado
                        );
                    } else {
                        System.out.println("Email ou senha inválidos");
                    }

                    break;

                case 8:

                    System.out.print("Nome: ");
                    String nome = scanner.nextLine();

                    System.out.print("CPF: ");
                    String cpf = scanner.nextLine();

                    System.out.print("Telefone: ");
                    String telefone = scanner.nextLine();

                    System.out.print("Email: ");
                    String email =  scanner.nextLine();

                    System.out.print("Senha: ");
                    String senha = scanner.nextLine();

                    if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
                        System.out.println("Preencha todos os campos obrigatórios.");
                        break;
                    }

                    Cliente novoCliente = new Cliente(
                            nome,
                            cpf,
                            telefone,
                            email,
                            senha
                    );

                    clienteDAO.salvarCliente(novoCliente);

                    break;


                case 0:
                    System.out.println("Encerrando Sistema...");

                    break;

                    default:

                        System.out.println("Opção inválida.");
            }
        } while (opcao != 0);

        scanner.close();
    }
}
