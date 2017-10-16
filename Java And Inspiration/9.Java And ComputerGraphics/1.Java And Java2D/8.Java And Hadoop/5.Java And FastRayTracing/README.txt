To create the input text file:
javac JavaAndImageTextFile.java
java JavaAndImageTextFile -txt input file 700 700

To compile and run the job:
javac -classpath hadoop-0.20.0/hadoop-0.20.0-core.jar  -d JavaAndFastRayTracing JavaAndFastRayTracing.java
jar -cvf JavaAndFastRayTracing.jar -C JavaAndFastRayTracing/ .
rm -r output/ && ./hadoop-0.20.0/bin/hadoop jar JavaAndFastRayTracing.jar JavaAndFastRayTracing input output
java JavaAndImageTextFile -img output part-r-00000 700 700

Then you could open the image file at:
/output/part-r-00000.jpg
