# Text-Summarizer
A text summarizer that uses sentence scoring according to T. Sri Rama Raju and Bhargav Allarpu's article "Text Summarization using Sentence Scoring Method": https://www.irjet.net/archives/V4/i5/IRJET-V4I5493.pdf

## Usage
1. Compile the package by running the following line from within the TextSummarizer directory:

> `javac src/main/java/Main.java src/main/java/Sentence.java src/main/java/Utils.java`

2. Navigate to the `src/main/java` directory:

> `cd src/main/java`

3. Run the program on an input document in .txt format, followed by the percentage of the length of the original document you would like your summary to be. For example, to summarize `TextSummarizer/TestDocument.txt` to a length of 20% of the original length, run:

> `java Main TestDocument.txt 20`

4. Check `TextSummarizer/output` for the summarized text.