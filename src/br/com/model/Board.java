package br.com.model;

import java.util.Collection;
import java.util.List;

import static br.com.model.GameStatusEnum.COMPLETE;
import static br.com.model.GameStatusEnum.INCOMPLETE;
import static br.com.model.GameStatusEnum.NON_STARTED;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Board {

    private final List<List<Space>> spaces; // Lista de listas de espaços no tabuleiro

    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    // Retorna a lista de listas de espaços
    public List<List<Space>> getSpaces() {
        return spaces;
    }

    // Retorna o status do jogo (não iniciado, incompleto ou completo)
    public GameStatusEnum getStatus(){
        // Objetivo: Se não houver espaços vazios e todos os espaços forem fixos, o jogo não foi iniciado
        // flatMap: Transforma uma lista de listas em uma lista
        // Collection::stream: Transforma uma lista em uma stream
        // noneMatch: Verifica se nenhum elemento da stream atende a condição
        if (spaces.stream().flatMap(Collection::stream).noneMatch(s -> !s.isFixed() && nonNull(s.getActual()))){
            return NON_STARTED;
        }

        // Objetivo: Se houver algum espaço vazio, o jogo está incompleto
        // anyMatch: Verifica se algum elemento da stream atende a condição
        return spaces.stream().flatMap(Collection::stream).anyMatch(s -> isNull(s.getActual())) ? INCOMPLETE : COMPLETE;
    }

    // Verifica se há erros no tabuleiro
    public boolean hasErrors(){
        if(getStatus() == NON_STARTED){
            return false;
        }
        // Objetivo: Se houver algum espaço com valor diferente do esperado, o jogo tem erros
        return spaces.stream().flatMap(Collection::stream)
                .anyMatch(s -> nonNull(s.getActual()) && !s.getActual().equals(s.getExpected()));
    }

    // Altera o valor de um espaço específico no tabuleiro
    public boolean changeValue(final int col, final int row, final int value){
        var space = spaces.get(col).get(row);
        if (space.isFixed()){
            return false;
        }

        space.setActual(value);
        return true;
    }

    // Limpa o valor de um espaço específico no tabuleiro
    public boolean clearValue(final int col, final int row){
        var space = spaces.get(col).get(row);
        if (space.isFixed()){
            return false;
        }

        space.clearSpace();
        return true;
    }

    // Reseta o tabuleiro, limpando todos os espaços
    public void reset(){
        spaces.forEach(c -> c.forEach(Space::clearSpace));
    }

    // Verifica se o jogo está finalizado (sem erros e completo)
    public boolean gameIsFinished(){
        return !hasErrors() && getStatus().equals(COMPLETE);
    }

}