package zad_1;

public class Configuration {

	private String[] fileNames;
	private int pop_size;
	private int gen; 
	private double px;
	private double pm; 
	private int tour;
	private int filesCount;
	
	private boolean roulette;
	private boolean tournament;
	
	private boolean inversion;
	private boolean swap;
	
	private boolean ordered;
	private boolean partially;
	
	public Configuration() {
		filesCount = 1;
		
		fileNames = new String[this.filesCount];
//		this.fileNames[0] = "A-n32-k5";
//		this.fileNames[0] = "A-n37-k6";
//		this.fileNames[0] = "A-n39-k5";
//		this.fileNames[0] = "A-n45-k6";
//		this.fileNames[0] = "A-n48-k7";
//		this.fileNames[0] = "A-n54-k7";
		this.fileNames[0] = "A-n60-k9";
		
		pop_size = 200;
		gen = 10000;
		px = 0.6;
		pm = 0.9;
		tour = 10;	
		
		roulette = false;
		tournament = true;
		
		inversion = true;
		swap = false;
		
		ordered =  false;
		partially = true;
		
	}

	
	
	public boolean isRoulette() {
		return roulette;
	}



	public void setRoulette(boolean roulette) {
		this.roulette = roulette;
	}



	public boolean isTournament() {
		return tournament;
	}



	public void setTournament(boolean tournament) {
		this.tournament = tournament;
	}



	public boolean isInversion() {
		return inversion;
	}



	public void setInversion(boolean inversion) {
		this.inversion = inversion;
	}



	public boolean isSwap() {
		return swap;
	}



	public void setSwap(boolean swap) {
		this.swap = swap;
	}



	public boolean isOrdered() {
		return ordered;
	}



	public void setOrdered(boolean ordered) {
		this.ordered = ordered;
	}



	public boolean isPartially() {
		return partially;
	}



	public void setPartially(boolean partially) {
		this.partially = partially;
	}



	public int getFilesCount() {
		return filesCount;
	}



	public void setFilesCount(int filesCount) {
		this.filesCount = filesCount;
	}


	public String[] getFileNames() {
		return fileNames;
	}

	public void setFileNames(String[] fileNames) {
		this.fileNames = fileNames;
	}

	public int getPop_size() {
		return pop_size;
	}

	public void setPop_size(int pop_size) {
		this.pop_size = pop_size;
	}

	public int getGen() {
		return gen;
	}

	public void setGen(int gen) {
		this.gen = gen;
	}

	public double getPx() {
		return px;
	}

	public void setPx(double px) {
		this.px = px;
	}

	public double getPm() {
		return pm;
	}

	public void setPm(double pm) {
		this.pm = pm;
	}

	public int getTour() {
		return tour;
	}

	public void setTour(int tour) {
		this.tour = tour;
	}
	
	
	
}
