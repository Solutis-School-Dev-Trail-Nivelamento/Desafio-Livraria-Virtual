package br.com.livraria;

import br.com.livraria.model.*;
import jakarta.persistence.EntityManager;
import util.EntityManagerUtil;

import java.util.InputMismatchException;
import java.util.Scanner;

public class LivrariaVirtual {
    private static final int MAX_IMPRESSOS = 10;
    private static final int MAX_ELETRONICOS = 20;
    private static final int MAX_VENDAS = 50;

    private EntityManager em = EntityManagerUtil.getEntityManager();

    private long numImpressos = em.createQuery("SELECT COUNT(i) FROM Impresso i", Long.class).getSingleResult();
    private long numEletronicos = em.createQuery("SELECT COUNT(e) FROM Eletronico e", Long.class).getSingleResult();
    private long numVendas = em.createQuery("SELECT COUNT(v) FROM Venda v", Long.class).getSingleResult();

    public void cadastrarLivro() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Tipo de livro a ser cadastrado:");
        System.out.println("1 - Impresso");
        System.out.println("2 - Eletrônico");
        System.out.println("3 - Ambos");
        int tipo = scanner.nextInt();
        scanner.nextLine();

        if (tipo == 1 || tipo == 3) {
            if (numImpressos < MAX_IMPRESSOS) {
                try {
                    System.out.println("Informe os dados do livro impresso:");
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autores: ");
                    String autores = scanner.nextLine();
                    System.out.print("Editora: ");
                    String editora = scanner.nextLine();
                    System.out.print("Preço: ");
                    float preco = scanner.nextFloat();
                    System.out.print("Frete: ");
                    float frete = scanner.nextFloat();
                    System.out.print("Estoque: ");
                    int estoque = scanner.nextInt();
                    scanner.nextLine();

                    Impresso impresso = new Impresso(titulo, autores, editora, preco, frete, estoque);
                    em.getTransaction().begin();
                    em.persist(impresso);
                    em.getTransaction().commit();
                    System.out.println("Livro impresso cadastrado com sucesso!");
                    numImpressos++;
                } catch (InputMismatchException e) {
                    System.out.println("Por favor, insira um valor válido.");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Não é possível cadastrar mais livros impressos.");
            }
        }

        if (tipo == 2 || tipo == 3) {
            if (numEletronicos < MAX_ELETRONICOS) {
                try {
                    System.out.println("Informe os dados do livro eletrônico:");
                    System.out.print("Título: ");
                    String titulo = scanner.nextLine();
                    System.out.print("Autores: ");
                    String autores = scanner.nextLine();
                    System.out.print("Editora: ");
                    String editora = scanner.nextLine();
                    System.out.print("Preço: ");
                    float preco = scanner.nextFloat();
                    System.out.print("Tamanho(kB): ");
                    int tamanho = scanner.nextInt();
                    scanner.nextLine();

                    Eletronico eletronico = new Eletronico(titulo, autores, editora, preco, tamanho);
                    em.getTransaction().begin();
                    em.persist(eletronico);
                    em.getTransaction().commit();
                    System.out.println("Livro eletrônico cadastrado com sucesso!");
                    numEletronicos++;
                } catch (InputMismatchException e) {
                    System.out.println("Por favor, insira um valor válido.");
                    scanner.nextLine();
                }
            } else {
                System.out.println("Não é possível cadastrar mais livros eletrônicos.");
            }
        }
    }
}