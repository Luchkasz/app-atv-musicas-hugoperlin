package ifpr.pgua.eic.colecaomusicas.model.entities;

import java.util.List;

public class Playlist {

    private int id;
    private String nome;
    private List<Musica> musicas;

    public Playlist(int id, String nome, List<Musica> musicas) {
        this.id = id;
        this.nome = nome;
        this.musicas = musicas;
    }

    public Playlist(String nome, List<Musica> musicas) {
        this.nome = nome;
        this.musicas = musicas;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Musica> getMusicas() {
        return musicas;
    }

    public void setMusicas(List<Musica> musicas) {
        this.musicas = musicas;
    }

    @Override
    public String toString() {
        return "" + nome + "";
    }

}
