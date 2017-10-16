_________________________________________________________________

Copyright @ Wanwan Li in Graphics Area. EECS. UCF
_________________________________________________________________

This javascript code implements the dice game on WebGL platform which 
supports OpenGL/SL/Shadow Mapping and Texture Mapping Machnism.
Here there is a cubic dice, when you press "->" key to start roate it, and 
press "<-" key to stop it and there will be a number shown in front of you.
Every 5 times, the color of the scene will be changed which means that
one pass of game finished. And the the sum of these five numbers are 13.

You could set the varibles on the JavaAndDice.html file: on Line 588:

	var RUN_TIMES=5, RUN_SUM=13;

According to your requirement.

*If you want to open the webpage with loding texture from other files,  
there are three options:
1. Run the code on the local website server: for example resin:
http://127.0.0.1:8080/JavaAndDiceGame/JavaAndDice.html
2. Open Chrome.exe with cmd
cd C:\Program Files\Google\Chrome\Application
chrome.exe --allow-file-access-from-files
3. Directly Run this code on Firefox web browser.

Have Fun!
_________________________________________________________________