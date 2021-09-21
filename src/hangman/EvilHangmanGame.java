package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;


public class EvilHangmanGame implements IEvilHangmanGame{
    private Set<String> words;
    private HashMap<String, Set<String>> sets = new HashMap<>();
    private SortedSet<Character> guessedCharacters = new TreeSet<Character>();
    private String lastBestMapKey;

    //private int wLength;

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        //wLength = wordLength;
        Scanner scanner = new Scanner(dictionary);
        words = new HashSet<String>();
        while(scanner.hasNext()){
            String str = scanner.next();
            if(str.length() == wordLength){
                words.add(str);
            }
        }
        if(words.isEmpty()){
            throw new EmptyDictionaryException();
        }
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);
        if(guessedCharacters.contains(guess)){
            throw new GuessAlreadyMadeException();
        }
        guessedCharacters.add(guess);
        for(String s: words){
            s = s.toLowerCase();
            StringBuilder key = new StringBuilder("");
            for(int i = 0; i < s.length(); i++){
                if(s.charAt(i) == guess){
                    key.append(guess);
                }
                else{
                    key.append('-');
                }
            }
            //Find if there's a set with that key. If so, add s to the set of that key.
            if(sets.containsKey(key.toString())){
                sets.get(key.toString()).add(s);
            }
            else{
                sets.put(key.toString(), new HashSet<String>());
                sets.get(key.toString()).add(s);
            }

        }
        //cycle through map and find which set is the biggest. Return the largest set and apply it to the screen.
        int largestSetSize = 0;
        Set<String> largestSet = null;
        String largestSetKey = "";
        for(Map.Entry<String, Set<String>> entry : sets.entrySet()){
            Set<String> set = entry.getValue();
            if(set.size() > largestSetSize) {
                largestSetSize = set.size();
                largestSet = set;
                largestSetKey = entry.getKey();
            }
            else if(set.size() == largestSetSize){
                largestSetKey = tiebreaker(entry.getKey(), largestSetKey);
                largestSet = sets.get(largestSetKey);
            }
        }

        //I need to set the largest set at the object level, also, I probably need to reset the map.
        words = largestSet;
        sets = new HashMap<String, Set<String>>();
        this.lastBestMapKey = largestSetKey;
        return largestSet;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedCharacters;
    }
    private String tiebreaker(String s1, String s2){
        //needs to be implemented
        //Check if either doesn't have the letter at all.
        int s1LetterCount = 0;
        int s2LetterCount = 0;
        for(int i = 0; i < s1.length(); i++){
            if(s1.charAt(i) != '-'){
                s1LetterCount++;
            }
            if(s2.charAt(i) != '-'){
                s2LetterCount++;
            }
        }
        if (s1LetterCount == 0){
            return s1;
        }
        else if(s2LetterCount == 0){
            return s2;
        }
        //Check which one has the fewest letters
        if(s1LetterCount != s2LetterCount){
            return s1LetterCount < s2LetterCount ? s1 : s2;
        }//else check which has the most right letters
        else{
            for(int i = s1.length() - 1; i > 0; i--){
                if(s1.charAt(i) != s2.charAt(i)){
                    if(s1.charAt(i) == '-'){
                        return s2;
                    }
                    else return s1;
                }
            }
        }
        return "error";
    }
    public String getLettersToAdd(){
        return lastBestMapKey;
    }
}
    