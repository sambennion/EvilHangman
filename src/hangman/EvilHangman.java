package hangman;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {
        EvilHangmanGame game = new EvilHangmanGame();
        File f = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        int numGuesses = Integer.parseInt(args[2]);
        try{
            game.startGame(f, wordLength);
        }
         catch (EmptyDictionaryException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        StringBuilder curWord = new StringBuilder();
        for (int i = 0; i < wordLength; i++){
            curWord.append('-');
        }
        gameLoop(game, numGuesses, curWord);
    }
    private static void gameLoop(EvilHangmanGame game, int numGuesses, StringBuilder curWord) {
        System.out.println(String.format("You have %d guesses left", numGuesses));
        String s = game.getGuessedLetters().toString();
        System.out.println(String.format("Used letters %s", s));
        System.out.println(String.format("Word: %s", curWord.toString()));
        boolean validWord;
        do {
            validWord = true;
            System.out.println("Enter guess: ");
            Scanner scan = new Scanner(System.in);
            String input = scan.next();
            input = input.toLowerCase();
            if (input.length() > 1 || (input.charAt(0) < 'a' || input.charAt(0) > 'z')) {
                System.out.println("Invalid input!");
                validWord = false;
            } else {
                try {
                    game.makeGuess(input.charAt(0));
                    int numAddedLetters = 0;
                    for (int i = 0; i < game.getLettersToAdd().length(); i++) {
                        if (game.getLettersToAdd().charAt(i) != '-') {
                            curWord.setCharAt(i, game.getLettersToAdd().charAt(i));
                            numAddedLetters++;
                        }
                    }
                    if (numAddedLetters == 0) {
                        System.out.println(String.format("Sorry, there are no %s\n", input));
                        numGuesses--;
                    } else {
                        System.out.println(String.format("Yes, there is %d %s\n", numAddedLetters, input));
                    }

                } catch (GuessAlreadyMadeException e) {
                    //e.printStackTrace();
                    System.out.println("Guess already made!");
                }
            }
        } while (!validWord);
        gameLoop(game, numGuesses, curWord);
        //System.out.println(String.format("Your guess is %s", input));


    }
}
