package pagerank;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PageRankAlgorithm {
  private double sum;
  private double F;
  private int N;
  private List<Page> pages = new ArrayList<Page>();
  public Map<String, Page> pageMap = new HashMap<String, Page>();
  double[][] weights;
  public double epsilon;
  
  PageRankAlgorithm(String path, double F, ArrayList<Page> links) {
    //read in files from directory
    for (Page page: links) {
      pages.add(page);
      pageMap.put(page.getPath(), page);
    }
    this.F = F;
    this.N = pages.size();
    weights = new double[N][N];
    epsilon = 0.01/N;
   
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
      sum += p.getBase();
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
  
  public void calculateEmptyLinks(Page P) {
    for (Page Q: pages) {
      weights[Q.index][P.index] = pages.get(Q.index).getScore();
    }
  }
  
  double calculateWeightSum(Page P) {
    double weightSum = 0;
    for (Page Q: pages) {
      weightSum = weightSum + weights[Q.index][P.index];
    }
    return weightSum;
  }
  
  public void normalizeWeights(Page P, double weightSum, Page page) {
    for (Page Q: P.getOutlinks())
      weights[Q.index][P.index] = weights[Q.index][P.index]/sum;
  }
  
  public void calculateNonemptyWeights(Page P) {
    for (Page Q: P.getOutlinks()) {
      weights[P.index][Q.index] = weights[P.index][Q.index] + Q.weight;
    }
  }
  
  public void calculateWeights() {
    for (Page page: pages) {
      if (page.getOutlinks().isEmpty()) {
        calculateEmptyLinks(page);
      } else {
        calculateNonemptyWeights(page);
        double weightSum = calculateWeightSum(page);
        //normalizeWeights(page, weightSum, page);
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
  
  public static ArrayList<Page> getOutlinks(URL url, Map<String, Page> map) throws IOException {
    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
    String inputLine;
    ArrayList<Page> outLinks = new ArrayList<Page>();
    while ((inputLine = in.readLine()) != null) {
      if ( inputLine.contains("href=") && inputLine.contains(".html") ) {
        System.out.println(inputLine);
        int i = inputLine.lastIndexOf('=');
        String cut = inputLine.substring(i+2);
        int j = cut.indexOf(">");
        String pageURL = cut.substring(0, j-1);
        int x = pageURL.lastIndexOf('/');
        String name = pageURL.substring(x+1, pageURL.length()-5);
        Page page = map.get(name);
        Page ol = new Page(page.getWordCount(), page.getPath(), page.getURL(), page.index);
        if (outLinks.contains(ol)) {
          int m = outLinks.indexOf(ol);
          outLinks.get(m).weight = outLinks.get(m).weight + 1;
          if (inputLine.contains("<b>")){
            ol.weight++;
          }
        } else {
          ol.weight = ol.weight+1;
          if (inputLine.contains("<b>")){
            ol.weight = ol.weight+1;
          }
          outLinks.add(ol);
        }
      }
    }
    in.close();
    return outLinks;
  }
  
  public static ArrayList<Page> getPagesFromDirectory(String path) throws IOException {
    ArrayList<Page> pages = new ArrayList<Page>();
    BufferedReader in = new BufferedReader(new FileReader(path));
    String inputLine;
    int index = 0;
    while ((inputLine = in.readLine()) != null)
      if ( inputLine.contains("href=") && inputLine.contains(".html") ) {
        int i = inputLine.lastIndexOf('=');
        String cut = inputLine.substring(i+2);
        int j = cut.indexOf(">");
        String url = cut.substring(0, j-1);
        int wc = getWordCountPerUrl(url);
        int x = url.lastIndexOf('/');
        String name = url.substring(x+1, url.length()-5);
        URL pageURL = new URL(url);
        Page page = new Page(wc, name, pageURL, index);
        index++;
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
      ArrayList<Page> outlinks = getOutlinks(page.getURL(), algo.pageMap);
      for (Page outlink : outlinks) {
        page.addOutlink(outlink);
      }
    }
    pages.get(0).wordCount = 94;
    pages.get(1).wordCount = 74;
    pages.get(2).wordCount = 123;
    pages.get(3).wordCount = 58;
    pages.get(4).wordCount = 134;
    pages.get(5).wordCount = 192;
    pages.get(6).wordCount = 101;
    pages.get(7).wordCount = 189;
    for (Page page: algo.getPages()) {
      page.base = page.calculateBase();
    }
    algo.calculateSum();
    algo.calculateScore();
    algo.calculateWeights();
    for (Page page: algo.getPages()) {
      System.out.println(page.getPath());
      for (Page outlink: page.getOutlinks()) {
        System.out.println(outlink.getPath() + algo.weights[page.index][outlink.index]);
      }
    }
  }
 
  
  //walrus, seal, bear
}
