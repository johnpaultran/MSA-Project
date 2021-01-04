# MSA-Project
Final project for CS 123A: Bioinformatics I. Multiple Sequence Alignment using the Smith-Waterman Algorithm

For the program, the Smith-Waterman algorithm is used to perform multiple sequence alignment. The Smith-Waterman algorithm is considered a dynamic programming algorithm, 
which means that it simplifies a complicated problem into simpler sub-problems in a recursive manner. This algorithm was selected because it performs local sequence alignment. 
The optimal local alignment of two sequences is the one that finds the longest segment of high sequence similarity  between the two sequences.
The Smith-Waterman algorithm uses matches, mismatches, and gaps (insertion/deletion) to align two sequences. 
The steps of the algorithm are as follows; setting the scoring for match and mismatch as well as the gap penalty scheme. 
In this program, an affine gap penalty that considers the gap opening penalty and gap extension penalty separately will be used. 
Then the scoring matrix is initialized with the length of each sequence plus one as the dimensions. 
One unique feature of the Smith-Waterman is that the first row and column is set to 0. 
Each element in the matrix is then scored based on the diagonal, vertical, and horizontal scores (substitutions and gaps). 
Another unique feature in Smith-Waterman is that all negative scores are set to 0. 
The last step is to generate the alignment by tracing back in the matrix from the highest scoring element until 0 is encountered. 
This program also generates a similarity score based on a concurrence string with all the aligned sequences together without gaps. 
If a nucleotide at the index of the concurrence string matches the index of the aligned sequences, then the score increments by 1.
