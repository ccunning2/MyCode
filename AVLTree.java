/**
 * 
 * @author cameroncunning1
 *
 */
public class AVLTree {
	private Node head;
	

	
	public Node getHead() {
		return head;
	}



	public void setHead(Node head) {
		this.head = head;
	}

	
	public Node findNode(int value) throws NullPointerException { //base case
		if (this.head.getValue() == value){
			return this.head;
		} else {
			return findNode(value, this.head);
		}
	}
	
	public Node findNode(int value, Node node) throws NullPointerException{
		if (node == null){
			return new Node(); //Not sure best way to handle this case. Depends on ultimate implementation of tree
		}
		if (node.getValue() == value){
			return node;
		} 
		
		if (value < node.getValue()){
			return findNode(value, node.getLeft());
		} 
		
		if (value > node.getValue()) {
			return findNode(value, node.getRight());
		}
		
		return null;
		
		
	}


	public void removeNode(int value){
		Node removing = this.findNode(value);
		if (removing.isNull){
			System.out.println(value + " " + removing.toString());
		}
		
		if(!removing.hasChildren()){ //node is a leaf, just get rid of it
			Node parent = removing.getParent();
			
			if(parent.getValue() < value){
				parent.setRight(null);
			} else {
				parent.setLeft(null);
			}
			removing.setParent(null);
			checkRotation(parent);
			return;
		}
		
		
		if((removing.getRight() != null) && (removing.getLeft() != null)) { //Node has two children
			Node largest = findLargest(removing.getLeft()); //Find largest value of left subtree
			Node currentNode = largest.getParent();
			currentNode.setRight(null);
			
			if (currentNode.getLeft() == null) { //No more children, yay
				currentNode.setChildren(false);
			}
			
			//Removing gets value of largest
			removing.setValue(largest.getValue());
			
			//Now need to checkweights from parent of largest up to the head and rotate if necessary
			checkRotation(currentNode);	
			return;
		}
		
		if ((removing.getLeft() == null) && (removing.getRight() != null)){
			Node parent = removing.getParent();
			Node check = removing.getRight();
			if(parent.getLeft() == removing){
				parent.setLeft(check);
			} else {
				parent.setRight(check);
			}
			removing.setParent(null);
			removing.setRight(null);
			checkRotation(check);
			return;
		}
		
		if ((removing.getLeft() != null) && (removing.getRight() == null)){
			Node parent = removing.getParent();
			Node check = removing.getLeft();
			if(parent.getLeft() == removing){
				parent.setLeft(check);
			} else {
				parent.setRight(check);
			}
			removing.setParent(null);
			removing.setLeft(null);
			checkRotation(check);
			return;
		}
		
	}
	
	public Node findLargest(Node start){
		//From starting point, go right as far as possible
		Node currentNode = start;
		while(currentNode.getRight() != null){
			currentNode = currentNode.getRight();
		}
		return currentNode;
	}
	
	
	public void addNode(int value){
		
		Node currentNode;
		
		
		if (this.head == null){
		 	 this.head = new Node(value);
		 	 return;
		} else currentNode = head;
		
		
		while (true){
			if (currentNode.value == value){
				//Error, values should be distinct. Skip for now
				return;
			}
			if (value > currentNode.value){
				if (currentNode.getRight() == null){
					currentNode.setRight(new Node(value));
					currentNode.setChildren(true);
					currentNode.getRight().setParent(currentNode);
					checkRotation(currentNode);
					return;
				} else {
					currentNode = currentNode.getRight();
					continue;
				}
			}
				
			if (value < currentNode.value)
				if (currentNode.getLeft() == null){
					currentNode.setLeft(new Node(value));
					currentNode.setChildren(true);
					currentNode.getLeft().setParent(currentNode);
					checkRotation(currentNode);
					return;
				} else {
					currentNode = currentNode.getLeft();
					continue;
				}
		}
		
		
		
		
		
		
	}



