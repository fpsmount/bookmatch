package br.com.alura.bookmatch.SERVICE;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
