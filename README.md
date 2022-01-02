# Space Invaders

Hello and welcome to my Space Invaders Project! 

I have always enjoyed playing video games, so I thought I would create my own twist on a classic video game like Space Invaders. My goal was to design and implement an algorithm that played Space Invaders for me, so I could sit back and watch as it racked up the points! I accomplished this by using Java and open-source computer vision software (OpenCV). Using these technologies, I created an application that takes a screenshot of the current game state, analyzes the image for any bullets, and moves the player according. The result is a "robot" that has a knack for dodging bullets and shooting aliens! I also implemented a window that allows users to "see" what the computer is processing in real-time. I have uploaded all source code and resources that I used for this project in this repo.

In the future, I hope to incorporate a targeting mechanism that locks onto enemies as a more efficient means of playing the game. I also would look into training an AI model to play the game rather than "hard-coding" an algorithm.

To play for yourself, download the JAR file located in this repo. Next, open the command interface in your computer, navigate to the directory where the JAR file is located, and enter ```java –jar SpaceInvadersFinal.jar```. Then head over to https://www.free80sarcade.com/spaceinvaders.php and scroll about half way down the page. Press 'x' to start a new game. I added in a 5 second padding, so you should be able to switch from the command interface to the website during that time. After the 5 seconds a window should open in the upper right of your screen and the player should begin to move on its own. Now just sit back and let the computer do the work for you! Close the pop-up window when you want the program to terminate. Hope you enjoy!

Note:  
  - *Must have Java 11 or higher installed*  
  - *After the window opens you might have to click again on the game to regain focus.*  
  - *Use the pop-up window to adjust the size and position of the game on the screen. It is correctly positioned if a blue rectangle follows the player as it moves.*
