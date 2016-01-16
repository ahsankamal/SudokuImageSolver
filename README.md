# SudokuImageSolver

# Step1
![Alt text](/screenshots/step1.png?raw=true "")

# Step2
![Alt text](/screenshots/step2.png?raw=true "")

# Step3
![Alt text](/screenshots/step3.png?raw=true "")

# Step4
![Alt text](/screenshots/step4.png?raw=true "")

# Step5
![Alt text](/screenshots/step5.png?raw=true "")



# Dependencies
      Tess4J (A Java JNA wrapper for Tesseract OCR API)
      Java SE Development Kit 8


First of all setup the development environment.
      http://tess4j.sourceforge.net/tutorial/


Download Tess4j (http://sourceforge.net/projects/tess4j/).
Then Add these jar files to the classpath.
        jai_imageio.jar, 
        jna.jar, 
        commons-io-2.4.jar,
        tess4j.jar.
Try to run this sample code
        (http://tess4j.sourceforge.net/codesample.html).
        Make sure tessdata folder are in the search path.
If everything went Awesome till now then just import the project into your workspace 
and enjoy solving the sudoku present in the newspaper,magazines or online.
To improve the accuracy of OCR, capture the sudoku with flash on.

# Sudoku.java 
It is the main file of Project. Compile and Run it to see the GUI.


# Assumption 
  Sudoku puzzle must be present inside a grid.
  
See testcases folder to get the idea about how to take the sudoku pic before Testing it on app.






