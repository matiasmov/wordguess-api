import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

enum LetterStatus {
    CORRECT_POSITION, // Verde
    WRONG_POSITION,   // Amarelo
    NOT_IN_WORD       // Cinza
}

public class TerminalGame {

    // --- CÓDIGOS DE COR ANSI PARA O TERMINAL  ---
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_WHITE_TEXT = "\u001B[97m";
    public static final String ANSI_GREEN_BACKGROUND = "\u001B[42m";
    public static final String ANSI_YELLOW_BACKGROUND = "\u001B[43m";
    public static final String ANSI_GRAY_BACKGROUND = "\u001B[100m";

    public static void main(String[] args) {
        System.out.println("--- BEM VINDO AO MEU JOGO DE TERMO ---");

        List<String> wordList = loadWordsFromFile("palavras.txt");

        if (wordList.isEmpty()) {
            System.out.println("Não foi possível carregar as palavras.");
            return;
        }

        Random random = new Random();
        String secretWord = wordList.get(random.nextInt(wordList.size()));
        int maxAttempts = 6;
        Scanner scanner = new Scanner(System.in);

        for (int attempt = 1; attempt <= maxAttempts; attempt++) {
            System.out.print("Tentativa " + attempt + " de " + maxAttempts + ": Digite uma palavra de 5 letras: ");
            String guess = scanner.nextLine().toUpperCase();

            if (guess.length() != 5) {
                System.out.println("Palavra inválida! digite uma palavra com 5 letras.");
                attempt--;
                continue;
            }

            LetterStatus[] result = processGuess(secretWord, guess);

            System.out.print("Resultado: ");
            for (int i = 0; i < result.length; i++) {
                char letter = guess.charAt(i);
                switch (result[i]) {
                    case CORRECT_POSITION:
                        System.out.print(ANSI_GREEN_BACKGROUND + ANSI_WHITE_TEXT + " " + letter + " " + ANSI_RESET);
                        break;
                    case WRONG_POSITION:
                        System.out.print(ANSI_YELLOW_BACKGROUND + ANSI_WHITE_TEXT + " " + letter + " " + ANSI_RESET);
                        break;
                    case NOT_IN_WORD:
                        System.out.print(ANSI_GRAY_BACKGROUND + ANSI_WHITE_TEXT + " " + letter + " " + ANSI_RESET);
                        break;
                }
                System.out.print(" ");
            }
            System.out.println();


            if (guess.equals(secretWord)) {
                System.out.println("\nPARABÉNS! Você acertou a palavra!");
                break;
            }

            if (attempt == maxAttempts) {
                System.out.println("\nJogo encerrado! A palavra era: " + secretWord);
            }
        }

        scanner.close();
    }

    public static List<String> loadWordsFromFile(String fileName) {
        List<String> words = new ArrayList<>();
        try {
            File file = new File(fileName);
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNextLine()) {
                words.add(fileScanner.nextLine().toUpperCase());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Erro: Arquivo '" + fileName + "' não encontrado! Certifique-se que ele está na mesma pasta do seu .java");
        }
        return words;
    }

    public static LetterStatus[] processGuess(String secretWord, String guessWord) {
        int wordLength = secretWord.length();
        LetterStatus[] result = new LetterStatus[wordLength];
        char[] secretChars = secretWord.toCharArray();
        char[] guessChars = guessWord.toCharArray();

        for (int i = 0; i < wordLength; i++) {
            if (guessChars[i] == secretChars[i]) {
                result[i] = LetterStatus.CORRECT_POSITION;
                secretChars[i] = '_';
                guessChars[i] = '*';
            }
        }

        for (int i = 0; i < wordLength; i++) {
            if (guessChars[i] == '*') continue;

            boolean foundInWrongPosition = false;
            for (int j = 0; j < wordLength; j++) {
                if (guessChars[i] == secretChars[j]) {
                    result[i] = LetterStatus.WRONG_POSITION;
                    secretChars[j] = '_';
                    foundInWrongPosition = true;
                    break;
                }
            }
            if (!foundInWrongPosition) {
                result[i] = LetterStatus.NOT_IN_WORD;
            }
        }
        return result;
    }
}