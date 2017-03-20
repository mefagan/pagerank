package pagerank;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Page {
   public double base;
   public int wordCount;
   private double score;
   private List<Page> outlinks;
   private double newScore;
   private String path;
   private URL url;
   public int index;
   public int weight = 0;
   
   //calculate base values
   Page(int wordCount, String path, URL url, int index) {
     this.wordCount = wordCount;
     outlinks = new ArrayList<Page>();
     this.path = path;
     this.url = url;
     this.index = index;
   }
   
   public URL getURL() {
     return url;
   }
   
   public String getPath() {
     return this.path;
   }
   
   public List<Page> getOutlinks() {
     return outlinks;
   }
   
   public boolean addOutlink(Page page) {
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
     base = base/sum;
     score = base;
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
       return Objects.equals(path, page.path);
   }
   
  
}
   



