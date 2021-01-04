import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// class to read in FASTA file and pass to MultipleSequenceAlignment
public class FastaReader
{
	// arraylists to store sequence names and sequences from FASTA file
	ArrayList<String> sequenceNames = new ArrayList<>();
	ArrayList<String> sequences = new ArrayList<>();

	// string to manipulate what to add into sequences arraylist
	static String currentSequence;

	// file reader to take in FASTA file
	public FastaReader(String fastaName) throws FileNotFoundException
	{
		File input = new File(fastaName);
		// set current sequence to empty string
		currentSequence = "";
		try
		{
			Scanner scanner = new Scanner(input);
			while(scanner.hasNextLine())
			{
				// trim white space in front and behind string in next line
				String line = scanner.nextLine().trim();
				// if first character in line is '>' then line contains name of sequence
				if(line.charAt(0) == '>')
				{
					// add sequence name
					sequenceNames.add(line);
					// if the current sequence isn't empty means that we just hit next sequence
					if(!currentSequence.equals(""))
					{
						// add current sequence and reset string to empty for next iteration
						sequences.add(currentSequence);
						currentSequence = "";
					}
				}
				// if first character in line isn't '>' then line contains sequence
				if(line.charAt(0) != '>')
				{
					// add line to current sequence
					currentSequence += line;
				}
			}
			// add current sequence and close scanner
			sequences.add(currentSequence);
			scanner.close();
		}
		finally
		{
			// construct and run multiple sequence alignment on given file
			MultipleSequenceAlignment p53 = new MultipleSequenceAlignment();
			p53.performMSA(sequences);
		}
	}
}
