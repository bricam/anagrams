/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;



public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 4;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private int wordLength = DEFAULT_WORD_LENGTH;

    private ArrayList<String> wordList = new ArrayList<String>();
    //HashMap stores anagrams
    private HashMap<String, ArrayList<String>> lettersToWord = new HashMap<String, ArrayList<String>>();
    private HashSet<String> wordSet = new HashSet<String>();
    private HashMap<Integer, ArrayList<String>> sizeToWords = new HashMap<Integer, ArrayList<String>>();


    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            if(sizeToWords.containsKey(word.length())){ //Any words of this length?
                sizeToWords.get(word.length()).add(word); //Add word to list of same sized words
            }
            else{ //No words of this length
                ArrayList<String> sameSizeWords = new ArrayList<String>(); //New list
                sameSizeWords.add(word); //Add word to list
                sizeToWords.put(word.length(),sameSizeWords); //Add list to hash map
            }
            //sizeToWords.put(word.length(), word);
            String sortedWord = sortLetters(word);
            wordSet.add(word); //Adds word to our HashSet
            if (lettersToWord.containsKey(sortedWord)){
                lettersToWord.get(sortedWord).add(word);
            }
            else{ //Doesnt have word
                ArrayList<String> anaList = new ArrayList<String>(); //The ArrayList of Anagrams
                anaList.add(word);
                lettersToWord.put(sortedWord, anaList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        if(wordSet.contains(word)){ //Check if word is in our dictionary
            if(word.toLowerCase().contains(base.toLowerCase())){ //Check if word contains base
                return false; //Contains base word
            }
            else {
                return true; // Word doesn't contain base
            }
        }
        return false; //Word isn't in dictionary
    }

    public List<String> getAnagrams(String targetWord) {
        //ArrayList<String> result = new ArrayList<String>();
        String sortedTarget = sortLetters(targetWord);
        ArrayList<String> result = lettersToWord.get(sortedTarget);
        return result;

        /*
        int goalSize = sortedTarget.length();
        int i = 0;
        while(i < wordList.size()) {
            String newWord = sortLetters(wordList.get(i));
            if ((newWord.length() == goalSize) && sortedTarget.equals(newWord)) {
                result.add(wordList.get(i)); //String matches, add to list
            }
            ++i;
        }
        return result;
        */
    }

    private String sortLetters(String input){ //Re-orders string
        char[] charArray = input.toCharArray();
        Arrays.sort(charArray);
        String output = new String(charArray);
        return output;



    }

    public List<String> getAnagramsWithOneMoreLetter(String word) { //To be implemented
        ArrayList<String> result = new ArrayList<String>();
        char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();
        for(int x = 0;x < alphabet.length; ++x ){
            String addedWord = word;
            addedWord += alphabet[x]; //Append current letter
            addedWord = sortLetters(addedWord); //Reorganize it
            if(lettersToWord.containsKey(addedWord)){ //If anagrams exist
                ArrayList<String> anagrams = lettersToWord.get(addedWord);
                for(int y=0;y<anagrams.size();++y){ //Take all anagrams in list
                    result.add(anagrams.get(y)); //Add it to the final ArrayList
                }
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {

        int wordListRange = sizeToWords.get(wordLength).size(); //Max range of list of same sized words
        Random r = new Random();
        int rand = r.nextInt(wordListRange);
        while(rand < wordListRange){
            String orderedWord = sortLetters(sizeToWords.get(wordLength).get(rand));
            if(lettersToWord.get(orderedWord).size() >= MIN_NUM_ANAGRAMS){
                if(wordLength < MAX_WORD_LENGTH) {
                    ++wordLength;
                }
                return sizeToWords.get(wordLength).get(rand);
            }
            ++rand; //Next word
            if(rand > sizeToWords.get(wordLength).size()- 1){ //If we're out of bounds, start over
                rand = 0;
            }
        }






        return "badge";




    }
}
