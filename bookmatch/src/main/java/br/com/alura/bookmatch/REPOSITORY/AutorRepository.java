package br.com.alura.bookmatch.REPOSITORY;

import br.com.alura.bookmatch.MODEL.Autor;
import br.com.alura.bookmatch.MODEL.Idioma;
import br.com.alura.bookmatch.MODEL.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    Optional<Autor> findByNomeContainingIgnoreCase(String nome);
    @Query("SELECT l.titulo FROM Livro l JOIN l.autor a WHERE a.nome LIKE %:nome%")
    List<String> listaDeLivrosPorAutor(String nome);

    @Query("SELECT l FROM Livro l")
    List<Livro> buscarLivro();

    @Query("SELECT a FROM Autor a")
    List<Autor> buscarAutor();

    @Query("SELECT a FROM Autor a WHERE a.anoNascimento <= :ano AND a.anoFalecimento >= :ano")
    List<Autor> buscarAutoresVivos(int ano);

    @Query("SELECT l FROM Livro l WHERE l.idioma = :idioma")
    List<Livro> listarLivroPorIdioma(Idioma idioma);

    @Query("SELECT l FROM Livro l ORDER BY l.numeroDownloads DESC LIMIT 5")
    List<Livro> listarTop5Livros();
}
