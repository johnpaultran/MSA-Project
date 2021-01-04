import java.util.ArrayList;
import java.util.Scanner;

// MultipleSequenceAlignment class contains the main functions to run MSA
public class MultipleSequenceAlignment
{
	// main function to perform multiple sequence alignment
	public void performMSA(ArrayList<String> sequencesArrayList)
	{
		// create scanner
		Scanner scanner = new Scanner(System.in);

		// array lists to store sequence info
		ArrayList<String> sequences = new ArrayList<>(sequencesArrayList);
		ArrayList<String> sequencesStored = new ArrayList<>(sequences);
		ArrayList<SequencePair> sequencePairs = new ArrayList<>();

		// instance variables
		int matchScore;
		int mismatchScore;
		int gapOpening;
		int gapExtension;
		String currentSequence1;
		String currentSequence2;
		int highScore;
		int currentHighScore;
		int index = 0;
		String sequence1Copy;
		String sequence2Copy;
		String alignedSequence1;
		String alignedSequence2;

		// initialize string to hold concurrence of all sequences
		String concurrence = "";

		int score = 0;

		// retrieve parameters for alignment from user
		System.out.println("Enter match score");
		matchScore = scanner.nextInt();
		System.out.println("Enter mismatch score");
		mismatchScore = scanner.nextInt();
		System.out.println("Enter gap opening penalty");
		gapOpening = scanner.nextInt();
		System.out.println("Enter gap extension penalty");
		gapExtension = scanner.nextInt();
		
		// create pairs of sequences (objects) and add them to the pairs ArrayList
		for(int i = 0; i < sequences.size() - 1; i++)
		{
			for(int j = i + 1; j < sequences.size(); j++)
			{
				currentSequence1 = sequences.get(i);
				currentSequence2 = sequences.get(j);
				
				SequencePair macAndCheese = new SequencePair(currentSequence1, currentSequence2);
				sequencePairs.add(macAndCheese);

				// call to align method
				align(currentSequence1, currentSequence2, matchScore, mismatchScore, gapOpening, gapExtension, macAndCheese);
			}
		}

		// while there are still sequences in array list
		while(sequences.size() > 1)
		{
			highScore = 0;
			// search for highest scoring sequence pair and store its index within pairs
			for(int i = 0; i < sequencePairs.size(); i++)
			{
				currentHighScore = sequencePairs.get(i).getHighScore();
				if(currentHighScore > highScore)
				{
					highScore = currentHighScore;
					index = sequencePairs.indexOf(sequencePairs.get(i));
				}
			}

			// upon testing, was receiving out of bounds error on some gap penalty values
			// this conditional statement ensures index is within arraylist
			if (index >= sequencePairs.size())
			{
				int difference = index - sequencePairs.size() + 1;
				index = index - difference;
			}

			// retrieve two sequences and two aligned sequences from their corresponding Pair object
			sequence1Copy = sequencePairs.get(index).getSequence1();
			sequence2Copy = sequencePairs.get(index).getSequence2();
			alignedSequence1 = sequencePairs.get(index).getAlignedSequence1();
			alignedSequence2 = sequencePairs.get(index).getAlignedSequence2();

			// create the concurrence from the two aligned sequences with preference to non-gaps
			concurrence = "";
			for(int i = 0; i < alignedSequence1.length(); i++)
			{
				// if current nucleotide is the same
				if(alignedSequence1.charAt(i) == alignedSequence2.charAt(i))
				{
					concurrence += alignedSequence1.charAt(i);
				}
				// if current nucleotide is different
				else if(alignedSequence1.charAt(i) != alignedSequence2.charAt(i))
				{
					// store the nucleotide instead of the gap
					if(alignedSequence1.charAt(i) == '-')
					{
						concurrence += alignedSequence2.charAt(i);
					}
					else if(alignedSequence2.charAt(i) == '-')
					{
						concurrence += alignedSequence1.charAt(i);
					}
					else {
						// randomly pick one if it is different nucleotides
						double rand = Math.random();
						if(rand <= 0.5)
						{
							concurrence += alignedSequence1.charAt(i);
						}
						else if(rand > 0.5)
						{
							concurrence += alignedSequence2.charAt(i);
						}
					}
				}
			}

			// remove from sequences
			sequences.remove(sequence1Copy);
			sequences.remove(sequence2Copy);
		
			// remove pairs with the two highest scoring sequences
			for(int i = 0; i < sequencePairs.size(); i++)
			{
				if(sequencePairs.get(i).getSequence1().equals(sequence1Copy) || sequencePairs.get(i).getSequence1().equals(sequence2Copy)
						|| sequencePairs.get(i).getSequence2().equals(sequence1Copy) || sequencePairs.get(i).getSequence2().equals(sequence2Copy))
				{
					sequencePairs.remove(i);
				}
			}
		
			// run sequence pairs with the new concurrence sequence
			for (String s : sequences)
			{
				currentSequence1 = s;

				SequencePair xayahAndRakan = new SequencePair(currentSequence1, concurrence);
				sequencePairs.add(xayahAndRakan);

				// call to align method
				align(currentSequence1, concurrence, matchScore, mismatchScore, gapOpening, gapExtension, xayahAndRakan);
			}

			// add concurrence string back to the sequences ArrayList
			sequences.add(concurrence);
		}

		// clear out pairs list
		sequencePairs.clear();
		
		// compare original sequences with the concurrence sequence and store as new objects
		for (String s : sequencesStored)
		{
			currentSequence1 = s;

			SequencePair bonnieAndClyde = new SequencePair(currentSequence1, concurrence);
			sequencePairs.add(bonnieAndClyde);

			// call to align method
			align(currentSequence1, concurrence, matchScore, mismatchScore, gapOpening, gapExtension, bonnieAndClyde);
		}

		// print the sequence alignments
		System.out.println("\nAlignment Results:");
		for (SequencePair sequencePair : sequencePairs)
		{
			System.out.println(sequencePair.getAlignedSequence1());
		}
		// new line for clarity
		System.out.println("");

		// calculate similarity score from concurrence
		for(int i = 0; i < concurrence.length(); i++)
		{
			boolean match = true;
			char nucleotide = concurrence.charAt(i);
			for(int j = 0; j < sequencePairs.size(); j++)
			{
				if(sequencePairs.get(j).getAlignedSequence1().charAt(i) != nucleotide)
				{
					match = false;
					break;
				}
			}
			// if match is true, add to similarity score
			if(match)
			{
				score += 1;
			}
		}
		System.out.print("Similarity Score = " + score);
	}

