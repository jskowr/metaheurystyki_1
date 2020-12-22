package zad_1;

import java.util.Random;

public class Process {

	private Loader loader;
	private FileData[] filesData;
	private int pop_size;
	private int gen;
	private double px;
	private double pm;
	private int tour;
	private Configuration cfg;

	public Process() {
		this.cfg = new Configuration();
		this.loader = new Loader();
		this.pop_size = cfg.getPop_size();
		this.gen = cfg.getGen();
		this.px = cfg.getPx();
		this.pm = cfg.getPm();
		this.tour = cfg.getTour();
	}

	public void run() {
		long startTime = System.nanoTime();
		// code
		int runs = 10;
		this.filesData = this.loader.loadAllFilesData();
		for (FileData fileData : this.filesData) {
			Operators operators = new Operators(fileData);
			int[] x_data = new int[gen];
			int[] y_data_avg = new int[gen];
			int[] y_data_worst = new int[gen];
			int[] y_data_best = new int[gen];

			int bestSum = 0;
			int bestOfAll = Integer.MAX_VALUE;
			int worstOfAll = 0;
			int[] bestArr = new int[runs];
			Random r = new Random();
			for (int i = 0; i < runs; i++) {
				System.out.println("run: " + (i + 1));
				for (int j = 0; j < gen; j++) {
					y_data_avg[j] = 0;
					y_data_worst[j] = 0;
					y_data_best[j] = 0;
				}

				Population population = new Population(pop_size, fileData);
				population.randomInitialization();
				for (int j = 0; j < gen; j++) {

					
					Individual[] newMembers = new Individual[pop_size];

					for (int z = 0; z < pop_size; z++) {
						double choosed = r.nextDouble();

						if (choosed < px) {
							if (cfg.isPartially()) {
								Individual[] indTemp = operators.partiallyMatchedCrossover(operators.selectionTournament(population, tour), operators.selectionTournament(population, tour));
					
								int[] newSequence = new int[indTemp[0].getSequence().length];
								int[] newSequence2 = new int[indTemp[1].getSequence().length];
								for(int x=0; x<indTemp[0].getSequence().length; x++) {
									newSequence[x] = indTemp[0].getSequence()[x];
									newSequence2[x] = indTemp[1].getSequence()[x];
								}
								newMembers[z] = new Individual(fileData);
								newMembers[z].setSequence(newSequence);
								if(z!=pop_size-1) {
									newMembers[z+1] = new Individual(fileData);
									newMembers[z+1].setSequence(newSequence2);
								}
								z++;
							}

							if (cfg.isOrdered()) {
								
								if (cfg.isTournament()) {
									newMembers[z] = operators.oderedCrossover(operators.selectionTournament(population, tour), operators.selectionTournament(population, tour));
								}
								if (cfg.isRoulette()) {
									newMembers[z] = operators.oderedCrossover(operators.selectionRoulette(population), operators.selectionRoulette(population));
								}
							}
						}else {
							newMembers[z] =  operators.selectionTournament(population, tour);
						}
					}
					
					population.setMembers(newMembers);
					
					/*
					if (cfg.isTournament()) operators.newPopulationByTournament(population, tour);
					if (cfg.isRoulette()) operators.newPopulationByRoulette(population);
					
					if(cfg.isOrdered()) operators.oderedCrossoverForPopulation(population, px);
					if(cfg.isPartially()) operators.partiallyMatchedCrossoverForPopulation(population, px);
					 */

					if (cfg.isSwap()) operators.mutationSwapForPopulation(population, pm);
					if (cfg.isInversion()) operators.mutationInversionForPopulation(population, pm);

					x_data[j] = j + 1;
					double[] values = population.getValues();
					y_data_avg[j] = (int) (values[1]);
					y_data_worst[j] = (int) (values[0]);
					y_data_best[j] = (int) (values[2]);
					
				}
				System.out.println(population.howMuchDifferentIndividuals() + " / "+pop_size);

				int best = Integer.MAX_VALUE;
				int worst = 0;
				for (int g = 0; g < gen; g++) {
					if (y_data_best[g] < best)
						best = y_data_best[g];
					if (y_data_worst[g] > worst)
						worst = y_data_worst[g];
				}
				bestArr[i] = best;
				if (best < bestOfAll)
					bestOfAll = best;
				if (best > worstOfAll)
					worstOfAll = best;
				bestSum += best;

			}
			StringBuilder nameBuilder = new StringBuilder();
			nameBuilder.append(fileData.getName());
			if (cfg.isTournament())
				nameBuilder.append(",tournament");
			if (cfg.isRoulette())
				nameBuilder.append(",roulette");

			if (cfg.isOrdered())
				nameBuilder.append(",OX");
			if (cfg.isPartially())
				nameBuilder.append(",PMX");

			if (cfg.isSwap())
				nameBuilder.append(",swap");
			if (cfg.isInversion())
				nameBuilder.append(",inversion");

			double std = 0;
			for (int z = 0; z < bestArr.length; z++) {
				System.out.println(bestArr[z]);
				std += Math.pow((bestArr[z] - bestSum / runs), 2);
			}
			std /= runs;
			double dstd = Math.sqrt(std);
			nameBuilder.append(",pop_size=" + pop_size + ",gen=" + gen + ",Px=" + px + ",Pm=" + pm + ",Tour=" + tour);

			String name = nameBuilder.toString();
			System.out.println(name);
			System.out.println("EA (10)....best: " + bestOfAll + ", worst: " + worstOfAll + ", avg: " + bestSum / runs
					+ ", std: " + dstd);
			Chart chart = new Chart(name, x_data, y_data_worst, y_data_avg, y_data_best);
			chart.saveChart(name);
		}
		long endTime = System.nanoTime();
		System.out.println("Took " + (endTime - startTime) / 1000000000 + " s");
	}

	public static Location getCustomerDataByNumber(FileData fileData, int customerNumber) {
		Location customer = fileData.getCustomers()[customerNumber - 1];
		return customer;
	}

	public static double costFunction(FileData fileData, Individual individual) {
		double sum = 0.0;
		int[] routes = individual.convertSequenceToRoutes();

		Location loc1 = null;
		Location loc2 = null;
		for (int i = 0; i < routes.length - 1; i++) {
			if (routes[i] == -1)
				loc1 = fileData.getDepot();
			else
				loc1 = Process.getCustomerDataByNumber(fileData, routes[i]);
			if (routes[i + 1] == -1)
				loc2 = fileData.getDepot();
			else
				loc2 = Process.getCustomerDataByNumber(fileData, routes[i + 1]);
			sum += Process.getDistanceBetweenTwoLocations(loc1, loc2);
		}

		return sum;
	}

	public static double getDistanceBetweenTwoLocations(Location location1, Location location2) {
		return Math.sqrt(
				Math.pow(location1.getX() - location2.getX(), 2) + Math.pow(location1.getY() - location2.getY(), 2));
	}
}
