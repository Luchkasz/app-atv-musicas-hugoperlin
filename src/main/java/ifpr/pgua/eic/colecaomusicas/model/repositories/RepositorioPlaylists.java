package ifpr.pgua.eic.colecaomusicas.model.repositories;

import java.util.ArrayList;
import java.util.List;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.colecaomusicas.model.daos.ArtistaDAO;
import ifpr.pgua.eic.colecaomusicas.model.daos.GeneroDAO;
import ifpr.pgua.eic.colecaomusicas.model.daos.PlaylistDAO;
import ifpr.pgua.eic.colecaomusicas.model.entities.Artista;
import ifpr.pgua.eic.colecaomusicas.model.entities.Genero;
import ifpr.pgua.eic.colecaomusicas.model.entities.Musica;
import ifpr.pgua.eic.colecaomusicas.model.entities.Playlist;

public class RepositorioPlaylists {

    private PlaylistDAO dao;
    private ArtistaDAO artistaDAO;
    private GeneroDAO generoDAO;

    public RepositorioPlaylists(PlaylistDAO dao, ArtistaDAO artistaDAO, GeneroDAO generoDAO) {
        this.dao = dao;
        this.artistaDAO = artistaDAO;
        this.generoDAO = generoDAO;
    }

    public Resultado cadastrarPlaylist(String nome, List<Musica> musicas) {
        if (nome.isEmpty() || nome.isBlank()) {
            return Resultado.erro("Nome inválido!");
        }

        if (musicas == null) {
            return Resultado.erro("Lista vazia!");
        }

        Playlist playlist = new Playlist(nome, musicas);
        return dao.criar(playlist);
    }

    public Resultado listarPlaylists() {

        Resultado resultado = dao.listar();

        return resultado;
    }

    public Resultado mostrarMusicasDeUmaPlaylist(Playlist playlist) {

        Resultado resultado = dao.getById(playlist.getId());

        if (resultado.foiSucesso()) {

            Playlist playlistAux = (Playlist) resultado.comoSucesso().getObj();
            
            List<Musica> musicas = new ArrayList<>();

            for (Musica musica : playlistAux.getMusicas()) {
                Resultado r1 = artistaDAO.buscarArtistaMusica(musica.getId());
                if (r1.foiErro()) {
                    return r1;
                }
                Artista artista = (Artista) r1.comoSucesso().getObj();
                musica.setArtista(artista);

                Resultado r2 = generoDAO.buscarGeneroMusica(musica.getId());
                if (r2.foiErro()) {
                    return r2;
                }
                Genero genero = (Genero) r2.comoSucesso().getObj();
                musica.setGenero(genero);

                musicas.add(musica);
            }

            return Resultado.sucesso("Musicas", musicas);
        }

        return resultado;
    }
}