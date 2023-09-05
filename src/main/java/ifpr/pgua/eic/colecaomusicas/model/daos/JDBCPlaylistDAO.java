package ifpr.pgua.eic.colecaomusicas.model.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.colecaomusicas.model.entities.Musica;
import ifpr.pgua.eic.colecaomusicas.model.entities.Playlist;

public class JDBCPlaylistDAO implements PlaylistDAO {

    private static final String INSERT_PLAYLISTS_SQL = "INSERT INTO playlists(nome) VALUES (?)";
    private static final String INSERT_PLAYLISTS_MUSICAS_SQL = "INSERT INTO playlists_musicas(musicaId, playlistId) VALUES (?, ?)";
    private static final String SELECT_PLAYLIST = "SELECT * FROM playlists";
    private static final String SELECT_BYID = "SELECT * FROM playlists WHERE id=?";
    private static final String SELECT_MUSICAS_PLAYLIST = "SELECT DISTINCT musicas.id, musicas.nome, musicas.duracao, musicas.anoLancamento FROM musicas, playlists_musicas, playlists WHERE playlists_musicas.playlistId=? AND playlists_musicas.musicaId=musicas.id;";

    private FabricaConexoes fabricaConexoes;

    public JDBCPlaylistDAO(FabricaConexoes fabricaConexoes) {
        this.fabricaConexoes = fabricaConexoes;
    }

    @Override
    public Resultado criar(Playlist playlist) {
        try (Connection con = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(INSERT_PLAYLISTS_SQL, Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, playlist.getNome());

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                ResultSet rs = pstm.getGeneratedKeys();
                rs.next();
                int id = rs.getInt(1);

                playlist.setId(id);
                ret = 0;
            }

            pstm = con.prepareStatement(INSERT_PLAYLISTS_MUSICAS_SQL, Statement.RETURN_GENERATED_KEYS);

            for (Musica musica : playlist.getMusicas()) {
                pstm.setInt(1, musica.getId());
                pstm.setInt(2, playlist.getId());

                pstm.executeUpdate();

                ret++;
            }

            if (ret == playlist.getMusicas().size()) {
                return Resultado.sucesso("Playlist cadastrada", playlist);
            }

            return Resultado.erro("Erro desconhecido!");

        } catch (Exception e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado listar() {
        try (Connection con = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_PLAYLIST);

            ResultSet rs1 = pstm.executeQuery();

            ArrayList<Playlist> lista = new ArrayList<>();

            while (rs1.next()) {
                int id = rs1.getInt("id");
                String nome = rs1.getString("nome");

                lista.add(new Playlist(id, nome, null));
            }

            return Resultado.sucesso("Playlist listadas", lista);

        } catch (Exception e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado getById(int id) {
        try (Connection con = fabricaConexoes.getConnection()) {

            PreparedStatement pstm = con.prepareStatement(SELECT_MUSICAS_PLAYLIST);

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            ArrayList<Musica> listaMusica = new ArrayList<>();

            while (rs.next()) {
                int musicaId = rs.getInt("id");
                String musicaNome = rs.getString("nome");
                int duracao = rs.getInt("duracao");
                int anoLancamento = rs.getInt("anoLancamento");

                Musica musica = new Musica(musicaId, musicaNome, anoLancamento, duracao, null, null);

                listaMusica.add(musica);
            }

            pstm = con.prepareStatement(SELECT_BYID);

            pstm.setInt(1, id);

            rs = pstm.executeQuery();
            rs.next();

            String nomePlaylist = rs.getString("nome");

            Playlist playlist = new Playlist(id, nomePlaylist, listaMusica);

            return Resultado.sucesso("Playlist listadas", playlist);

        } catch (Exception e) {
            return Resultado.erro(e.getMessage());
        }
    }

    @Override
    public Resultado atualizar(int id, Playlist playlist) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'atualizar'");
    }

    @Override
    public Resultado deletar(int id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deletar'");
    }


}
