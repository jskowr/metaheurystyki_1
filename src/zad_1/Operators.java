
package zad_1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Operators {
	
	private FileData fileData;
	
	
	
	public FileData getFileData() {
		return fileData;
	}

	public void setFileData(FileData fileData) {
		this.fileData = fileData;
	}

	public Operators(FileData fileData) {
		this.fileData = fileData;
	}
	
	public void mutationSwap(Individual individual, double pm) {
		int[] sequence = individual.getSequence();
		Random random = new Random();
		int random1 = random.nextInt(sequence.length);
		int random2 = random1;
		while(random1 == random2) {
			random2 = random.nextInt(sequence.length);
		}
		int[] newSequence = new int[sequence.length];
		for(int i=0; i<sequence.length; i++) {
			newSequence[i] = sequence[i];
		}
		if(random.nextDouble()<pm) {
		int tempValue = newSequence[random2];
		newSequence[random2] = newSequence[random1];
		newSequence[random1] = tempValue;
		}
		individual.setSequence(newSequence);;
	}
	
	public void mutationSwapGene(Individual individual, double pm) {
		int[] sequence = individual.getSequence();
		Random random = new Random();
		int[] newSequence = new int[sequence.length];
		for(int i=0; i<sequence.length; i++) {
			newSequence[i] = sequence[i];
		}
		for(int j=0; j<sequence.length; j++) {
		int random1 = j;
		int random2 = random1;
		while(random1 == random2) {
			random2 = random.nextInt(sequence.length);
		}
		if(random.nextDouble()<pm) {
		int tempValue = newSequence[random2];
		newSequence[random2] = newSequence[random1];
		newSequence[random1] = tempValue;
		}
		}
		individual.setSequence(newSequence);;
	}
	
	public void mutationSwapForPopulation(Population population, double pm) {
		for(int i=0; i<population.getMembers().length; i++) {
			mutationSwapGene(population.getMembers()[i], pm);
		}
	}
	
	public void mutationInversion(Individual individual) {
		int[] sequence = individual.getSequence();
		Random random = new Random();
		int random1 = random.nextInt(sequence.length);
		int random2 = random1;
		while(random1 == random2) {
			random2 = random.nextInt(sequence.length);
		}
		int start = -1;
		int end = - 1;
		if(random1<random2) {
			start = random1;
			end = random2;
		}
		else {
			start = random2;
			end = random1;
		}
		
		int[] newSequence = new int[sequence.length];
		for(int i=0; i<newSequence.length; i++) {
			newSequence[i] = sequence[i];
		}
		
		ArrayList<Integer> seqTemp = new ArrayList<Integer>();
		for(int i=start; i<=end; i++) {
			seqTemp.add(newSequence[i]);
		}
		Collections.reverse(seqTemp);
		for(int i=start; i<=end; i++) {
			newSequence[i] = seqTemp.get(i-start);
		}
			
		individual.setSequence(newSequence);
	}
	
	public void mutationInversionForPopulation(Population population, double pm) {
		Random r = new Random();
		for(int i=0; i<population.getMembers().length; i++) {
			if(r.nextDouble()<pm) {
				//System.out.println("sekwencja 1: "+population.getMembers()[i].getHash());
				mutationInversion(population.getMembers()[i]);
				//System.out.println("sekwencja 2: "+population.getMembers()[i].getHash());
				//System.out.println();
			}
		}
	}
	
	public Individual selectionTournament(Population population, int N) {
		Random random = new Random();
		int choosed = random.nextInt(population.getMembers().length);
		double bestValue = population.getMembers()[choosed].getValue();
		int bestIndex = choosed;
		for(int i=1; i<N; i++) {
			choosed = random.nextInt(population.getMembers().length);
			double tempValue = population.getMembers()[choosed].getValue(); 
			if(tempValue<bestValue) {
				bestValue = tempValue;
				bestIndex = choosed;
			}
		}
		
		Individual newInd = new Individual(fileData);
		newInd.setCapacity(population.getMembers()[bestIndex].getCapacity());
		newInd.setSequence(population.getMembers()[bestIndex].getSequence());
		
		
		return newInd;
	}
	
	public void newPopulationByTournament(Population population, int N){
		
		Individual[] newMembers = new Individual[population.getMembers().length];
		
		for(int i=0; i<population.getMembers().length; i++) {
			newMembers[i] = this.selectionTournament(population, N);
		}
		
		population.setMembers(newMembers);
	}
	
	public Individual selectionRoulette(Population population) {
		Individual[] members = population.getMembers(); 
		double[] values = new double[members.length];
		double valuesSum = 0.0;
		for(int i=0; i<values.length; i++) {
			double v = members[i].getValue();
			values[i] = 1/v;
			valuesSum += 1/v;
		}
		Random random = new Random();
		double randomValue = random.nextDouble()*valuesSum;

		double sumTemp = 0.0;
		int bestIndex = -1;
		for(int i=0; i<values.length; i++) {
			sumTemp += values[i];
			if(sumTemp >= randomValue) {
				bestIndex = i;
				break;
			}
		}
		return members[bestIndex];
	}
	
	public void newPopulationByRoulette(Population population) {
		Individual[] newMembers = new Individual[population.getMembers().length];
		
		for(int i=0; i<population.getMembers().length; i++) {
			newMembers[i] = this.selectionRoulette(population);
		}
		
		population.setMembers(newMembers);
	}
	
	public Individual oderedCrossover(Individual ind1, Individual ind2) {
		
		int[] sequence = ind1.getSequence();
		Random random = new Random();
		int random1 = random.nextInt(sequence.length);
		int random2 = random1;
		while(random1 == random2) {
			random2 = random.nextInt(sequence.length);
		}
		int start = -1;
		int end = - 1;
		if(random1<random2) {
			start = random1;
			end = random2;
		}
		else {
			start = random2;
			end = random1;
		}
		ArrayList<Integer> seqTemp = new ArrayList<Integer>();
		ArrayList<Integer> seqTemp2 = new ArrayList<Integer>();
		for(int i=start; i<=end; i++) {
			seqTemp.add(ind1.getSequence()[i]);
		}
		
		for(int i=0; i<sequence.length; i++) {
			if(!seqTemp.contains(ind2.getSequence()[i])) seqTemp2.add(ind2.getSequence()[i]);
		}
		
		int[] newSequence = new int[sequence.length];
		
		int cnt = 0;
		for(int i=0; i<newSequence.length; i++) {
			if(i<start) {
				newSequence[i] = seqTemp2.get(cnt); 
				cnt++;
			}
			else if(i>end) {
				newSequence[i] = seqTemp2.get(cnt); 
				cnt++;
			}
			else newSequence[i] = seqTemp.get(i-start);		
		}
		
		Individual newIndividual = new Individual(fileData);
		newIndividual.setSequence(newSequence);
		
		return newIndividual;
	}
	
	public void oderedCrossoverForPopulation(Population population, double px) {
		
		int size = population.getMembers().length;
		Individual[] newMembers = new Individual[size];
		Random r = new Random();
		for(int i=0; i<size; i++) {
			
			//Individual ind = oderedCrossover(this.selectionTournament(population, tour), this.selectionTournament(population, tour));
			//newMembers[i] = ind;
			
			double choosed = r.nextDouble();
			if(choosed<px) {
				if(i!=size-1) newMembers[i] = oderedCrossover(population.getMembers()[i], population.getMembers()[i+1]);
				else {
					newMembers[i] = oderedCrossover(population.getMembers()[i], population.getMembers()[0]);
				}
			}else {
				newMembers[i] = population.getMembers()[i];
			}
			
		}
		population.setMembers(newMembers);
	}
	
	public Individual[] partiallyMatchedCrossover(Individual ind1, Individual ind2) {
		Individual newInd1 = new Individual(fileData);
		Individual newInd2 = new Individual(fileData);

		int[] sequence1 = ind1.getSequence();
		int[] sequence2 = ind1.getSequence();
		Random random = new Random();
		int random1 = random.nextInt(sequence1.length);
		int random2 = random1;
		while(random1 == random2) {
			random2 = random.nextInt(sequence1.length);
		}
		int start = -1;
		int end = - 1;
		if(random1<random2) {
			start = random1;
			end = random2;
		}
		else {
			start = random2;
			end = random1;
		}
		
		ArrayList<Integer> seqMapping1 = new ArrayList<Integer>();
		ArrayList<Integer> seqMapping2 = new ArrayList<Integer>();
		
		int[] newSeq1 = new int[sequence1.length];
		int[] newSeq2 = new int[sequence2.length];
		
		for(int i=start; i<=end; i++) {
			seqMapping1.add(sequence2[i]);
			seqMapping2.add(sequence1[i]);
		}
		
		for(int i=0; i<sequence1.length; i++) {
			if(i<start || i>end) {
				int temp1 = sequence1[i];
				while(seqMapping1.contains(temp1)) {
					int index = seqMapping1.indexOf(temp1);
					temp1 = seqMapping2.get(index);
				}
				
				int temp2 = sequence2[i];
				while(seqMapping2.contains(temp2)) {
					int index2 = seqMapping2.indexOf(temp2);
					temp2 = seqMapping1.get(index2);
				}
				
				newSeq1[i] = temp1;
				newSeq2[i] = temp2;
			}else {
				newSeq1[i] = sequence2[i];
				newSeq2[i] = sequence1[i];
			}	
		}
		
		Individual[] newInds = new Individual[2];
		newInd1.setSequence(newSeq1);
		newInd2.setSequence(newSeq2);
		newInds[0] = newInd1;
		newInds[1] = newInd2;
		return newInds;
	}
	
	public void partiallyMatchedCrossoverForPopulation(Population population, double px) {
		
		Random r = new Random();
		double randomValue = r.nextDouble();
		Individual[] newMembers = new Individual[population.getMembers().length];
		for(int i=0; i<population.getMembers().length;) {
			
			if(i<population.getMembers().length-1 && r.nextDouble()<px) {
				Individual[] inds = partiallyMatchedCrossover(population.getMembers()[i], population.getMembers()[i+1]);
				newMembers[i] = inds[0];
				newMembers[i+1] = inds[1];
				i+=2;
			}
			else {
				newMembers[i] = population.getMembers()[i];
				i++;
			}
			/*else {
				newMembers[i] = population.getMembers()[i];
				newMembers[i+1] = population.getMembers()[i+1];
			}
			i+=2;*/
		}
		population.setMembers(newMembers);
	}
	
}
