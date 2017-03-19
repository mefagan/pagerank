package pagerank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PageRankAlgorithm {
  private double sum;
  private double F;
  private int N;
  private List<Page> pages = new ArrayList<Page>();
  double[][] weights;
  public double epsilon;
  
  PageRankAlgorithm(String path, double F, ArrayList<Page> links) {
    //read in files from directory
    for (Page page: links) {
      pages.add(page);
    }
    this.F = F;
    this.N = pages.size();
    weights = new double[N][N];
    epsilon = 0.01/N;
    calculateSum();
    calculateScore();
  }
  
  public boolean addPage(Page page) {
    pages.add(page);
    return true;
  }
  
  public List<Page> getPages(){
    return pages;
  }
  
  //calculate sum
  public boolean calculateSum() {
    for (Page p: pages) {
      sum =+ p.getBase();
    }
    return true;
  }
  
  public double getSum() {
    return sum;
  }
  
  public void calculateScore() {
    for (Page p: pages) {
      p.calculateScore(this.getSum());
    }
  }
  
  public void calculateEmptyLinks(int P) {
    for (int Q = 0; Q < pages.size(); Q++) {
      weights[Q][P] = pages.get(Q).getScore();
    }
  }
  
  double calculateWeightSum(int P) {
    double weightSum = 0;
    for (int Q = 0; Q < pages.size(); Q++) {
      weightSum =+ weights[Q][P];
    }
    return weightSum;
  }
  
  public void normalizeWeights(int P, double weightSum, Page page) {
    for (int Q = 0; Q < page.getOutlinks().size(); Q++) {
      weights[Q][P] = weights[Q][P]/weightSum;
    }
  }
  
  public double calculateWeight(int P, int Q) {
    return 1;
  }
  
  public void calculateNonemptyWeights(int P) {
    for (int Q = 0; Q < pages.size(); Q++) {
      weights[Q][P] =+ calculateWeight(P, Q);
    }
  }
  
  public void calculateWeights() {
    for (int P = 0; P < pages.size(); P++) {
      Page page = pages.get(P);
      if (page.getOutlinks().isEmpty()) {
        calculateEmptyLinks(P);
      } else {
        calculateNonemptyWeights(P);
        double weightSum = calculateWeightSum(P);
        normalizeWeights(P, weightSum, page);
      }
    }
  }
  
  double getQSum(int P) {
    double qSum = 0;
    for (int Q = 0; Q < pages.size(); Q++) {
       qSum += pages.get(Q).getScore() * weights[P][Q];
    }
    return qSum;
  }
  
  public boolean setNewScore() {
    boolean changed = false;
    for (int P = 0; P < pages.size(); P++) { 
      double qSum = getQSum(P);
      double newScore = (1-F) * pages.get(P).getBase() + F * qSum;
      pages.get(P).setNewScore(newScore);
      if (Math.abs(pages.get(P).getNewScore() - pages.get(P).getScore()) > epsilon) {
        changed = true;
      }
    }
    return changed;
  }
  
  public void calculateNewScore() {
    boolean changed = false;
    while (changed){
      changed = setNewScore();
      for (Page page: pages) {
        page.setScore(page.getNewScore());
      }
    }
  }
  
  public static int getWordCountPerUrl(String url) throws IOException {
    int wordCount = 0;
    URL link = new URL(url);
    BufferedReader in = new BufferedReader(new InputStreamReader(link.openStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      wordCount += countWords(inputLine);
    }
    in.close();
    return wordCount;
  }
  
  public static int countWords(String s){
    int wordCount = 0;
    boolean word = false;
    int endOfLine = s.length() - 1;
    for (int i = 0; i < s.length(); i++) {
        if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
            word = true;
        } else if (!Character.isLetter(s.charAt(i)) && word) {
            wordCount++;
            word = false;
        } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
            wordCount++;
        }
    }
    return wordCount;
}
  
  public static ArrayList<Page> getPagesFromDirectory(String path) throws IOException {
    ArrayList<Page> pages = new ArrayList<Page>();
    BufferedReader in = new BufferedReader(new FileReader(path));
    String inputLine;
    while ((inputLine = in.readLine()) != null)
      if ( inputLine.contains("href=") && inputLine.contains(".html") ) {
        int i = inputLine.lastIndexOf('=');
        String cut = inputLine.substring(i+2);
        int j = cut.indexOf(">");
        String url = cut.substring(0, j-1);
        int wc = getWordCountPerUrl(url);
        int x = url.lastIndexOf('/');
        String name = url.substring(x+1, url.length()-5);
        Page page = new Page(wc, name);
        pages.add(page);
      }
    in.close();
    return pages;
  }
  
  public static void main(String args[]) throws IOException {
    String path = args[0];
    double F =Double.parseDouble(args[1]);
    ArrayList<Page> pages = getPagesFromDirectory(path);
    PageRankAlgorithm algo = new PageRankAlgorithm(path, F, pages);
    for (Page page: algo.getPages()) {
      System.out.println(page.getPath());
      System.out.println(page.getWordCount());
    }
  }
 
}
