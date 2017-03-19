package pagerank;

import java.util.ArrayList;
import java.util.List;

public class Page {
   private double base;
   private int wordCount;
   private double score;
   private List<Page> outlinks;
   private double newScore;
   private String path;
   
   //calculate base values
   Page(int wordCount, String path) {
     this.wordCount = wordCount;
     base = calculateBase();
     outlinks = new ArrayList<Page>();
     this.path = path;
   }
   
   public String getPath() {
     return this.path;
   }
   
   public List<Page> getOutlinks() {
     return outlinks;
   }
   
   public double calculateBase() {
     return Math.log10(wordCount) / Math.log10(2.);
   }
   
   public double getBase() {
     return base;
   }
   
   public int getWordCount() {
     return wordCount;
   }
   
   public boolean calculateScore(double sum) {
     score = base/sum;
     return true;
   }
   
   public double getScore() {
     return score;
   }
   
   public void setScore(double score) {
     this.score = score;
   }
   
   public double getNewScore() {
     return newScore;
   }
   
   public void setNewScore(double newScore) {
     this.newScore = newScore;
   }
}
