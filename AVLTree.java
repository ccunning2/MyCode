
public class AVLTree {
	private Node head;
	

	
	public Node getHead() {
		return head;
	}



	public void setHead(Node head) {
		this.head = head;
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
					while (currentNode != this.head){
						currentNode.setWeight();
						if (currentNode.getWeight() > 1 ){
							rotateLeft(currentNode);
							System.out.println("We have a weight greater than or equal 2 at " + currentNode.getValue());
							return; //TODO Attention
						}
						currentNode = currentNode.getParent(); //Move back up the tree
					}
					if (currentNode == this.head){
						currentNode.setWeight();
						if (currentNode.getWeight() > Math.abs(1)){
							rotateLeft(currentNode);
							System.out.println("We have a weight greater than or equal 2 at " + currentNode.getValue());
							return;
						}
					}
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
					while (currentNode != this.head){
						currentNode.setWeight();
						if (currentNode.getWeight() > Math.abs(1)){
							//Do some rotation or something
							System.out.println("We have a weight greater than or equal 2 at " + currentNode.getValue());
						}
						currentNode = currentNode.getParent(); //Move back up the tree
					}
					if (currentNode == this.head){
						currentNode.setWeight();
						if (currentNode.getWeight() > Math.abs(1)){
							//Do some rotation or something
							System.out.println("We have a weight greater than or equal 2 at " + currentNode.getValue());
						}
					}
					return;
				} else {
					currentNode = currentNode.getLeft();
					continue;
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
		}
		
		if(b.getLeft() != null) {
			a.setRight(b.getLeft());
		} else {
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
		}
		
		if(b.getRight() != null) {
			a.setLeft(b.getRight());
		} else {
			a.setChildren(false);
		}
		
		b.setRight(a);
	
		a.setParent(b);
		
	}
	
	
		
		
	
	
	

	
	

	public static void main(String[] args) {
		int[] values = {2,3,1,5,6};
		
		AVLTree myTree = new AVLTree();
		
		for(int i=0; i< values.length; i++){
			myTree.addNode(values[i]);
		}
		
		myTree.head.getRight().setWeight();
		
	
		

	}

	
	
	class Node {
		private int value;
		private Node left;
		private Node right;
		private int weight;
		private boolean children;
		private Node parent;
		
		
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
			if(node == null)
				return 0;
			if (!node.children){
				return 0;
			} else {
				if (node.right != null && node.left != null){
				return countWeights(node.right) + countWeights(node.left);
			}
				if (node.right != null){
					return countWeights(node.right) + 1;
				}
				if (node.left != null){
					return countWeights(node.left) + 1;
				}
		
			return 0;	
			}
			
		}
	
		public void setWeight(){
			if (!this.children){
				this.weight = 0;
			} else {
				this.weight = 1 + (countWeights(this.right) - countWeights(this.left));
			}
		}

		public int getWeight() {
			return weight;
		}
		
		
		
	}
	
}