	private void checkRotation(Node currentNode) {
		while (currentNode != this.head){
			currentNode.setWeight();
			if (currentNode.getWeight() > 1 ){ //Subtree is right heavy
				currentNode.getRight().setWeight();
				if (currentNode.getRight().getWeight() < 0) { //Subtree is left heavy
					rotateDoubleLeft(currentNode);
				} else {
					rotateLeft(currentNode);
				}
				
			} else if (currentNode.getWeight() < -1) { //subtree is left heavy
				currentNode.getLeft().setWeight();
				if (currentNode.getLeft().getWeight() > 0){ //Subtree is right heavy
					rotateDoubleRight(currentNode);
				} else {
					rotateRight(currentNode);
				}
			}
			currentNode = currentNode.getParent(); //Move back up the tree
		}
		if (currentNode == this.head){
			currentNode.setWeight();
			if (currentNode.getWeight() > 1 ){ //Subtree is right heavy
				currentNode.getRight().setWeight();
				if (currentNode.getRight().getWeight() < 0) { //Subtree is left heavy
					rotateDoubleLeft(currentNode);
				} else {
					rotateLeft(currentNode);
				}
				
			} else if (currentNode.getWeight() < -1) { //subtree is left heavy
				currentNode.getLeft().setWeight();
				if (currentNode.getLeft().getWeight() > 0){ //Subtree is right heavy
					rotateDoubleRight(currentNode);
				} else {
					rotateRight(currentNode);
				}
			}
		}
	}

	
	
	public void rotateLeft(Node root){
		
		Node a = root;
		Node b = root.getRight();
		
		if (a.getParent() != null){
			int value = a.getParent().getValue();
			if (b.value > value){
				a.getParent().setRight(b);
			} else {
				a.getParent().setLeft(b);
			}
			b.setParent(a.getParent());
		} else { //Parent is null, so a is the head
		this.setHead(b);
		b.setParent(null);
		}
		
		if(b.getLeft() != null) {
			a.setRight(b.getLeft());
			a.getRight().setParent(a);
		} else {
			a.setRight(null);
			if(a.getRight() == null && a.getLeft() == null)
			a.setChildren(false);
		}
		
		b.setLeft(a);
	
		a.setParent(b);
		
	}
	
	public void rotateDoubleLeft(Node root){
		rotateRight(root.getRight());
		rotateLeft(root);
	}
	
	
	public void rotateDoubleRight(Node root){
		rotateLeft(root.getLeft());
		rotateRight(root);
	}
	
	public void rotateRight(Node root){
		Node a = root;
		Node b = root.getLeft();
		
		if (a.getParent() != null){
			int value = a.getParent().getValue();
			if (b.value > value){
				a.getParent().setRight(b);
			} else {
				a.getParent().setLeft(b);
			}
			b.setParent(a.getParent());
		} else { //Parent is null, so a is the head
		this.setHead(b);
		b.setParent(null);
		}
		
		if(b.getRight() != null) {
			a.setLeft(b.getRight());
			a.getLeft().setParent(a);
		} else {
			a.setLeft(null);
			if(a.getRight() == null && a.getLeft() == null)
			a.setChildren(false);
		}
		
		b.setRight(a);
	
		a.setParent(b);
		
	}
	
	
		
		
	
	
	

	
	
     
	public static void main(String[] args) {
		int[] values = {1,2,3,4,5,7};
		
		AVLTree myTree = new AVLTree();
		
		for(int i=0; i< values.length; i++){
			myTree.addNode(values[i]);
		}
		
				
		myTree.removeNode(4);
		System.out.println();
		

	}

	
	
	class Node {
		private int value;
		private Node left;
		private Node right;
		private int weight;
		private boolean children;
		private Node parent;
		private boolean isNull; //To be used in find method
		
		
		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public boolean hasChildren() {
			return children;
		}

		public void setChildren(boolean children) {
			this.children = children;
		}

		public Node(int value){
			this.value = value;
			this.weight = 0;
			this.isNull = false;
		}
		
		public Node() {
			this.isNull = true;
		}
		
		public int getValue() {
			return value;
		}
		public void setValue(int value) {
			this.value = value;
		}
		public Node getLeft() {
			return left;
		}
		public void setLeft(Node left) {
			this.left = left;
		}
		public Node getRight() {
			return right;
		}
		public void setRight(Node right) {
			this.right = right;
		}
		
		
		public int countWeights(Node node){
			if(node == null) {
				return 0;
			} else {
				return (Math.max(countWeights(node.getLeft()) + 1, countWeights(node.getRight()) + 1));
			}
			
		}
	
		public void setWeight(){
			if (!this.children){
				this.weight = 0;
			} else {
				
				this.weight = (countWeights(this.getRight()) - countWeights(this.getLeft()));
			}
		}

		public int getWeight() {
			return weight;
		}

		@Override
		public String toString() {
			if (this.isNull){
				return "node does not exist.";
			} else {
				return Integer.toString(this.getValue());
			}
		}
		
		
		
	}
	
}

