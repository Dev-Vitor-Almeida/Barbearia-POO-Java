package model;

public class Servico {
    private String nome;
    private double preco;
    private int duracaoMinutos;

    public Servico(String nome, double preco, int duracaoMinutos) {
        this.nome = nome;
        this.preco = preco;
        this.duracaoMinutos = duracaoMinutos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getDuracaoMinutos() {
        return duracaoMinutos;
    }

    public void setDuracaoMinutos(int duracaoMinutos) {
        this.duracaoMinutos = duracaoMinutos;
    }

    @Override
    public String toString() {
        return "Servico{" +
                "nome='" + nome + '\'' +
                ", preco=" + preco +
                ", duracaoMinutos=" + duracaoMinutos +
                '}';
    }
}
