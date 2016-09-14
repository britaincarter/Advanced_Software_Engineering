public class AvlMap<K extends Comparable<? super K>, V> implements Map<K, V> {


	private AvlTree<Pair<K,V>> tree;
	
  public AvlMap() {
	  tree = new AvlTree<Pair<K,V>>();
  }
  
  @Override
  public void put(K key, V value) {
	  if(key==null||value==null){
		  throw new IllegalArgumentException();
	  }
	  
	  Pair<K, V> p = new Pair<K,V>(key, value);
	  
	  tree.insert(p);
  }
  
  @Override
  public V get(K key) {
	  Pair<K,V> p = new Pair<K,V>(key, null);
	  return tree.get(p).value;
  }
  /*
  public static void main(String[] args){

	    AvlMap<Integer, String> avlMap = new AvlMap<Integer, String>();
	    
	    avlMap.put(0, "Hey");
	    String test = avlMap.get(0);
	    System.out.println(test);

  }
*/  
}
