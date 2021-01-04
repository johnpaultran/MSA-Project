import java.util.*;
import java.lang.*;

// main class is the driver code of the program
public class Main
{
	public static void main(String[] args) throws Exception
	{
		// initialize fileName and create scanner
		String fastaName ="";
		Scanner scanner = new Scanner(System.in);

		// retrieve FASTA file name from user
		System.out.println("Enter your FASTA file name");
		fastaName = scanner.next();

		// print out file name and prompt
		System.out.println("\nFile name = " + fastaName);
		System.out.println("Multiple Sequence Alignment of nucleotides in P53");
		System.out.println("-------------------------------------------------");
		// call to file reader to read FASTA file
		new FastaReader(fastaName);
	}
}