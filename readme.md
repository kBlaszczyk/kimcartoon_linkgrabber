# KimCartoon Linkgrabber  
  
Tool to automatically collect video links from the KimCartoon platform.

The application is based on web browser automation using Selenium.
Captured links either get posted to stdio or written into the specified file.
  
## Usage:  
`java -jar kimcartoon-linkgrabber.jar -h`
`java -jar kimcartoon-linkgrabber.jar -q=720p CARTOON > cartoon_links.txt`
`java -jar KimCartoon-Linkgrabber.jar -t -f=cartoon_links.txt CARTOON`
  
CARTOON represents the cartoon name according to the KimCartoon URL.  
Example: https://kimcartoon.to/Cartoon/SpongeBob-SquarePants-Season-01  
CARTOON = SpongeBob-SquarePants-Season-01
  
## Compile and Deploy:
Run the application: `gradle run`  
Deploy an executable jar file: `gradle fatJar`
