package pagerank;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Page {
   public double base;
   public int wordCount;
   private double score;
   private List<String> outlinks;
   private double newScore;
   private String path;
   private URL url;
   
   //calculate base values
   Page(int wordCount, String path, URL url) {
     this.wordCount = wordCount;
     outlinks = new ArrayList<String>();
     this.path = path;
     this.url = url;
   }
   
   public URL getURL() {
     return url;
   }
   
   public String getPath() {
     return this.path;
   }
   
   public List<String> getOutlinks() {
     return outlinks;
   }
   
   public boolean addOutlink(String page) {
     return outlinks.add(page);
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
     System.out.println(path);
     System.out.println("base =" + base);
     System.out.println("score = " + score);
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
   
   @Override
   public boolean equals(Object o) {
       if (this == o)
           return true;
       if (o == null)
           return false;
       if (getClass() != o.getClass())
           return false;
       Page page = (Page) o;
       // field comparison
       return Objects.equals(path, page.path)
               && Objects.equals(wordCount, page.wordCount) 
               && Objects.equals(url, page.url);
   }

}
