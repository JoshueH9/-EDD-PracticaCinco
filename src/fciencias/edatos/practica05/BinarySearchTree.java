package fciencias.edatos.practica05;
	
import java.util.Scanner;
import java.util.InputMismatchException;
/**
* Implementación de un árbol binario de busqueda.
* @author Emmanuel Cruz Hernández.
* @version 3.0 Noviembre 2021 (Anterior 2.0 Julio 2021).
* @since Estructuras de Datos 2022-1.
*/
public class BinarySearchTree<K extends Comparable<K>, T> implements TDABinarySearchTree<K, T>{
	
	/**
	 * Nodo para un árbol binario de búsqueda.
	 */
	public class BinaryNode{

		/** Clave. */
		public K key;

		/** Elemento. */
		public T element;

		/** Padre del nodo. */
		public BinaryNode parent;

		/** Hijo izquierdo. */
		public BinaryNode left;

		/** Hijo derecho. */
		public BinaryNode rigth;

		/**
		 * Crea un nuevo nodo.
		 * @param key la clave.
		 * @param element el elemento a almacenar.
		 * @param parent el padre del nodo.
		 */
		public BinaryNode(K key, T element, BinaryNode parent){
			this.key = key;
			this.element = element;
			this.parent = parent;
		}

	}

	/** Root */
	private BinaryNode root;

	@Override
	public T retrieve(K k){
		BinaryNode node = retrieve(root, k);
		if(node == null)
			return null;
		return node.element;
	}

	/**
	 * Método auxiliar de retrieve.
	 * */
	private BinaryNode retrieve(BinaryNode actual, K k){
		// No se encuentra el elemento
		if(actual == null)
			return null;

		// Si encontramos el elemento
		if(actual.key.compareTo(k) == 0)
			return actual;

		// Comparamos los elementos
		if(k.compareTo(actual.key) < 0){ // Verificamos en la izquierda
			return retrieve(actual.left, k);
		} else { // Verificar en la derecha
			return retrieve(actual.rigth, k);
		}
	}

	@Override
	public void insert(T e, K k){
		// Si es vacío entonces insertamos al nuevo elemento como la raíz del árbol
		if(root == null){
			BinaryNode inicio = new BinaryNode(k, e, null);
			this.root = inicio;
			return;
		}

		// Crear un nodo iterador que comience en la raíz
		BinaryNode iterador = root;

		// Invocar el método insert de tres parámetros
		insert(iterador, e, k);
	}

	/**
	 * Método auxiliar de insert.
	 * */
	private void insert(BinaryNode actual, T e, K k){

		// Comparamos las claves: la clave de actual con k. Con compareTo
		// Si la clave es menor verificamos que el hijo izquierdo no sea null
		// * Si es null insertamos el nuevo elemento como hijo izquierdo del actual. Si no recursión

		if(k.compareTo(actual.key) <= 0){
			if(actual.left==null)				
				actual.left = new BinaryNode(k, e, actual);
			else
				insert(actual.left,e,k);
		}

		// Si la clave es mayor
		// * Si es null insertamos el nuevo elemento como hijo derecho del actual. Si no recursión

		if(k.compareTo(actual.key) > 0){
			if(actual.rigth==null){
				actual.rigth = new BinaryNode(k, e, actual);
			}
			else
				insert(actual.rigth,e,k);
		}
	}