	// function to align two sequences using the Smith-Waterman algorithm
	public int align(String sequence1, String sequence2, int matchScore, int mismatchScore, int gapOpening, int gapExtension, SequencePair lewisAndClark)
	{
		// instance variables
		int diagonalScore;
		int leftScore;
		int verticalScore;
		int alignmentScore;
		double rand;

		// create two 2D arrays for the scoring and traceback matrices
		int[][] scoringMatrix = new int[sequence2.length() + 1][sequence1.length() + 1];
		int[][] tracebackMatrix = new int[sequence2.length() + 1][sequence1.length() + 1];

		// set constants for each direction in matrix
		final int DIAGONAL = 1;
		final int VERTICAL = 2;
		final int LEFT = 3;
		final int VERTICAL_DIAGONAL_TIE = 4;
		final int LEFT_DIAGONAL_TIE = 5;
		final int VERTICAL_LEFT_TIE = 6;
		final int ALL_TIE = 7;

		// create two strings to store the finalized aligned sequences
		String alignedSequence1 = "";
		String alignedSequence2 = "";

		// initialize matrices
		for(int i = 0; i < sequence2.length() + 1; i++)
		{
			for(int j = 0; j < sequence1.length() + 1; j++)
			{
				scoringMatrix[i][j] = 0;
				if(i == 0 && j >= 1)
				{
					tracebackMatrix[i][j] = 3;
				}
				else if(i >= 1 && j == 0)
				{
					tracebackMatrix[i][j] = 2;
				}
			}
		}

		// fill the matrix with actual scores
		for(int i = 1; i < sequence2.length() + 1; i++)
		{
			for(int j = 1; j < sequence1.length() + 1; j++)
			{
				// if there is a match, set match score to all score methods
				if(sequence1.charAt(j - 1) == sequence2.charAt(i - 1))
				{
					diagonalScore = matchScore;
					leftScore = matchScore;
					verticalScore = matchScore;
				}
				// else set scores to mismatch (negative scores set to 0 in Smith-Waterman)
				else {
					diagonalScore = mismatchScore;
					leftScore = mismatchScore;
					verticalScore = mismatchScore;
				}

				// diagonal score = match/mismatch + score from diagonal
				diagonalScore += (scoringMatrix[i-1][j-1]);
				
				// compute best score from cell to the left
				// left score = match/mismatch + gap penalty on left
				if( i == sequence2.length())
				{
					leftScore += scoringMatrix[i][j-1];
				}
				else if(tracebackMatrix[i][j-1] == 3 || tracebackMatrix[i][j-1] == 5
						|| tracebackMatrix[i][j-1] == 6 || tracebackMatrix[i][j-1] == 7)
				{
					leftScore += gapExtension + scoringMatrix[i][j-1];
				}
				else {
					leftScore += (gapOpening + scoringMatrix[i][j-1]);
				}
				
				// compute best score from cell above
				// vertical score = match/mismatch + gap penalty from top
				if( j == sequence1.length())
				{
					verticalScore += scoringMatrix[i-1][j];
				}
				else if(tracebackMatrix[i-1][j] == 2 || tracebackMatrix[i-1][j] == 4 
						|| tracebackMatrix[i-1][j] == 6 || tracebackMatrix[i-1][j] == 7)
				{
					verticalScore += (gapExtension + scoringMatrix[i-1][j]);
				}
				else {
					verticalScore += (gapOpening + scoringMatrix[i-1][j]);
				}
				
				// assign the highest score of the three possibilities to the matrix
				int highScore = Math.max(diagonalScore, Math.max(leftScore, verticalScore));
				scoringMatrix[i][j] = highScore;
				
				// assign traceback value to the traceback matrix and address ties if any are present
				if(diagonalScore > leftScore && diagonalScore > verticalScore)
				{
					tracebackMatrix[i][j] = DIAGONAL;
				}
				else if(verticalScore > diagonalScore && verticalScore > leftScore)
				{
					tracebackMatrix[i][j] = VERTICAL;
				}
				else if(leftScore > diagonalScore && leftScore > verticalScore)
				{
					tracebackMatrix[i][j] = LEFT;
				}
				else if(diagonalScore == verticalScore && diagonalScore > leftScore)
				{
					tracebackMatrix[i][j] = VERTICAL_DIAGONAL_TIE;
				}
				else if(diagonalScore == leftScore && diagonalScore > verticalScore)
				{
					tracebackMatrix[i][j] = LEFT_DIAGONAL_TIE;
				}
				else if(leftScore == verticalScore && leftScore > diagonalScore)
				{
					tracebackMatrix[i][j] = VERTICAL_LEFT_TIE;
				}
				else if(diagonalScore == verticalScore && diagonalScore == leftScore)
				{
					tracebackMatrix[i][j] = ALL_TIE;
				}
			}
		}

		// set alignment score
		alignmentScore = scoringMatrix[sequence2.length()][sequence1.length()];

		// follow the traceback matrix to create the aligned sequences
		int j = sequence1.length();
		int i = sequence2.length();
		while(i != 0 || j != 0)
		{
			if(tracebackMatrix[i][j] == 1)
			{
				alignedSequence1 = sequence1.charAt(j-1) + alignedSequence1;
				alignedSequence2 = sequence2.charAt(i-1) + alignedSequence2;
				i += -1;
				j += -1;
			}
			else if(tracebackMatrix[i][j] == 2)
			{
				alignedSequence1 = "-" + alignedSequence1;
				alignedSequence2 = sequence2.charAt(i-1) + alignedSequence2;
				i += -1;
			}
			else if(tracebackMatrix[i][j] == 3)
			{
				alignedSequence1 = sequence1.charAt(j-1) + alignedSequence1;
				alignedSequence2 = "-" + alignedSequence2;
				j += -1;
			}
			else if(tracebackMatrix[i][j] == 4)
			{
				rand = Math.random();
				if(rand >= 0.5)
				{
					alignedSequence1 = sequence1.charAt(j-1) + alignedSequence1;
					alignedSequence2 = sequence2.charAt(i-1) + alignedSequence2;
					i += -1;
					j += -1;
				}
				else {
					alignedSequence1 = "-" + alignedSequence1;
					alignedSequence2 = sequence2.charAt(i-1) + alignedSequence2;
					i += -1;
				}
			}
			else if(tracebackMatrix[i][j] == 5)
			{
				rand = Math.random();
				if(rand >= 0.5)
				{
					alignedSequence1 = sequence1.charAt(j-1) + alignedSequence1;
					alignedSequence2 = sequence2.charAt(i-1) + alignedSequence2;
					i += -1;
					j += -1;
				}
				else {
					alignedSequence1 = sequence1.charAt(j-1) + alignedSequence1;
					alignedSequence2 = "-" + alignedSequence2;
					j += -1;
				}
			}
			else if(tracebackMatrix[i][j] == 6)
			{
				rand = Math.random();
				if(rand >= 0.5)
				{
					alignedSequence1 = "-" + alignedSequence1;
					alignedSequence2 = sequence2.charAt(i-1) + alignedSequence2;
					i += -1;
				}
				else {
					alignedSequence1 = sequence1.charAt(j-1) + alignedSequence1;
					alignedSequence2 = "-" + alignedSequence2;
					j += -1;
				}
			}
			else if(tracebackMatrix[i][j] == 7)
			{
				if(i == 1 && j == 1)
				{
					alignedSequence1 = sequence1.charAt(j-1) + alignedSequence1;
					alignedSequence2 = sequence2.charAt(i-1) + alignedSequence2;
					i += -1;
					j += -1;
				}
				else {
					rand = Math.random();
					if(rand <= 0.33)
					{
						alignedSequence1 = sequence1.charAt(j-1) + alignedSequence1;
						alignedSequence2 = sequence2.charAt(i-1) + alignedSequence2;
						i += -1;
						j += -1;
					}
					else if(rand > 0.33 && rand < 0.66)
					{
						alignedSequence1 = "-" + alignedSequence1;
						alignedSequence2 = sequence2.charAt(i-1) + alignedSequence2;
						i += -1;
					}
					else if(rand >= 0.66)
					{
						alignedSequence1 = sequence1.charAt(j-1) + alignedSequence1;
						alignedSequence2 = "-" + alignedSequence2;
						j += -1;
					}
				}
			}
		}

		// create the concurrence from the two aligned sequences with preference to non-gaps
		String concurrence = "";
		for(int k = 0; k < alignedSequence1.length(); k++)
		{
			// if current nucleotide is the same
			if(alignedSequence1.charAt(k) == alignedSequence2.charAt(k))
			{
				concurrence += alignedSequence1.charAt(k);
			}
			// if current nucleotide is different
			else if(alignedSequence1.charAt(k) != alignedSequence2.charAt(k))
			{
				// store the nucleotide instead of the gap
				if(alignedSequence1.charAt(k) == '-')
				{
					concurrence += alignedSequence2.charAt(k);
				}
				else if(alignedSequence2.charAt(k) == '-')
				{
					concurrence += alignedSequence1.charAt(k);
				}
				else {
					// randomly pick one if it is different nucleotides
					rand = Math.random();
					if(rand <= 0.5)
					{
						concurrence += alignedSequence1.charAt(k);
					}
					else if(rand > 0.5)
					{
						concurrence += alignedSequence2.charAt(k);
					}
				}
			}
		}

		// store information in Pair object
		lewisAndClark.setConcurrence(concurrence);
		lewisAndClark.setHighScore(alignmentScore);
		lewisAndClark.setAlignedSeq1(alignedSequence1);
		lewisAndClark.setAlignedSeq2(alignedSequence2);

		// end function
		return 0;
	}
}