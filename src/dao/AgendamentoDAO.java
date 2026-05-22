package dao;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
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
}