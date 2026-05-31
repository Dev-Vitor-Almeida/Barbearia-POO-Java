package dao;
import model.Barbeiro;
import util.ConexaoDB;
import util.SenhaUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class BarbeiroDAO {

    public void salvarBarbeiro(Barbeiro barbeiro){
        String sql = "INSERT INTO barbeiro (nome, telefone, email, senha, especialidade) VALUES (?, ?, ?, ?, ?)";

        try{
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1, barbeiro.getNome());
            statement.setString(2, barbeiro.getTelefone());
            statement.setString(3, barbeiro.getEmail());
            statement.setString(4, SenhaUtil.gerarHash(barbeiro.getSenha()));
            statement.setString(5, barbeiro.getEspecialidade());

            statement.executeUpdate();

            System.out.println("Barbeiro salvo com sucesso!");
            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar barbeiro: ");
            e.printStackTrace();
        }
    }

    public ArrayList<Barbeiro> listarBarbeiros() {
        ArrayList<Barbeiro> barbeiros = new ArrayList<>();
        String sql = "SELECT * FROM barbeiro";

        try {
            Connection conexao = ConexaoDB.conectar();
            Statement statement = conexao.createStatement();
            ResultSet resultado = statement.executeQuery(sql);

            while (resultado.next()) {
                Barbeiro barbeiro = new Barbeiro(
                        resultado.getString("nome"),
                        resultado.getString("telefone"),
                        resultado.getString("email"),
                        resultado.getString("especialidade"),
                        resultado.getString("senha")
                );
                barbeiro.setId(resultado.getInt("id_barbeiro"));
                barbeiros.add(barbeiro);
            }

            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao listar barbeiros.");
            e.printStackTrace();
        }

        return barbeiros;
    }

    public Barbeiro buscarBarbeiroPorNome (String nome) {
        String sql = "SELECT * FROM barbeiro WHERE nome = ?";

        try{
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1, nome);
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()){
                Barbeiro barbeiro = new Barbeiro(
                        resultado.getString("nome"),
                        resultado.getString("telefone"),
                        resultado.getString("email"),
                        resultado.getString("especialidade"),
                        resultado.getString("senha")
                );

                barbeiro.setId(
                        resultado.getInt("id_barbeiro")
                );

                conexao.close();
                return barbeiro;
            }

            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao fazer login.");
            e.printStackTrace();
        }
        return null;
    }

    public Barbeiro buscarBarbeiroPorId(int id) {
        String sql = "SELECT * FROM barbeiro WHERE id_barbeiro = ?";

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultado = statement.executeQuery();

            if (resultado.next()) {
                Barbeiro barbeiro = new Barbeiro(
                        resultado.getString("nome"),
                        resultado.getString("telefone"),
                        resultado.getString("email"),
                        resultado.getString("especialidade"),
                        resultado.getString("senha")
                );
                barbeiro.setId(resultado.getInt("id_barbeiro"));
                conexao.close();
                return barbeiro;
            }

            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao buscar barbeiro por ID.");
            e.printStackTrace();
        }

        return null;
    }

    public Barbeiro loginBarbeiro (String email, String senha){
        String sql = "SELECT * FROM barbeiro WHERE email = ?";

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement =
                    conexao.prepareStatement(sql);

            statement.setString(1,email);
            ResultSet resultado =
                    statement.executeQuery();

            if (resultado.next() && SenhaUtil.conferir(senha, resultado.getString("senha"))) {
                Barbeiro barbeiro = new Barbeiro(
                        resultado.getString("nome"),
                        resultado.getString("telefone"),
                        resultado.getString("email"),
                        resultado.getString("especialidade"),
                        resultado.getString("senha")
                );

                barbeiro.setId(resultado.getInt("id_barbeiro"));

                conexao.close();

                return barbeiro;
            }

            conexao.close();

        } catch (Exception e) {
            System.out.println("Erro ao fazer login do barbeiro.");
            e.printStackTrace();
        }

        return null;
    }
}
