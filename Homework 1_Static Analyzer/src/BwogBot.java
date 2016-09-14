import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BwogBot {

	
	/*
	Chose to go with SeparateChainingMap instead of AvlMap because put and get methods
	cost O(1) with small load factor rather than O(logN) of AvlMap put/get methods.
	*/
	private SeparateChainingMap<String, Integer> chainMap;
	
  public BwogBot() {
	  chainMap = new SeparateChainingMap<String, Integer>();
  }
  
  

  public void readFile(String fileName) throws IOException {
	  FileReader fr = new FileReader(fileName);
	  BufferedReader br = new BufferedReader(fr);
	  
	  String comment;
	  String word = "";
	  
	  
	  while((comment=br.readLine())!=null){
		  String[] commentsWords = comment.split(" ");
		  for(int i=0; i<commentsWords.length;i++){
			  if(chainMap.get(commentsWords[i])==null){  
				  chainMap.put(commentsWords[i], 1);
				  
				  System.out.println(commentsWords[i]+" "+1); 
			  }else{
				  
				  int newValue = Integer.valueOf(String.valueOf(chainMap.get(commentsWords[i])));
				  newValue++;
				  chainMap.put(commentsWords[i], newValue);
				
				  System.out.println(commentsWords[i]+" "+newValue); 
			  }
			  
		  }
		  
		  /*
		  
		  for(int i=0; i<comment.length();i++){
			  //End of word
			  
			  if(comment.charAt(i)==' '){
				  
				  
				  if(word==null||word==" "){

					  continue;
				  }
				  word = 
				  if(chainMap.get(word)==null){
					chainMap.put(word, 1);
				  }else{	
					int newValue = Integer.valueOf(String.valueOf(chainMap.get(word)));
					newValue++;
					chainMap.put(word, newValue);
					System.out.println(word +" "+newValue);
				  }	
				    word="";
				  	//continue;
			  }

			  word += comment.charAt(i);
			  
		  }*/
	  }
  }

  public int getCount(String word) {
	  
	  
	  if(chainMap.get(word)==null)
		  return 0;
		  
	  int getCount = chainMap.get(word);
    return getCount;
  }

/*
 * Attempted to do the extra credit using a list<V> (getValues) method which returns all the 
 * values in the SeparateChainMap. Doesn't work.
  public List<String> getNMostPopularWords(int n) {
	  List<Integer> lst = new ArrayList<Integer>();
	  String word;
	  
	  lst = chainMap.getValues();
	  
	  
	for(int i = 0; i<lst.size();i++){
		
		
		//word
		int getCount = getCount(chainMap.get(lst.get(i)));
		
		if(n>=getCount){
			  lst.add(word);
		  }
	}
		
	return lst;
}
  */

  public List<String> getNMostPopularWords(int n){
	  return null;
  }
	 
		  

  public Map<String, Integer> getMap() {
    return chainMap;
  }

  public static void main(String[] args) throws IOException {
    BwogBot bot = new BwogBot();
    bot.readFile("C:/Users/Britain/Documents/GitHub/homework-bic2106/4/src/comments.txt");
    System.out.println(bot.getCount("hamdel")); // because linan's hungry now
    System.out.println(bot.getCount("hodor"));
    System.out.println(bot.getCount("text"));
    
    //System.out.println(bot.getNMostPopularWords(100));
  }
}
