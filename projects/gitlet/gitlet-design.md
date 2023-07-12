# Gitlet Design Document

**Name**: Jacky Zhao

## Classes and Data Structures
### Commit
This class stores in the information of the commit so that later on it can be accessed to view or restore any changes that was made to a previous version.

**Fields**
1. Integer `idnum` the hashcode of the commit
2. String `message` message of the commit
3. String `timestamp` time and date the commit was made (use pattern)

### Command
List of commands for gitlet

**Fields**
1. **add:** Adds a copy of the file to be ready for commit.
2. **init:** Create a new gitlet version control system in the current repository.
3. **rm:** Removing the files specified when using the command.
4. **log:** Show's a log of all the commits that was made including the id number, message, and timestamp. 
5. **status:** Show's the files that have been changed and need to be committed to the main directory.
6. **push:** Pushing changes to the current working directory.
7. **pull:** Get access to the changes made to the current working directory.


## Algorithms
### Commit Class
1. `Commit()`: the class constructor, saves the state of the Commit by making a copy of the current commit and adding it to the data structure that holds all the commits so it can later be accessed to view or restore a previous version of the changes. 

### Command Class
1. `addCommand(String fileName)`: Adds the file (if it's not already in the directory) by creating a file and storing it. ``gitlet add [filename]``
2. `initCommand()`: Initiates a new repository of gitlet by making a new directory. Declare the current directory as the **Master Branch** in order to navigate through other folders and files on gitlet.
3. `rmCommand(String fileName)`: Removes the file given the fileName typed by the user.
4. `logCommand()`: Shows a log of all the commits that were made including the `timestamp`, `message`, and `Hashcode`. Stores it in a list.
5. `statusCommand()`: Displays the state of the current repository by printing a message of the files that either need to be added, committed, or saved.
6. `pushCommand()`: Push changes to the working directory.
7. `pullCommand()`: Fetch and download content from a remote repository and update the current repository to match the content in that repository.


### Main Class
1. Where the commands are being ran. Sets up for persistence so it's possible to review or restore a previous version through readObject(). Use a Scanner to read in the files of the Commit Hashcode and be able to find any file that was saved inside of gitlet.


## Persistence
All persistent data should be stored in a "gitlet" directory in the current working directory. 
Keep track of the current state we are in after each call to gitlet.Main by executing:``java -ea gitlet.Main [configuration file] [input] [output]``

We would want to be able to encrypt another input file without providing a configuration file. That is, the settings we provided in the first run should persist across multiple executions of the program.

In order to persist the settings of the commit, we will need to save the state of the commit message after each call to add. To do this,

1. Write all the Commit objects to disk. We can serialize the Commit objects and write them to files on disk (for example, “Dog” file, “Cat” file, etc.). This can be done with the writeObject method from the Utils class. We will make sure that our Rotor class implements the Serializable interface.
2. Store all the Commits in a Hashmap, where the first value is the Hashcode of the Commit, and the second value is the Commit object.

In order to retrieve our state, before executing any code, we need to search for the saved files in the working directory (folder in which our program exists) and load the objects that we saved in them. Since we set on a file naming convention (“Cat”, etc.) our program always knows which files it should look for. We can use the readObject method from the Utils class to read the data of files as and deserialize the objects we previously wrote to these files.