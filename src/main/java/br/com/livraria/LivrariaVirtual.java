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

    public void realizarVenda() {
        if (numVendas >= MAX_VENDAS){
            System.out.println("Limite máximo de vendas atingido.");
            return;
        }

        try {
            Scanner scanner = new Scanner(System.in);

            System.out.println("Nome do cliente: ");
            String cliente = scanner.nextLine();

            System.out.println("Tipo de livro (1 - Impresso, 2 - Eletrônico): ");
            int tipoLivro = scanner.nextInt();
            listarLivros(tipoLivro);

            System.out.println("Escolha o ID do livro: ");
            int idLivro = scanner.nextInt();

            System.out.println("Quantidade de livros a comprar: ");
            int quantidadeLivros = scanner.nextInt();

            Livro livroEscolhido = em.find(Livro.class, idLivro);
            em.getTransaction().begin();

            if (livroEscolhido != null) {
                if (tipoLivro == 1) {
                    Impresso impresso = (Impresso) livroEscolhido;
                    Venda venda = new Venda();

                    if (impresso.getEstoque() >= quantidadeLivros) {

                        impresso.atualizarEstoque(quantidadeLivros);

                        registrarVenda(venda, impresso, cliente, quantidadeLivros);

                        System.out.println("Venda realizada com sucesso!");
                        numVendas++;
                    } else {
                        System.out.println("Estoque insuficiente para essa quantidade de livros.");
                    }
                } else if (tipoLivro == 2) {
                    Eletronico eletronico = (Eletronico) livroEscolhido;
                    Venda venda = new Venda();

                    registrarVenda(venda, eletronico, cliente, quantidadeLivros);

                    System.out.println("Venda realizada com sucesso!");
                    numVendas++;
                } else {
                    System.out.println("Tipo de livro inválido.");
                }
            } else {
                System.out.println("Livro não encontrado!");
            }
        } catch (Exception e) {
            em.getTransaction().rollback();
            e.printStackTrace();
        }
    }

    private void registrarVenda(Venda venda, Livro livro, String cliente, int quantidadeLivros) {
        venda.setCliente(cliente);
        venda.setValor(venda.getValor() + livro.getPreco() * quantidadeLivros);
        venda.getLivros().add(livro);

        em.persist(venda);
        em.getTransaction().commit();
    }
    
}
