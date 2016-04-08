# MyComicsFullProject
An in development, for personal use, comic book database for android.

This is my first app, so try not to judge me or my code too harshly.  Mostly a learning experience.

By the end, it should operate like so:
It opens up to all characters the user owns.  
Should display character name and an image of the users choosing.  
A button on the bottom should allow the user to add a character.  
Clicking on the image should allow the user to change it from the gallery.  
Clicking on the name should take the user to a new activity that shows what Series' the user owns based on that character.  
Holding the name should prompt the user to delete the character.  
The series screen should look similar to the character screen, only it shows Series titles.  Behaves the same as well.  
Clicking a series shows a new screen that displays the issue numbers the user has.

Currenlty, it operates like so:
Everything works in character screen. Even added an "Edit" button, incase the user misstyped. Havent added remove/edit functionality for
other activities yet.

Thoughts:  
--After most recent update, wanted to get rid of a chuck of code that was bothering me.  In the 
MainActivity.java file, in the characterDialog method, I was having an issue where it would sometimes add a duplicated
character so I just said "if you just added two characters, delete the last one".  I removed that code, but with the expence of
needing to restart the activity everytime I add a character. Need to debug original code. Figure out what is going on.--  

Should use a better structure for saving data.  
Will improve asthetics once it works properly.  
Add sort function for character and series.  
Should clean up code.  Right now it is ductaped together.  
Currently very "brute forced".  
Come up with a more astheticaly pleasing way to display issues.  
Pictures dont scale accross platforms. Currently best on my Galaxy S5.  

Possible future modifications/new apps:  
Character pic is just a random series pic which would add variety.  
More information such as publisher.  
Implement API databases.  
Output a text file that can be sent to an email so the user can print it and take it with them.  
Use a barecode scanner to get information.  
Maybe do away with current strucure, and just have an option to display by Publisher, Character, Series, etc.  
"Wish list" analizes what comics you have, and suggests next in series. EX: "You have Superman 1-12. Recomend Superman 13."  
