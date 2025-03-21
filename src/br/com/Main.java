package br.com;

import br.com.model.Board;
import br.com.model.Space;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Stream;

import static br.com.util.BoardTemplate.BOARD_TEMPLATE;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;

public class Main {
    // Declara um objeto Scanner estático e final para ler a entrada do usuário
    private final static Scanner scanner = new Scanner(System.in);

    // Declara um objeto Board estático que será usado para representar o tabuleiro do jogo
    private static Board board;

    // Define uma constante estática e final para o limite do tabuleiro, que é 9
    private final static int BOARD_LIMIT = 9;

    // 0,0;4,false 1,0;7,false 2,0;9,true 3,0;5,false 4,0;8,true 5,0;6,true 6,0;2,true 7,0;3,false 8,0;1,false 0,1;1,false 1,1;3,true 2,1;5,false 3,1;4,false 4,1;7,true 5,1;2,false 6,1;8,false 7,1;9,true 8,1;6,true 0,2;2,false 1,2;6,true 2,2;8,false 3,2;9,false 4,2;1,true 5,2;3,false 6,2;7,false 7,2;4,false 8,2;5,true 0,3;5,true 1,3;1,false 2,3;3,true 3,3;7,false 4,3;6,false 5,3;4,false 6,3;9,false 7,3;8,true 8,3;2,false 0,4;8,false 1,4;9,true 2,4;7,false 3,4;1,true 4,4;2,true 5,4;5,true 6,4;3,false 7,4;6,true 8,4;4,false 0,5;6,false 1,5;4,true 2,5;2,false 3,5;3,false 4,5;9,false 5,5;8,false 6,5;1,true 7,5;5,false 8,5;7,true 0,6;7,true 1,6;5,false 2,6;4,false 3,6;2,false 4,6;3,true 5,6;9,false 6,6;6,false 7,6;1,true 8,6;8,false 0,7;9,true 1,7;8,true 2,7;1,false 3,7;6,false 4,7;4,true 5,7;7,false 6,7;5,false 7,7;2,true 8,7;3,false 0,8;3,false 1,8;2,false 2,8;6,true 3,8;8,true 4,8;5,true 5,8;1,false 6,8;4,true 7,8;7,false 8,8;9,false

    public static void main(String[] args) {
        // Converte os argumentos passados na linha de comando em um mapa de posições
        // Stream.of: Cria uma stream a partir de um array
        // collect: Coleta os elementos da stream em um mapa
        // toMap: Cria um mapa a partir dos elementos da stream
        final var positions = Stream.of(args)
                .collect(toMap(
                        k -> k.split(";")[0], // Chave: coordenada (ex: "0,0")
                        v -> v.split(";")[1]  // Valor: configuração (ex: "1,true")
                ));
        var option = -1;
        while (true){
            // Exibe o menu de opções para o usuário
            System.out.println("Selecione uma das opções a seguir");
            System.out.println("1 - Iniciar um novo Jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            // Lê a opção escolhida pelo usuário
            option = scanner.nextInt();

            // Executa a ação correspondente à opção escolhida
            switch (option){
                case 1 -> startGame(positions); // Inicia um novo jogo
                case 2 -> inputNumber();        // Insere um novo número no tabuleiro
                case 3 -> removeNumber();       // Remove um número do tabuleiro
                case 4 -> showCurrentGame();    // Exibe o estado atual do tabuleiro
                case 5 -> showGameStatus();     // Exibe o status atual do jogo
                case 6 -> clearGame();          // Limpa o tabuleiro
                case 7 -> finishGame();         // Finaliza o jogo
                case 8 -> System.exit(0);       // Sai do programa
                default -> System.out.println("Opção inválida, selecione uma das opções do menu"); // Opção inválida
            }
        }
    }

    private static void startGame(final Map<String, String> positions) {
        // Verifica se o jogo já foi iniciado
        if (nonNull(board)){
            System.out.println("O jogo já foi iniciado");
            return;
        }

        // Cria uma lista de listas para representar as posições do tabuleiro
        List<List<Space>> spaces = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            spaces.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                // Obtém a configuração da posição a partir do mapa de posições
                var positionConfig = positions.get("%s,%s".formatted(i, j));
                // Divide a configuração em valor esperado e se é fixo
                var expected = Integer.parseInt(positionConfig.split(",")[0]);
                var fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                // Cria um novo espaço com o valor esperado e se é fixo
                var currentSpace = new Space(expected, fixed);
                // Adiciona o espaço à lista de espaços
                spaces.get(i).add(currentSpace);
            }
        }

        // Inicializa o tabuleiro com a lista de espaços
        board = new Board(spaces);
        System.out.println("O jogo está pronto para começar");
    }