	@Override
	public T delete(K k){		
		
		T elementoEliminado = null;
		BinaryNode actual = null;

		// Aplicar el método retrieve
		// Si ese resultado es null -> regresar null
		if(retrieve(root, k)==null)
			return null;
		else
			actual = retrieve(root, k);	// Crear una variable que almacene el elemento en retrieve

		// Cuando tiene dos hijos
		if(actual.left != null && actual.rigth != null){

			elementoEliminado = actual.element;

			// Buscamos al maximo de los mínimos
			BinaryNode maximo = max(actual.left);

			//Auxiliares para el swap
			T elementoAux = maximo.element;
			K keyAux = maximo.key;

			// eliminar el nodo con el que se hizo swap
			delete(maximo.key);

			// hacemos un swap actual con el maximo de los mínimos
			actual.element = elementoAux;
			actual.key = keyAux;


			return elementoEliminado;
		}		

		// Cuando no tiene hijos
		if(actual.left == null && actual.rigth == null){

			elementoEliminado = actual.element;

			//Cuando el padre tiene un hijo
			if(actual.parent.left==null || actual.parent.rigth==null){
				if(actual.parent.left==null){
					actual.parent.rigth = null;
					return elementoEliminado;					
				}											// Verificar si es hijo izquierdo o es hijo derecho
				else{
					actual.parent.left = null;
					return elementoEliminado;
				}
			}

			//Cuando el padre tiene 2 hijos
			if(actual.parent.left.key == actual.key){
				actual.parent.left = null;		// Si es hijo izquiero hacer null el izquierdo del padre
				return elementoEliminado;
			}
			else{
				actual.parent.rigth = null;		// Si es hijo derecho hacer null el derecho del padre
				return elementoEliminado;
			}
		}
		
		// Cuando solo tiene un hijo
		if(actual.left != null || actual.rigth != null){

			elementoEliminado = actual.element;

			//Verificar si actual es un hijo izquierdo 
			if(actual.parent.left!=null){  //pregunta si el padre de actual tiene hijo izquierdo
				if(actual.key==actual.parent.left.key){		//pregunta si las claves son las mismas

					if(actual.left!=null){							//Si el hijo de actual es izquierdo
						actual.parent.left = actual.left;
						actual.left.parent = actual.parent;
						actual.parent = null;
						return elementoEliminado;
					}else{											//Si el hijo de actual es derecho
						actual.parent.left = actual.rigth;
						actual.rigth.parent = actual.parent;
						actual.parent = null;
						return elementoEliminado;
					}
				}					
			}

			//Verificar si actual es un hijo derecho
			if(actual.parent.rigth!=null){	//pregunta si el padre de actual tiene hijo derecho
				if(actual.key==actual.parent.rigth.key){	//pregunta si las claves son las mismas
					if(actual.left!=null){							//Si el hijo de actual es izquierdo
						actual.parent.rigth = actual.left;
						actual.left.parent = actual.parent;
						actual.parent = null;
						return elementoEliminado;
					}else{											//Si el hijo de actual es derecho
						actual.parent.rigth = actual.rigth;
						actual.rigth.parent = actual.parent;
						actual.parent = null;
						return elementoEliminado;
					}
				}
			}
		}
		return elementoEliminado;		
	}

	/**
	 * Método auxiliar para delete y findMax.
	 * */
	private BinaryNode max(BinaryNode node){

		// Verificar que no sea vacío -> return null
		if(node==null)
			return null;

		BinaryNode maximo = node;

		// Mientras sí tenga hijo derecha -> Que actual se mueva al derecho
		if(node.rigth!=null)
			maximo = max(node.rigth);
		else
			return maximo;	// Ya encontramos al nodo con clave mayor

		return maximo;
	}

	/**
	 * Método auxiliar para findMin.
	 * */
	private BinaryNode min(BinaryNode node){

		// Verificar que no sea vacío -> return null
		if(node==null)
			return null;

		BinaryNode minimo = node;

		// Mientras sí tenga hijo izquierdo -> Que actual se mueva al izquierdo
		if(node.left!=null)
			minimo = min(node.left);
		else
			return minimo;	// Ya encontramos al nodo con clave menor

		return minimo;
	}

	@Override
	public T findMin(){
		return min(root).element;
	}

	@Override
	public T findMax(){
		return max(root).element;
	}

	@Override
	public void preorden(){
		this.preorden(root);
	}

	/**
	 * Método auxiliar de preorden.
	 * */
	private void preorden(BinaryNode node){
		// Primero verifica la raiz
		if(node == null)
			return;

		System.out.println(node.element);

		// Aplica preorden al izquierdo
		preorden(node.left);

		// Aplica preorden al derecho
		preorden(node.rigth);
	}

