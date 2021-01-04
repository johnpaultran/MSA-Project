// class for to create an object for a pair of sequences and store its information
public class SequencePair
{
	// instance variables
	String sequence1;
	String sequence2;
	String alignedSequence1;
	String alignedSequence2;
	int highScore;
	String concurrence;
	
	// constructor for pair class
	public SequencePair(String seq1, String seq2)
	{
		this.sequence1 = seq1;
		this.sequence2 = seq2;
	}

	// following are get methods to retrieve the indicated values
	public String getSequence1()
	{
		return sequence1;
	}

	public String getSequence2()
	{
		return sequence2;
	}

	public String getAlignedSequence1()
	{
		return alignedSequence1;
	}

	public String getAlignedSequence2()
	{
		return alignedSequence2;
	}

	public int getHighScore()
	{
		return highScore;
	}

	// following are set methods to set each value
	public void setAlignedSeq1(String alignedSequence1)
	{
		this.alignedSequence1 = alignedSequence1;
	}

	public void setAlignedSeq2(String alignedSequence2)
	{
		this.alignedSequence2 = alignedSequence2;
	}

	public void setHighScore(int highScore)
	{
		this.highScore = highScore;
	}

	public void setConcurrence(String concurrence)
	{
		this.concurrence = concurrence;
	}
}
