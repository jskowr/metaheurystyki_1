package zad_1;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Population {
	private Individual[] members;
	private int pop_size;
	private FileData fileData;
	private List<String> hashes;

	public List<String> getHashes() {
		return hashes;
	}

	public void setHashes(List<String> hashes) {
		this.hashes = hashes;
	}

	public Individual[] getMembers() {
		return members;
	}

	public void setMembers(Individual[] members) {
		this.members = members;
		hashes = new ArrayList<String>();
		for (int i = 0; i < members.length; i++) {
			hashes.add(members[i].getHash());
		}
	}

	public Population(int pop_size, FileData fileData) {
		this.pop_size = pop_size;
		this.members = new Individual[pop_size];
		this.fileData = fileData;
	}

	public void randomInitialization() {
		for (int i = 0; i < this.pop_size; i++) {
			this.members[i] = Individual.createRandomIndividual(fileData);
		}
	}

	public void greedyInitialization() {
		for (int i = 0; i < this.pop_size; i++) {
			this.members[i] = Individual.createGreedySolution(fileData);
		}
	}

	public double[] getValues() {
		double worst = this.members[0].getValue();
		double best = this.members[0].getValue();
		double sum = 0.0;
		double[] values = new double[3];

		for (int i = 1; i < members.length; i++) {
			double tempValue = this.members[i].getValue();
			if (tempValue > worst)
				worst = tempValue;
			if (tempValue < best)
				best = tempValue;
			sum += tempValue;
		}

		values[0] = worst;
		values[1] = sum / members.length;
		values[2] = best;
		return values;
	}

	public int howMuchDifferentIndividuals() {
		ArrayList<String> count = new ArrayList<String>();
		for (int i = 0; i < this.members.length; i++) {
			if (!count.contains(this.members[i].getHash()))
				count.add(this.members[i].getHash());
		}
		return count.size();
	}
}