	@Override
	public void inorden(){
		this.inorden(root);
	}

	/**
	 * Método auxiliar de inorden.
	 * */
	private void inorden(BinaryNode node){
		// Primero verifica la raiz
		if(node == null)
			return;

		// Aplica inorden al izquierdo
		inorden(node.left);

		System.out.println(node.element);

		// Aplica inorden al derecho
		inorden(node.rigth);
	}

	@Override
	public void postorden(){
		this.postorden(root);
	}

	/**
	 * Método auxiliar de postorden.
	 * */
	private void postorden(BinaryNode node){
		// Primero verifica la raiz
		if(node == null)
			return;

		// Aplica postorden al izquierdo
		postorden(node.left);

		// Aplica postorden al derecho
		postorden(node.rigth);

		System.out.println(node.element);
	}

	@Override
	public boolean isEmpty(){
		return (root == null ? true:false);
	}



	public static void main(String[] args) {

		TDABinarySearchTree<Integer,String> tree = new BinarySearchTree<>();
		int clave = 0;
		String cadena = "";

		String rojo = "\u001B[31m", verde = "\u001B[32m", amarillo = "\u001B[33m", morado = "\u001B[35m", blanco = "\u001B[37m";
        Scanner sc = new Scanner(System.in);
        System.out.println("PRACTICA 05 --- Arboles binarios de búsqueda\n\nPara mi practica implementé arboles que guarden listas de caracteres(String)\nY las claves con las que se ordenan son de tipo Integer.\n\n");
        System.out.println("──▒▒▒▒▒▒───▄████▄\n─▒─▄▒─▄▒──███▄█▀ \n─▒▒▒▒▒▒▒─▐████──█─█ \n─▒▒▒▒▒▒▒──█████▄ \n─▒─▒─▒─▒───▀████▀ \n");
        System.out.println("\nPresiona Enter para comenzar.");
        sc.nextLine();

		int opcion = 0;

        do{
	
	    System.out.println("\n"+verde+"HOLA BIENVENIDO AL MENU."+blanco+"\n");

	    System.out.println(verde+"Elije una de las siguientes opciones:"+blanco+"\n\n "
			       +amarillo+"1) "+verde+" Método Retrieve"+blanco+"\n "
			       +amarillo+"2) "+verde+" Método Insert"+blanco+"\n "
			       +amarillo+"3) "+verde+" Método Delete"+blanco+"\n "
			       +amarillo+"4) "+verde+" Método FindMin"+blanco+"\n "
			       +amarillo+"5) "+verde+" Método FindMax"+blanco+"\n "
			       +amarillo+"6) "+verde+" Recorrido con Preorden"+blanco+"\n "
			       +amarillo+"7) "+verde+" Recorrido con Inorden"+blanco+"\n "
			       +amarillo+"8) "+verde+" Recorrido con Postorden"+blanco+"\n "
			       +amarillo+"9) "+verde+" Método isEmpty"+blanco+"\n\n" //Aqui tiene 2 saltos de linea para que resalte mejor lo de salir del menú.
			       +amarillo+"10) "+verde+"Salir del menú"+blanco);
	    
	    try{  
			System.out.print(blanco+"\n"+amarillo+" Opcion: "+blanco+"\n");
			opcion = sc.nextInt();
		}catch(InputMismatchException ime){
			System.out.println(rojo+"ERROR: Ingresa un numero."+blanco);
			sc.nextLine();
		}catch(Exception e){
			System.out.println(rojo+"\n\nERROR INESPERADO EN EL CODIGO.\n\n");
		}

		    switch(opcion){
			
		    case 1:
		    	try{
		    		System.out.println(verde+"Inserta la clave a buscar:"+blanco);
		    		clave = sc.nextInt();

		    		if(tree.retrieve(clave)==null)
						System.out.println("El elemento con esa clave no existe en el arbol");
					else
						System.out.println("El elemento con esa clave es: "+tree.retrieve(clave));

					try{
            			Thread.sleep(3000);
        			}catch(InterruptedException ie){}  

		   		}catch(InputMismatchException ime){
					System.out.println(rojo+"ERROR: Ingresa un numero."+blanco);
					sc.nextLine();
				}catch(Exception e){
					System.out.println(rojo+"\n\nERROR INESPERADO EN EL CODIGO.\n\n");
				}
			break;

		    case 2:
		    	try{
		    		System.out.println(verde+"Inserta la clave en donde se guardara:"+blanco);
		    		clave = sc.nextInt();

		    		sc.nextLine();
		    		System.out.println(verde+"Inserta la cadena a guardar:"+blanco);
		    		cadena = sc.nextLine()+"";

		    		tree.insert(cadena, clave);

		    		System.out.println("La cadena "+cadena+" se a guardado con la clave "+clave);

					try{
            			Thread.sleep(3000);
        			}catch(InterruptedException ie){} 

		   		}catch(InputMismatchException ime){
					System.out.println(rojo+"ERROR: Ingresa un numero."+blanco);
					sc.nextLine();
				}catch(Exception e){
					System.out.println(rojo+"\n\nERROR INESPERADO EN EL CODIGO.\n\n");
				}
			break;

		    case 3:
		    	try{
		    		System.out.println(verde+"Inserta la clave del elemento a eliminar:"+blanco);
		    		clave = sc.nextInt();

		    		if(tree.retrieve(clave)==null)
						System.out.println("El elemento con esa clave no existe en el arbol");
					else{
						tree.delete(clave);
						System.out.println("El elemento con la clave "+clave+" se ha eliminado");
					}

					try{
            			Thread.sleep(3000);
        			}catch(InterruptedException ie){}  

		   		}catch(InputMismatchException ime){
					System.out.println(rojo+"ERROR: Ingresa un numero."+blanco);
					sc.nextLine();
				}catch(Exception e){
					System.out.println(rojo+"\n\nERROR INESPERADO EN EL CODIGO.\n\n");
				}
			break;

		    case 4:
		    	System.out.println("El elemento con clave con valor minimo del arbol es "+tree.findMin());
		    	try{
            		Thread.sleep(3000);
        		}catch(InterruptedException ie){} 
			break;

		    case 5:
		    	System.out.println("El elemento con clave con valor máximo del arbol es "+tree.findMax());
		    	try{
            		Thread.sleep(3000);
        		}catch(InterruptedException ie){} 
			break;

		    case 6:
		    	System.out.println("Recorrido del arbol en Preorden\n");
		    	tree.preorden();
		    	try{
            		Thread.sleep(3000);
        		}catch(InterruptedException ie){} 
			break;

		    case 7:
		    	System.out.println("Recorrido del arbol en Inorden\n");
		    	tree.inorden();
		    	try{
            		Thread.sleep(3000);
        		}catch(InterruptedException ie){} 
			break;

		    case 8:
		    	System.out.println("Recorrido del arbol en Postorden\n");
		    	tree.postorden();
		    	try{
            		Thread.sleep(3000);
        		}catch(InterruptedException ie){} 
			break;

		    case 9:		    	
		    	if(tree.isEmpty())
		    		System.out.println("El arbol es vacio");
		    	else
		    		System.out.println("El arbol NO es vacio");
		    	try{
            		Thread.sleep(3000);
        		}catch(InterruptedException ie){} 
			break;

		    case 10:
				System.out.println(rojo+"\n ¡Adioooos!\n"+blanco);
		    	try{
            		Thread.sleep(3000);
        		}catch(InterruptedException ie){} 
			break;
			
		    default:
				System.out.println(rojo+"\nEsa opción no es válida\n");
				opcion=20;
		    	try{
            		Thread.sleep(3000);
        		}catch(InterruptedException ie){} 			
			break;
		    }
		}while (opcion!=10);
	}
}