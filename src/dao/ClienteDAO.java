package dao;

import model.Cliente;
import util.ConexaoDB;
import util.SenhaUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class ClienteDAO {

    public void salvarCliente(Cliente cliente) {

        String sql = """
                INSERT INTO cliente
                (nome, telefone, cpf, email, senha)
                VALUES (?, ?, ?, ?, ?)
                """;

        try {

            Connection conexao = ConexaoDB.conectar();

            PreparedStatement statement =
                    conexao.prepareStatement(sql);

            statement.setString(1, cliente.getNome());
            statement.setString(2, cliente.getTelefone());
            statement.setString(3, cliente.getCpf());
            statement.setString(4, cliente.getEmail());
            statement.setString(5, SenhaUtil.gerarHash(cliente.getSenha()));

            statement.executeUpdate();

            System.out.println("Cliente salvo com sucesso!");

            conexao.close();

        } catch (Exception e) {

            System.out.println("Erro ao salvar cliente!");

            e.printStackTrace();
        }
    }

    public ArrayList<Cliente> listarClientes() {

        ArrayList<Cliente> clientes = new ArrayList<>();

        String sql = "SELECT * FROM cliente";

        try {

            Connection conexao = ConexaoDB.conectar();

            Statement statement =
                    conexao.createStatement();

            ResultSet resultado =
                    statement.executeQuery(sql);

            while (resultado.next()) {

                Cliente cliente = new Cliente(
                        resultado.getString("nome"),
                        resultado.getString("telefone"),
                        resultado.getString("cpf"),
                        resultado.getString("email"),
                        resultado.getString("senha")
                );

                cliente.setId(resultado.getInt("id_cliente"));
                clientes.add(cliente);
            }

            conexao.close();

        } catch (Exception e) {

            System.out.println("Erro ao listar clientes.");

            e.printStackTrace();
        }

        return clientes;
    }

    public Cliente loginCliente(String email, String senha) {

        String sql = "SELECT * FROM cliente WHERE email = ?";

        try {
            Connection conexao = ConexaoDB.conectar();

            PreparedStatement statement =
                    conexao.prepareStatement(sql);

            statement.setString(1, email);
            ResultSet resultado =
                    statement.executeQuery();

            if (resultado.next() && SenhaUtil.conferir(senha, resultado.getString("senha"))) {
                Cliente cliente = new Cliente(
                        resultado.getString("nome"),
                        resultado.getString("telefone"),
                        resultado.getString("cpf"),
                        resultado.getString("email"),
                        resultado.getString("senha")
                );

                cliente.setId(
                        resultado.getInt("id_cliente")
                );


                conexao.close();

                return cliente;
            }

            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao fazer login.");
            e.printStackTrace();
        }

        return null;
    }

    public Cliente buscarClientePorNome(String nome) {
        String sql = "SELECT * FROM cliente WHERE nome = ?";
        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement =
                    conexao.prepareStatement(sql);
            statement.setString(1, nome);
            ResultSet resultado =
                    statement.executeQuery();
            if (resultado.next()){
                Cliente cliente = new Cliente(
                        resultado.getString("nome"),
                        resultado.getString("telefone"),
                        resultado.getString("cpf"),
                        resultado.getString("email"),
                        resultado.getString("senha")
                );

                cliente.setId(
                        resultado.getInt("id_cliente")
                );

                conexao.close();
                return cliente;
            }
            conexao.close();

        }catch (Exception e){
            System.out.println("Erro ao buscar cliente.");
            e.printStackTrace();
        }
        return null;
    }

    public Cliente buscarClientePorId(int id) {
        String sql = "SELECT * FROM cliente WHERE id_cliente = ?";

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1, id);

            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                Cliente cliente = new Cliente(
                        resultado.getString("nome"),
                        resultado.getString("telefone"),
                        resultado.getString("cpf"),
                        resultado.getString("email"),
                        resultado.getString("senha")
                );
                cliente.setId(resultado.getInt("id_cliente"));
                conexao.close();
                return cliente;
            }

            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao buscar cliente por ID.");
            e.printStackTrace();
        }

        return null;
    }


}



