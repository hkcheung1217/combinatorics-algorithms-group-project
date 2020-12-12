package pizzasliceproject;

public class Tower {
	private int[][] slices;
	Path minObs;
	Path solPath;
	
	public Tower(int[][] slices) {
		this.slices = slices;
		this.minObs = null;
		this.solPath = null;
	}
	
	public void findPaths() {
		solPath = new Path(0);
		solPath.updateCollisions(slices[0].clone());
		findPaths(solPath);
	}
	
	public void findPaths(Path curNode) {
		if(minObs==null||minObs.collisions>curNode.collisions) {
			if(curNode.height<slices.length) {
				for(int i=0;i<3;i++)
					findPaths(curNode.newChild(i, getRotatedSlice(slices[curNode.height],i)));
			}else if(curNode.height==slices.length) {
				minObs = curNode;
				//System.out.println("new min: "+minObs.collisions);
			}
		}
	}
	
	public int[] getRotatedSlice(int[] slice, int r) {
		int[] rot = new int[3];
		for(int i=0;i<3;i++)
			rot[(i+r)%3]=slice[i];
		return rot;
	}
	
	public void printSlice(int[] slice) {
		System.out.print("[ ");
		for(int i:slice)
			System.out.print(i+" ");
		System.out.print("]\n");
	}
	
	@Override
	public String toString() {
		String s = "Top\n";
		for(int i=0;i<slices.length*3;i++) {
			if(i%3==0)
				s+=(slices.length-(i/3+1)+1+":\t[ ");
			s+=slices[slices.length-(i/3+1)][i%3];
			if(i%3!=2)
				s+=", ";
			else
				s+=" ]\n";
		}
		return s+"Bottom";
	}
	
	public static void main(String args[]) {
		//Puzzle One
		int[][] slices = new int[30][3];
		int[] colOccur = new int[30];
		int skips = 0;
		for(int i=0;i<90;i++) {
			int curCol = -1;
			while(curCol==-1||colOccur[curCol-1]>2) {
				if(curCol>-1)
					skips++;
				curCol = 1+((int)((i+1+skips)*Math.pow(Math.PI, 11))%30);
			}
			slices[i/3][i%3]=curCol;
			colOccur[curCol-1]++;
		}
		Tower tower = new Tower(slices);
		System.out.println("Puzzle One\n"+tower.toString());
		tower.findPaths();
		System.out.println("Minimal Obstacle/Solution:");
		tower.minObs.printPath();
		tower.minObs.printSlices();
		
		//Puzzle Two
		slices = new int[30][3];
		colOccur = new int[30];
		skips = 0;
		for(int i=0;i<90;i++) {
			int curCol = -1;
			while(curCol==-1||colOccur[curCol-1]>2) {
				if(curCol>-1)
					skips++;
				curCol = 1+((int)((i+1+skips)*Math.pow(Math.E, 11))%30);
			}
			slices[i/3][i%3]=curCol;
			colOccur[curCol-1]++;
		}
		tower = new Tower(slices);
		System.out.println("Puzzle Two\n"+tower.toString());
		tower.findPaths();
		System.out.println("Minimal Obstacle/Solution:");
		tower.minObs.printPath();
		tower.minObs.printSlices();
		
		//Puzzle Three
		slices = new int[30][3];
		colOccur = new int[30];
		skips = 0;
		for(int i=0;i<90;i++) {
			int curCol = -1;
			while(curCol==-1||colOccur[curCol-1]>2) {
				if(curCol>-1)
					skips++;
				curCol = 1+(((i+1+skips)*53)%30);
			}
			slices[i/3][i%3]=curCol;
			colOccur[curCol-1]++;
		}
		tower = new Tower(slices);
		System.out.println("Puzzle Three\n"+tower.toString());
		tower.findPaths();
		System.out.println("Minimal Obstacle/Solution:");
		tower.minObs.printPath();
		tower.minObs.printSlices();
		
		//Puzzle Four
		slices = new int[30][3];
		colOccur = new int[30];
		skips = 0;
		for(int i=0;i<90;i++) {
			int curCol = -1;
			while(curCol==-1||colOccur[curCol-1]>2) {
				if(curCol>-1)
					skips++;
				curCol = 1+(((i+1+skips)*29)%30);
			}
			slices[i/3][i%3]=curCol;
			colOccur[curCol-1]++;
		}
		tower = new Tower(slices);
		System.out.println("Puzzle Four\n"+tower.toString());
		tower.findPaths();
		System.out.println("Minimal Obstacle/Solution:");
		tower.minObs.printPath();
		tower.minObs.printSlices();
	}
	
	public class Path {
		Path[] children;
		Path parent;
		Path root;
		
		int height;
		int index;
		int[] slice;
		int collisions;
		
		public Path(int index) {
			children = new Path[3];
			parent = null;
			root = this;
			
			this.height = 1;
			this.index = index;
			collisions = 0;
		}
		
		public Path newChild(int index, int[] slice) {
			int i=0;
			while(i<3&&children[i]!=null)
				i++;
			if(i<3) {
				Path child = new Path(index);
				child.root = this.root;
				child.parent = this;
				child.height = this.height+1;
				child.updateCollisions(slice);
				//System.out.print(child.collisions+": ");
				//printSlice(slice);
				this.children[i]=child;
				return child;
			}
			System.err.println("Path: "+this+" already has a child in that index!");
			return null;
		}
		
		public void updateCollisions(int[] slice) {
			this.slice = slice;
			if(this.parent!=null)
				this.collisions = parent.collisions;
			Path cur = this;
			int count[] = new int[3];
			while(cur.parent!=null) {
				cur=cur.parent;
				for(int i=0;i<3;i++)
					if(cur.slice[i]==this.slice[i])
						count[i]++;
			}
			for(int i:count)
				if(i>=1)
					this.collisions++;
		}
		
		public void printPath() {
			String s = index+" ]Bottom";
			Path cur = this;
			while(cur.height>1) {
				cur = cur.parent;
				s = cur.index+", "+s;
			}
			System.out.println(this.collisions+" collisions: Top[ "+s);
		}
		
		public void printSlices() {
			Path cur = this;
			System.out.println("Solved Tower:\nTop");
			printSlice(cur.slice);
			while(cur.parent!=null) {
				cur = cur.parent;
				printSlice(cur.slice);
			}
			System.out.println("Bottom");
		}
	}
}
