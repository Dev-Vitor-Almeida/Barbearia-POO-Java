package dao;

import model.Servico;
import util.ConexaoDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ServicoDAO {

    public void salvarServico(Servico servico) {

        String sql = "INSERT INTO servico (servico, preco, duracao_minutos) VALUES (?, ?, ?)";

        try {

            Connection conexao = ConexaoDB.conectar();

            PreparedStatement statement =
                    conexao.prepareStatement(sql);

            statement.setString(1, servico.getNome());
            statement.setDouble(2, servico.getPreco());
            statement.setInt(3, servico.getDuracaoMinutos());

            statement.executeUpdate();

            System.out.println("Serviço salvo com sucesso!");

            conexao.close();

        } catch (Exception e) {

            System.out.println("Erro ao salvar serviço.");

            e.printStackTrace();
        }
    }

    public ArrayList<Servico> listarServicos() {
        ArrayList<Servico> servicos = new ArrayList<>();
        String sql = "SELECT * FROM servico";

        try {
            Connection conexao = ConexaoDB.conectar();
            Statement statement = conexao.createStatement();
            ResultSet resultado = statement.executeQuery(sql);

            while (resultado.next()) {
                Servico servico = new Servico(
                        resultado.getString("servico"),
                        resultado.getDouble("preco"),
                        resultado.getInt("duracao_minutos")
                );
                servico.setId(resultado.getInt("id_servico"));
                servicos.add(servico);
            }

            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao listar servicos.");
            e.printStackTrace();
        }

        return servicos;
    }

    public Servico buscarServicoPorNome(String nome) {

        String sql = "SELECT * FROM servico WHERE servico = ?";

        try {

            Connection conexao = ConexaoDB.conectar();

            PreparedStatement statement =
                    conexao.prepareStatement(sql);

            statement.setString(1, nome);

            ResultSet resultado =
                    statement.executeQuery();

            if (resultado.next()) {

                Servico servico = new Servico(
                        resultado.getString("servico"),
                        resultado.getDouble("preco"),
                        resultado.getInt("duracao_minutos")
                );

                servico.setId(
                        resultado.getInt("id_servico")
                );

                resultado.getInt("id_servico");

                conexao.close();

                return servico;
            }

            conexao.close();

        } catch (Exception e) {

            System.out.println("Erro ao buscar serviço.");

            e.printStackTrace();
        }

        return null;
    }

    public Servico buscarServicoPorId(int id) {
        String sql = "SELECT * FROM servico WHERE id_servico = ?";

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                Servico servico = new Servico(
                        resultado.getString("servico"),
                        resultado.getDouble("preco"),
                        resultado.getInt("duracao_minutos")
                );
                servico.setId(resultado.getInt("id_servico"));
                conexao.close();
                return servico;
            }

            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao buscar servico por ID.");
            e.printStackTrace();
        }

        return null;
    }
}
