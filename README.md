# Map
##### An app which helps you to track the location of your friends.

This app will help you to find live location of your friends (who are using this app).</br>

#### How to use this app:
1. User need to create an account with a *unique* username.</br>
2. App will ask permission and user needs to allow all permissions for the app to run properly.</br>
3. If the user wants to search their friends location, they will need their(friend's) *username*.</br>
Exampli gratia: A will need B's username to search B's location.

#### Working:
1. Only if the user provides all the permissions,the application will be able to access the location.</br>
2. Location of the user will be sent to the server and be saved. 
3. This process will continue to work even if the app is closed(activity is destroyed), because this process is implemented using *services*.</br>
4. The location of the user changes countinously in the server.</br>
5. If the user searches for his friend, the application will fetch the data from the server and show the live location of the friend.

#### Images:
<h5>Search Friend</h5>
<img src="https://github.com/saurabh13209/Map/blob/master/Read%20me/App%20screenshot%20(1).png" width="200" height="400" />
