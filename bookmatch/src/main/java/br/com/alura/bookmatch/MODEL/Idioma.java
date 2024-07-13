package br.com.alura.bookmatch.MODEL;

public enum Idioma {
    PT("pt"),
    EN("en"),
    FR("fr"),
    ES("es");

    private String linguagem;

    Idioma (String linguagem){
        this.linguagem = linguagem;
    }

    public static Idioma fromString(String text){
        for(Idioma lingua : Idioma.values()){
            if(lingua.linguagem.equalsIgnoreCase(text)){
                return lingua;
            }
        }
        throw new IllegalArgumentException("Nenhuma linguagem encontrada para a string fornecida: ");
    }
}
