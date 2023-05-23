package controller;

import dao.TarefaDAO;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.model.SelectItem;
import jakarta.inject.Named;
import model.Tarefa;
import lombok.Getter;
import lombok.Setter;


import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Named
@SessionScoped
@Getter
@Setter
public class TarefaMBean implements Serializable {

    private List<Tarefa> tarefas;
    private Tarefa tarefa = new Tarefa();
    private String mensagemSucesso;
    private String mensagemErro;
    private Long numero;
    private String tituloDescricao;
    private String responsavel;
    private boolean status;
    private Date dataTermino;
    private boolean ativaMensagem;
    private boolean cadastroOuAlteracao;

    @PostConstruct
    public void init() {
        tarefa = new Tarefa();
        tarefas = new ArrayList<>();
        mensagemErro = "";
        mensagemSucesso = "";
        tituloDescricao = "";
        responsavel = "";
        numero = null;
        status = true;
        dataTermino = new Date();
        ativaMensagem = false;
        cadastroOuAlteracao = false;
    }

    public String cadastrar() {
        TarefaDAO trfDAO = new TarefaDAO();
        try {
            if (dadosValidados()) {
                trfDAO.salvar(tarefa);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        init();
        mensagemSucesso = "Operação realizada com sucesso!";
        mensagemErro = "";
        ativaMensagem = true;
        listar();
        return null;
    }

    public String excluir(Tarefa trf) {
        TarefaDAO trfDAO = new TarefaDAO();
        try {
            if (trf != null) {
                trfDAO.remover(trf);
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        init();
        mensagemSucesso = "Operação realizada com sucesso!";
        mensagemErro = "";
        ativaMensagem = true;
        listar();
        return null;
    }

    public String alterar() {
        TarefaDAO trfDAO = new TarefaDAO();
        try {
            if (dadosValidados()) {
                trfDAO.alterar(tarefa);
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        init();
        mensagemSucesso = "Operação realizada com sucesso!";
        mensagemErro = "";
        ativaMensagem = true;
        listar();
        return null;
    }

    public String listar() {
        TarefaDAO trfDAO = new TarefaDAO();
        try {
            tarefas = trfDAO.findAllByIdTituloDescricaoResponsavelStatus(numero, tituloDescricao, responsavel, status);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        if (tarefas.isEmpty()) {
            ativaMensagem = true;
            mensagemErro = "Busca sem resultados.";
            return null;
        }
        return null;
    }

    public String concluir(Tarefa trf) {
        TarefaDAO trfDAO = new TarefaDAO();
        trf.setStatus(false);
        try {
            trfDAO.alterar(trf);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
        init();
        mensagemSucesso = "Operação realizada com sucesso!";
        mensagemErro = "";
        ativaMensagem = true;
        listar();
        return null;
    }

    public String carregaTelaCadastro() {
        init();
        cadastroOuAlteracao = true;
        return null;
    }

    public String carregaTelaAlteracao(Tarefa trf) {
        init();
        tarefa = trf;
        cadastroOuAlteracao = true;
        return null;
    }

    public String fechaMensagem() {
        ativaMensagem = false;
        mensagemErro = "";
        mensagemSucesso = "";
        return null;
    }


    public List<SelectItem> getListaResponsaveis() {
        List<String> listaStrings = new ArrayList<>();
        listaStrings.add("Andre Melo");
        listaStrings.add("Elias Souza");
        listaStrings.add("Yuri Conrado");

        List<SelectItem> listaSelectItens = new ArrayList<>();

        for (String valor : listaStrings) {
            SelectItem selectItem = new SelectItem(valor, valor);
            listaSelectItens.add(selectItem);
        }
        return listaSelectItens;
    }

    public List<SelectItem> getListaPrioridades() {
        List<String> listaStrings = new ArrayList<>();
        listaStrings.add("Alta");
        listaStrings.add("Média");
        listaStrings.add("Baixa");

        List<SelectItem> listaSelectItens = new ArrayList<>();

        for (String valor : listaStrings) {
            SelectItem selectItem = new SelectItem(valor, valor);
            listaSelectItens.add(selectItem);
        }
        return listaSelectItens;
    }

    public LocalDate getDataHoje() {
        return LocalDate.now();
    }

    public String getRetornoMensagem() {
        return mensagemSucesso.isEmpty() && !mensagemErro.isEmpty() ? mensagemErro : mensagemSucesso;
    }

    public boolean dadosValidados() {
        TarefaDAO tfDao = new TarefaDAO();
        if (tarefa.getTitulo() == null || tarefa.getTitulo().isEmpty()) {
            mensagemErro = "Campo título é obrigatorio.";
            mensagemSucesso = "";
            ativaMensagem = true;
            return false;
        }
        if (tarefa.getDescricao() == null || tarefa.getDescricao().isEmpty()) {
            mensagemErro = "Campo descrição é obrigatorio.";
            mensagemSucesso = "";
            ativaMensagem = true;
            return false;
        }
        if (tarefa.getResponsavel() == null || tarefa.getResponsavel().isEmpty()) {
            mensagemErro = "Campo responsável é obrigatorio.";
            mensagemSucesso = "";
            ativaMensagem = true;
            return false;
        }
        if (tarefa.getPrioridade() == null || tarefa.getPrioridade().isEmpty()) {
            mensagemErro = "Campo prioridade é obrigatorio.";
            mensagemSucesso = "";
            ativaMensagem = true;
            return false;
        }
        mensagemErro = "";
        ativaMensagem = false;
        return true;
    }

    public void carregarInicio() {
        listar();
        init();
    }


}
