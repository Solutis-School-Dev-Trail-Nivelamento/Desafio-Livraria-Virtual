package br.com.livraria.model;

import jakarta.persistence.Entity;

@Entity
public class Impresso extends Livro {
    private float frete;
    private int estoque;


    public Impresso() {

    }

    public Impresso(String titulo, String autores, String editora, float preco, float frete, int estoque) {
        super(titulo, autores, editora, preco);
        this.frete = frete;
        this.estoque = estoque;
    }

    public float getFrete() {
        return frete;
    }

    public void setFrete(float frete) {
        this.frete = frete;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public void atualizarEstoque(int quantidade) {
        if (estoque >= quantidade) {
            estoque -= quantidade;
        } else {
            System.out.println("Estoque insuficiente.");
        }
    }

    @Override
    public String toString() {
        return String.format(
                "%s, Frete: %.2f, Estoque: %d unidades",
                super.toString(), frete, estoque
        );
    }
}