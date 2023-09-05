package ifpr.pgua.eic.colecaomusicas.controllers;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;

import ifpr.pgua.eic.colecaomusicas.App;
import ifpr.pgua.eic.colecaomusicas.model.entities.Musica;
import ifpr.pgua.eic.colecaomusicas.model.repositories.RepositorioMusicas;
import ifpr.pgua.eic.colecaomusicas.model.repositories.RepositorioPlaylists;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;

public class CadastroPlaylist implements Initializable {

    @FXML
    private ListView<Musica> lstMusicas;

    @FXML
    private TextField tfNome;

    private RepositorioPlaylists repositorioPlaylists;

    private RepositorioMusicas repositorioMusicas;

    public CadastroPlaylist(RepositorioPlaylists repositorioPlaylists, RepositorioMusicas repositorioMusicas) {
        this.repositorioMusicas = repositorioMusicas;
        this.repositorioPlaylists = repositorioPlaylists;
    }

    @FXML
    void cadastrar(ActionEvent event) {
        String nome = tfNome.getText();
        List<Musica> musicas = lstMusicas.getSelectionModel().getSelectedItems();

        Resultado rs = repositorioPlaylists.cadastrarPlaylist(nome, musicas);

        Alert alert;
        String msg = rs.getMsg();
        if (rs.foiErro()) {
            alert = new Alert(AlertType.ERROR, msg);
        } else {
            alert = new Alert(AlertType.CONFIRMATION, msg);

        }

        alert.showAndWait();
    }

    @FXML
    void voltar(ActionEvent event) {
        App.popScreen();
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        lstMusicas.getItems().clear();

        lstMusicas.getSelectionModel()
                .setSelectionMode(SelectionMode.MULTIPLE);

        Resultado r = repositorioMusicas.listar();

        if (r.foiSucesso()) {
            List<Musica> lista = (List) r.comoSucesso().getObj();
            lstMusicas.getItems().addAll(lista);
        } else {
            Alert alert = new Alert(AlertType.ERROR, r.getMsg());
            alert.showAndWait();
        }
    }
}