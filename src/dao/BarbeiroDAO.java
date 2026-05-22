package dao;
import model.Barbeiro;
import util.ConexaoDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class BarbeiroDAO {

    public void salvarBarbeiro(Barbeiro barbeiro){
        String sql = "INSERT INTO barbeiro (nome, telefone, email, senha, especialidade) VALUES (?, ?, ?, ?, ?)";

        try{
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement = conexao.prepareStatement(sql);
            statement.setString(1, barbeiro.getNome());
            statement.setString(2, barbeiro.getTelefone());
            statement.setString(3, barbeiro.getEmail());
            statement.setString(4, barbeiro.getSenha());
            statement.setString(5, barbeiro.getEspecialidade());

            statement.executeUpdate();

            System.out.println("Barbeiro salvo com sucesso!");
            conexao.close();
        } catch (Exception e) {
            System.out.println("Erro ao salvar barbeiro: ");
            e.printStackTrace();
        }
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
                        resultado.getString("senha"),
                        resultado.getString("especialidade")
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

    public Barbeiro loginBarbeiro (String email, String senha){
        String sql = "SELECT * FROM barbeiro WHERE email = ? and senha = ?";

        try {
            Connection conexao = ConexaoDB.conectar();
            PreparedStatement statement =
                    conexao.prepareStatement(sql);

            statement.setString(1,email);
            statement.setString(2, senha);

            ResultSet resultado =
                    statement.executeQuery();

            if (resultado.next()) {
                Barbeiro barbeiro = new Barbeiro(
                        resultado.getString("nome"),
                        resultado.getString("telefone"),
                        resultado.getString("email"),
                        resultado.getString("senha"),
                resultado.getString("especialidade")
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
