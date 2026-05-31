package dao;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.Agendamento;
import util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class AgendamentoDAO {

    public void salvarAgendamento(Agendamento agendamento) {

        String sql = """
                INSERT INTO agendamento
                (id_cliente, id_barbeiro, id_servico, data_agendamento, horario)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {

            Connection conexao = ConexaoDB.conectar();

            PreparedStatement statement =
                    conexao.prepareStatement(sql);

            statement.setInt(
                    1,
                    agendamento.getCliente().getId()
            );

            statement.setInt(
                    2,
                    agendamento.getBarbeiro().getId()
            );

            statement.setInt(
                    3,
                    agendamento.getServico().getId()
            );

            statement.setString(
                    4,
                    agendamento.getData()
            );

            statement.setString(
                    5,
                    agendamento.getHorario()
            );

            statement.executeUpdate();

            System.out.println("Agendamento salvo com sucesso!");

            conexao.close();

        } catch (Exception e) {

            System.out.println("Erro ao salvar agendamento.");

            e.printStackTrace();
        }
    }

    public ArrayList<Agendamento> listarAgendamentos() {
        ArrayList<Agendamento> agendamentos = new ArrayList<>();

        String sql = """
                SELECT * FROM agendamento""";

        try {
            Connection conexao = ConexaoDB.conectar();

            Statement statement = conexao.createStatement();

            ResultSet resultado =
                    statement.executeQuery(sql);

            while(resultado.next()) {
                System.out.println("""
===========================
AGENDAMENTO
===========================
Cliente ID: %d
Barbeiro ID: %d
Serviço ID: %d
Data: %s
Horário: %s
===========================
""".formatted(
                        resultado.getInt("id_cliente"),
                        resultado.getInt("id_barbeiro"),
                        resultado.getInt("id_servico"),
                        resultado.getString("data_agendamento"),
                        resultado.getString("horario")
                ));
            }

            conexao.close();
        }catch (Exception e) {
            System.out.println("Erro ao listar agendamentos.");
            e.printStackTrace();
        }

        return agendamentos;
    }

    public boolean horarioDisponivel(int idBarbeiro, String data, String horario) {
        String sql = """
                SELECT COUNT(*) AS total
                FROM agendamento
                WHERE id_barbeiro = ? AND data_agendamento = ? AND horario = ?
                """;

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1, idBarbeiro);
            statement.setString(2, data);
            statement.setString(3, horario);

            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                boolean disponivel = resultado.getInt("total") == 0;
                conexao.close();
                return disponivel;
            }

            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao verificar horario disponivel.");
            e.printStackTrace();
        }

        return false;
    }

    public ArrayList<Map<String, String>> listarAgendamentosDetalhados() {
        ArrayList<Map<String, String>> agendamentos = new ArrayList<>();

        String sql = """
                SELECT
                a.id_agendamento,
                c.id_cliente,
                c.nome AS nome_cliente,
                b.id_barbeiro,
                b.nome AS nome_barbeiro,
                s.id_servico,
                s.servico AS nome_servico,
                s.preco,
                s.duracao_minutos,
                a.data_agendamento,
                a.horario
                FROM agendamento a
                INNER JOIN cliente c ON a.id_cliente = c.id_cliente
                INNER JOIN barbeiro b ON a.id_barbeiro = b.id_barbeiro
                INNER JOIN servico s ON a.id_servico = s.id_servico
                ORDER BY a.data_agendamento, a.horario
                """;

        try {
            Connection conexao = ConexaoDB.conectar();
            Statement statement = conexao.createStatement();
            ResultSet resultado = statement.executeQuery(sql);

            while (resultado.next()) {
                Map<String, String> agendamento = new HashMap<>();
                agendamento.put("id", String.valueOf(resultado.getInt("id_agendamento")));
                agendamento.put("clienteId", String.valueOf(resultado.getInt("id_cliente")));
                agendamento.put("clienteNome", resultado.getString("nome_cliente"));
                agendamento.put("barbeiroId", String.valueOf(resultado.getInt("id_barbeiro")));
                agendamento.put("barbeiroNome", resultado.getString("nome_barbeiro"));
                agendamento.put("servicoId", String.valueOf(resultado.getInt("id_servico")));
                agendamento.put("servicoNome", resultado.getString("nome_servico"));
                agendamento.put("preco", String.valueOf(resultado.getDouble("preco")));
                agendamento.put("duracaoMinutos", String.valueOf(resultado.getInt("duracao_minutos")));
                agendamento.put("data", resultado.getString("data_agendamento"));
                agendamento.put("horario", resultado.getString("horario"));
                agendamentos.add(agendamento);
            }

            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao listar agendamentos detalhados.");
            e.printStackTrace();
        }

        return agendamentos;
    }

    public void buscarAgendamentoPorCliente(String nomeCliente) {
        String sql = """
                SELECT
                a.id_agendamento, 
                c.nome AS nome_cliente,
                b.nome AS nome_barbeiro,
                s.servico AS nome_servico,
                a.data_agendamento,
                a.horario
                FROM agendamento a
                INNER JOIN cliente c ON a.id_cliente = c.id_cliente
                INNER JOIN barbeiro b ON a.id_barbeiro = b.id_barbeiro
                INNER JOIN servico s ON a.id_servico = s.id_servico
                WHERE c.nome = ?
                """;

        try {
            Connection conexao = ConexaoDB.conectar();

            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1,nomeCliente);

            ResultSet resultado = statement.executeQuery();

            boolean encontrado = false;

            while (resultado.next()) {
                System.out.println("""
                        =========================
                        AGENDAMENTO ENCONTRADO
                        =========================
                        Cliente: %s
                        Barbeiro: %s
                        Serviço: %s
                        Data: %s
                        Horário: %s
                        =========================
                        """.formatted(
                                resultado.getString("nome_cliente"),
                                resultado.getString("nome_barbeiro"),
                                resultado.getString("nome_servico"),
                                resultado.getString("data_agendamento"),
                                resultado.getString("horario")
                                ));

                encontrado = true;
            }

            if (!encontrado) {
                System.out.println("Nenhum agendamento encontrado para o cliente: " + nomeCliente);
            }

            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao buscar agendamento.");
            e.printStackTrace();
        }
    }

    public void cancelarAgendamentoPorCliente(String nomeCliente) {
        String sql = """
                DELETE a FROM agendamento a
                INNER JOIN cliente c on a.id_cliente = c.id_cliente
                WHERE c.nome = ?""";

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement =
                    conexao.prepareStatement(sql);
            statement.setString(1,nomeCliente);
            int linhasAfetadas = statement.executeUpdate();

            if (linhasAfetadas > 0) {
                System.out.println("Agendamento cancelado com sucesso!");
            } else {
                System.out.println("Agendamento não encontrado!");
            }

            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao cancelar agendamento.");
            e.printStackTrace();
        }
    }

    public boolean cancelarAgendamentoPorId(int idAgendamento) {
        String sql = "DELETE FROM agendamento WHERE id_agendamento = ?";

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1, idAgendamento);

            int linhasAfetadas = statement.executeUpdate();
            conexao.close();

            return linhasAfetadas > 0;
        } catch (Exception e) {
            System.out.println("Erro ao cancelar agendamento.");
            e.printStackTrace();
        }

        return false;
    }

    public void atualizarHorarioAgendamento(
            String nomeCliente,
            String novoHorario
    ) {
        String sql = """
                UPDATE agendamento a
                INNER JOIN cliente c
                on a.id_cliente = c.id_cliente
                SET a.horario = ?
                WHERE c.nome = ?""";

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement =
                    conexao.prepareStatement(sql);

            statement.setString(1, novoHorario);
            statement.setString(2, nomeCliente);

            int linhasAfetadas = statement.executeUpdate();
            if (linhasAfetadas > 0) {
                System.out.println("Horário atualizado com sucesso!");

            } else {
                System.out.println("Agendamento não encontrado.");
            }

            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao atualizar horário.");
            e.printStackTrace();
        }
    }

    public boolean atualizarHorarioAgendamentoPorId(
            int idAgendamento,
            String novoHorario
    ) {
        String sql = "UPDATE agendamento SET horario = ? WHERE id_agendamento = ?";

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement = conexao.prepareStatement(sql);

            statement.setString(1, novoHorario);
            statement.setInt(2, idAgendamento);

            int linhasAfetadas = statement.executeUpdate();
            conexao.close();

            return linhasAfetadas > 0;
        } catch (Exception e) {
            System.out.println("Erro ao atualizar horario.");
            e.printStackTrace();
        }

        return false;
    }
}
