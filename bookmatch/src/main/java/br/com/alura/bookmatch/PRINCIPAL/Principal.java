package br.com.alura.bookmatch.PRINCIPAL;

import br.com.alura.bookmatch.MODEL.Autor;
import br.com.alura.bookmatch.MODEL.DadosRequisição;
import br.com.alura.bookmatch.MODEL.Idioma;
import br.com.alura.bookmatch.MODEL.Livro;
import br.com.alura.bookmatch.REPOSITORY.AutorRepository;
import br.com.alura.bookmatch.SERVICE.ConsumoAPI;
import br.com.alura.bookmatch.SERVICE.ConverteDados;

import java.util.*;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Principal {

    private Scanner scanner = new Scanner(System.in);
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConverteDados converteDados = new ConverteDados();
    private final String ENDERECO = "https://gutendex.com/books/?search=";
    private AutorRepository repositorio;

    public Principal(AutorRepository repositorio){
        this.repositorio = repositorio;
    }

    private List<Autor> autores = new ArrayList<>();
    private Optional<Autor> autorOptinal;

    public void exibeMenu(){
        var opcao = -1;
        while(opcao != 0){
            var menu  = """
                    Escolha uma opção: 
                    1 - Buscar Livro pelo título
                    2 - Listar livros registrados
                    3 - Listar autores registrados
                    4 - Listar autores vivos em um determinado ano
                    5 - Listar livros em um determinado idioma
                    6 - Listar os 5 livros mais baixados
                    
                    0 - Sair
                    """;
            System.out.println(menu);
            opcao = scanner.nextInt();

            scanner.nextLine();

            switch(opcao){

                case 1:
                    buscarLivro();
                break;
                case 2:
                    listarLivro();
                break;
                case 3:
                    listarAutor();
                break;
                case 4:
                    listarVivos();
                break;
                case 5:
                    listarIdioma();
                break;
                case 6:
                    listarTop5();
                break;

                default:
                    System.out.println("Opção não encontrada: "+opcao);
                break;
            }
        }
    }

    private void buscarLivro() {
        List<Livro> livros = new ArrayList<>();
        System.out.println("Digite o nome do livro desejado: ");
        var nomeLivro = scanner.nextLine();
        var json = consumoAPI.obterDados(ENDERECO + nomeLivro.toLowerCase().replace(" ", "+"));
        var dados = converteDados.obterDados(json, DadosRequisição.class);

        try{
            if(!dados.results().isEmpty()) {
                var dadoLivroAutor = dados.results().get(0).autores().get(0);
                var dadosLivro = dados.results().get(0);

                Autor autor = new Autor(dadoLivroAutor.nome(), dadoLivroAutor.anoNascimento(), dadoLivroAutor.anoFalecimento());
                Livro novoLivro = new Livro(dadosLivro.titulo(), dadosLivro.idiomas(), dadosLivro.numeroDownloads(), autor);
                livros.add(novoLivro);

                autorOptinal = repositorio.findByNomeContainingIgnoreCase(autor.getNome().toLowerCase());

                if (!autorOptinal.isPresent()) {
                    System.out.println("Adicionando autor: " + autor.getNome());
                    autor.setLivros(livros);
                    repositorio.save(autor);
                } else {
                    System.out.println("O autor já está no repositório");
                    autorOptinal.get().setLivros(livros);
                    repositorio.save(autorOptinal.get());
                }
            } else System.out.println("Nenhum livro encontrado");
            } catch (IndexOutOfBoundsException e){
            System.out.println("Autor desconhecido");
        }
    }

    private void listarLivro(){
        List<Livro> livros = repositorio.buscarLivro();
        livros.stream()
                .forEach(a -> System.out.println(String.format("""
                        ------ Livro ----
                        Título: %s
                        Autor: %d
                        Idioma: %d
                        Número de downloads: %s
                        """, a.getTitulo(), a.getAutor(), a.getIdioma(), a.getNumeroDownloads())));
    }

    private void listarAutor(){
        autores = repositorio.buscarAutor();
        autores.stream()
                .forEach(a -> System.out.println(String.format("""
                        ------ Autor ------
                        Autor: %s
                        Ano de nascimento: %d
                        Ano de falescimento: %d
                        Livros: %s
                        """, a.getNome(), a.getAnoNascimento(), a.getAnoFalecimento(), repositorio.listaDeLivrosPorAutor(a.getNome()))));
    }

    private void listarVivos(){
        System.out.println("Insira um ano para listar os autores vivos: ");
        int ano = scanner.nextInt();
        scanner.nextLine();

        autores = repositorio.buscarAutoresVivos(ano);
        autores.stream()
                .filter(b -> b.getAnoNascimento() != 0 && b.getAnoFalecimento() != 0)
                .forEach(a -> System.out.println(String.format("""
                        Autor: %s
                        Ano Nascimento: %d
                        Ano Falecimento: %d
                        Livros: %s
                        """, a.getNome(), a.getAnoNascimento(), a.getAnoFalecimento(), repositorio.listaDeLivrosPorAutor(a.getNome()))));
    }

    private void listarIdioma(){
        System.out.println("""
                Insira um idioma para a busca:
                es - espanhol
                en - ingles
                fr - frances
                pt - portugues
                """);
        var idioma = scanner.nextLine();
        List<Livro> livrosPorIdioma = repositorio.listarLivroPorIdioma(Idioma.fromString(idioma));
        livrosPorIdioma.stream()
                .forEach(l -> System.out.println(String.format("""
                        ------ Livro ------
                        Título: %s
                        Autor: %s
                        Idioma: %s
                        Número de Downloads: %d
                        --------------------
                        """, l.getTitulo(), l.getAutor(), l.getIdioma(), l.getNumeroDownloads())));
    }

    private void listarTop5(){
        List<Livro> top5Livros = repositorio.listarTop5Livros();
        top5Livros.stream()
                .forEach(l -> System.out.println(String.format("""
                        ------ Livro ------
                        Título: %s
                        Autor: %s
                        Idioma: %s
                        Número de downloads: %d
                        --------------------
                        """, l.getTitulo(), l.getAutor(), l.getIdioma(), l.getNumeroDownloads())));
    }

}
