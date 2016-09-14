import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SeparateChainingMap<K extends Comparable<? super K>, V> implements Map<K, V> {

	//Global variable counting number of pairs.
  int counter=0;
	
  public static final int SCALE_FACTOR = 2;
  public static final int INITIAL_TABLE_SIZE = 8;
  public static final double MAX_LOAD_FACTOR = 1.0;  
  
  private AvlTree<Pair<K,V>> tree;
  
  @SuppressWarnings("unchecked")
  private LinkedList<Pair<K,V>> [] table = (LinkedList<Pair<K,V>>[])new LinkedList [INITIAL_TABLE_SIZE];
  
  
  public SeparateChainingMap() {
	  tree = new AvlTree<Pair<K,V>>();
  }

  //Number of pairs, keep a global variable for every put.
  public int getSize() {
    return counter;
  }

  //return table size of LinkedList<Pair<K,V>> [] table
  public int getTableSize() {
    return table.length;
  }
  
  //method to return all values in table[] for the getNMostPopularWords.
  public List<V> getValues(){
	  List<V> lstValues = new ArrayList<V>();
	  for(int i = 0; i<table.length;i++){
		  for(int k =0; k<table[i].size();k++){
			  lstValues.add(table[i].get(k).value);
		  }
	  }
	  
	  return lstValues;
	  
  }
  
  //Puts a pair into the table[]
  public void put(K key, V value) {
	  if(key==null||value==null){
		  throw new IllegalArgumentException();
	  }
	  
	  counter++;
	  
	  //Check the load factor is >=1
	  if(getSize()/getTableSize()>=MAX_LOAD_FACTOR){
		  	upsize();
		  }
	  
	  //counter++;
	  
	  Pair<K, V> p = new Pair<K,V>(key, value);
	  
	  //Hashfunction
	  int hashcode = key.hashCode()%table.length;
	  
	  if(hashcode<0){
		  hashcode = hashcode+table.length;
	  }
	  

	  //System.out.println("Put HashCode: "+hashcode);
	  
	  //If table is empty at a position
	  if(table[hashcode]==null){
		  
		  table[hashcode]= new LinkedList<Pair<K,V>>();
		  table[hashcode].addFirst(p);
		  //System.out.println(hashcode);
		  //System.out.println("First to cell: "+p.value);
		  
	  //If the table has a pair at the position
	  }else if(table[hashcode]!=null){
		  //Check to see if there are any identical pairs.
		  for(int i = 0; i<table[hashcode].size();i++){
			  if(p.compareTo(table[hashcode].get(i))==0){
			  	Pair<K,V>n = table[hashcode].remove(i);		  	
			  	table[hashcode].addFirst(p);
			  	//System.out.println("Same key has new value of: "+p.value +" Old Value: "+n.value);
			  	counter--;
			  	return;
		  	}
		  }
		  
		  
		  table[hashcode].addFirst(p);
		  //System.out.println(hashcode);
		  //System.out.println("Appended to a cell: "+p.value);
	  }
	  
	  
	 
	  
	  //System.out.println("counter: "+counter);
  }

  
  //returns value of a given key in table[]
  public V get(K key) {
	  if(key==null){
		  return null;
	  }
	  Pair<K,V> p = new Pair<K,V>(key, null);
	  
	  int hashCode = key.hashCode()%table.length;
	  
	  if(hashCode<0){
		  hashCode+=table.length;
	  }
	  //System.out.println("Get HashCode: "+hashCode);
	  
	  if(table[hashCode]==null){
		  return null;
	  
	  
	  
	  }else{
		  //Check if there are any equivalent values in the hashcode location of linkedlist
		  for(int i = 0; i<table[hashCode].size(); i++){
			  if(p.compareTo(table[hashCode].get(i))==0){
				  V value = table[hashCode].get(i).value;
				  return value;
			  }
		  }
	  }
	  	return null;
  }
  
  
  
  public void upsize() {
	  //System.out.println("Entered Upsize");
	  
	  
	  LinkedList<Pair<K,V>> [] oldtable = (LinkedList<Pair<K,V>>[]) new LinkedList [getTableSize()*SCALE_FACTOR];
	  oldtable = table;
	  
	  int tablesizebeforecleared = getTableSize();
	  int doubletable = tablesizebeforecleared*SCALE_FACTOR;
	  
	  /*
	  for(int i = 0; i<table.length;i++){
		  if(table[i]==null)continue;
		  
		  int numElements = table[i].size();
		  for(int k = 0; k<numElements;k++){
			  Pair<K,V> p = table[i].removeFirst();
		  	  System.out.println(p.value);
			  oldtable[i].add(p);
			  
	  	  }
	  }
	  
	  for(int i = 0; i<oldtable.length;i++){
		  System.out.println(oldtable[i]);
	  }
	  */
	 
	  
	  //Increase the size of table by SCALE_FACTOR
	  table = new LinkedList[( doubletable) ];
	  
	  //
	  for(int i = 0; i<oldtable.length;i++){
		  //If a cell is empty in the oldtable skip to the next cell
		  if(oldtable[i]==null){
			  //System.out.println(i);
			  continue;
		  }
		  //Copy over the old pairs into the new table[] at cell at i.
		  for(int k =0; k<oldtable[i].size();k++){
			  //System.out.println("Length of Table at i: "+oldtable[i].size()+" Linked List index: "+k);
			  Pair<K,V> p = oldtable[i].get(k);
			  
			  //HashFunction
			  int hashcode = p.key.hashCode()%table.length;
			  
			  if(hashcode<0){
				  hashcode+=table.length;
			  }
			  
			 
			  //System.out.println("Key: "+p.key+" hashcode: " +hashcode);
			  
			  //Same checks as put for collisions
			  if(table[hashcode]!=null){
				 table[hashcode].addFirst(p);
				 //System.out.println(" Value: "+p.value);
				  
				  
			  }else if(table[hashcode]==null){
				  table[hashcode]= new LinkedList<Pair<K,V>>();
				  table[hashcode].addFirst(p);
				  //System.out.println("Null-UpSize: "+k+" Value: "+p.value);
				  
			  }
		  }
	  }
  }
	  
	  
	  
  
  public static void main(String[] args){

	    SeparateChainingMap<Integer, String> avlMap = new SeparateChainingMap<Integer, String>();
	    
	    avlMap.put(0, "Zero-1");
	    avlMap.put(8, "Eight-4");
	    avlMap.put(0, "Zero-2");
	    avlMap.put(0, "Zero-3");
	    avlMap.put(1, "One-1");
	    avlMap.put(2, "Two-1");
	    avlMap.put(1, "One-2");
	    avlMap.put(4, "Four-1");
	    avlMap.put(5, "Five-1");
	    avlMap.put(6, "Six-1");
	    avlMap.put(13, "Thirteen-1");
	    avlMap.put(14, "Fourteen-1");
	    avlMap.put(15, "Fifteen-1");
	    avlMap.put(16, "Sixteen-1");
	    avlMap.put(17, "Seventeen-1");
	    System.out.println("GetSize: "+avlMap.getTableSize());
	    System.out.println("GetPairs: "+avlMap.getSize());
	    String test = avlMap.get(0);
	    String test1 = avlMap.get(1);
	    String test2 = avlMap.get(2);
	    String test3 = avlMap.get(14);
	    String test4 = avlMap.get(5);
	    String test5 = avlMap.get(13);
	    String test8 = avlMap.get(8);
	    String test16 = avlMap.get(16);
	    String test15 = avlMap.get(15);
	    String test17 = avlMap.get(17);
	    
	    System.out.println(test);
	    System.out.println(test1);
	    System.out.println(test2);
	    System.out.println(test3);
	    System.out.println(test4);
	    //System.out.println();
	    System.out.println(test5);
	    System.out.println(test8);
	    System.out.println(test15);
	    System.out.println(test16);
	    System.out.println(test17);
	    

}
}
