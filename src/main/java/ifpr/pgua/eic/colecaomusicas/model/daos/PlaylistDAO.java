package ifpr.pgua.eic.colecaomusicas.model.daos;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.colecaomusicas.model.entities.Playlist;

public interface PlaylistDAO {
    public Resultado criar(Playlist playlist);
    public Resultado listar();
    public Resultado getById(int id);
    public Resultado atualizar(int id, Playlist playlist);
    public Resultado deletar(int id);
}