// Método para inserir um número no tabuleiro
private static void inputNumber() {
    // Verifica se o jogo já foi iniciado
    if (isNull(board)){
        System.out.println("O jogo ainda não foi iniciado iniciado");
        return;
    }

    // Solicita ao usuário a coluna onde o número será inserido
    System.out.println("Informe a coluna que em que o número será inserido");
    var col = runUntilGetValidNumber(0, 8);
    // Solicita ao usuário a linha onde o número será inserido
    System.out.println("Informe a linha que em que o número será inserido");
    var row = runUntilGetValidNumber(0, 8);
    // Solicita ao usuário o número que será inserido na posição especificada
    System.out.printf("Informe o número que vai entrar na posição [%s,%s]\n", col, row);
    var value = runUntilGetValidNumber(1, 9);
    // Tenta alterar o valor na posição especificada, se não for possível, informa que a posição é fixa
    if (!board.changeValue(col, row, value)){
        System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
    }
}

// Método para remover um número do tabuleiro
private static void removeNumber() {
    // Verifica se o jogo já foi iniciado
    if (isNull(board)){
        System.out.println("O jogo ainda não foi iniciado iniciado");
        return;
    }

    // Solicita ao usuário a coluna onde o número será removido
    System.out.println("Informe a coluna que em que o número será inserido");
    var col = runUntilGetValidNumber(0, 8);
    // Solicita ao usuário a linha onde o número será removido
    System.out.println("Informe a linha que em que o número será inserido");
    var row = runUntilGetValidNumber(0, 8);
    // Tenta remover o valor na posição especificada, se não for possível, informa que a posição é fixa
    if (!board.clearValue(col, row)){
        System.out.printf("A posição [%s,%s] tem um valor fixo\n", col, row);
    }
}

// Método para exibir o estado atual do tabuleiro
private static void showCurrentGame() {
    // Verifica se o jogo já foi iniciado
    if (isNull(board)){
        System.out.println("O jogo ainda não foi iniciado iniciado");
        return;
    }

    // Cria um array de argumentos para formatar a exibição do tabuleiro
    var args = new Object[81];
    var argPos = 0;
    // Preenche o array de argumentos com os valores atuais do tabuleiro
    for (int i = 0; i < BOARD_LIMIT; i++) {
        for (var col: board.getSpaces()){
            args[argPos ++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
        }
    }
    // Exibe o tabuleiro formatado
    System.out.println("Seu jogo se encontra da seguinte forma");
    System.out.printf((BOARD_TEMPLATE) + "\n", args);
}

// Método para exibir o status atual do jogo
private static void showGameStatus() {
    // Verifica se o jogo já foi iniciado
    if (isNull(board)){
        System.out.println("O jogo ainda não foi iniciado iniciado");
        return;
    }

    // Exibe o status atual do jogo
    System.out.printf("O jogo atualmente se encontra no status %s\n", board.getStatus().getLabel());
    // Verifica se o jogo contém erros e informa o usuário
    if(board.hasErrors()){
        System.out.println("O jogo contém erros");
    } else {
        System.out.println("O jogo não contém erros");
    }
}

// Método para limpar o tabuleiro
private static void clearGame() {
    // Verifica se o jogo já foi iniciado
    if (isNull(board)){
        System.out.println("O jogo ainda não foi iniciado iniciado");
        return;
    }

    // Solicita confirmação do usuário para limpar o tabuleiro
    System.out.println("Tem certeza que deseja limpar seu jogo e perder todo seu progresso?");
    var confirm = scanner.next();
    while (!confirm.equalsIgnoreCase("sim") && !confirm.equalsIgnoreCase("não")){
        System.out.println("Informe 'sim' ou 'não'");
        confirm = scanner.next();
    }

    // Se o usuário confirmar, limpa o tabuleiro
    if(confirm.equalsIgnoreCase("sim")){
        board.reset();
    }
}

// Método para finalizar o jogo
private static void finishGame() {
    // Verifica se o jogo já foi iniciado
    if (isNull(board)){
        System.out.println("O jogo ainda não foi iniciado iniciado");
        return;
    }

    // Verifica se o jogo foi concluído com sucesso
    if (board.gameIsFinished()){
        System.out.println("Parabéns você concluiu o jogo");
        showCurrentGame();
        board = null;
    } else if (board.hasErrors()) {
        // Informa se o jogo contém erros
        System.out.println("Seu jogo conté, erros, verifique seu board e ajuste-o");
    } else {
        // Informa se ainda há espaços a serem preenchidos
        System.out.println("Você ainda precisa preenhcer algum espaço");
    }
}

// Método auxiliar para obter um número válido do usuário dentro de um intervalo
private static int runUntilGetValidNumber(final int min, final int max){
    var current = scanner.nextInt();
    while (current < min || current > max){
        System.out.printf("Informe um número entre %s e %s\n", min, max);
        current = scanner.nextInt();
    }
    return current;
}

}