# Key-value data base

## Description

This program is utility for work with key-value database

# _User guide_

## Run
* You can run this utility with this command:
```
./gradlew --console=plain -q run
```

## Database file format

We consider that database is a directory with name ends with `.db`.
Inside this directory we have the file with name `size` were we store database size and the list of file.
Each file name is equal to hash of keys stored in it.

## List of commands:
* `help` show help message
* `quit` - finish work with databases and quit this manager
* `create [NAME]` - create database with name equals to NAME
* `open [NAME]` - open and work with database NAME
* `delDB` - delete opened database
* `fetch [KEY]` - fetch the value of given KEY from opened database
* `delete [KEY]` - delete given KEY from opened database
* `store [KEY] [VALUE]` - store pair (KEY, VALUE) into opened database

## Example of using

```
KVDB> create a
a.db> create b
b.db> open a
a.db> delDB
KVDB> open b
b.db> store key value
b.db> store "key with spaces" "value with spaces"
b.db> fetch "key with spaces"
value with spaces
b.db> store "key" "value with \" special symbols \\ "
b.db> fetch key 
value with " special symbols \ 
b.db> delete key
b.db> fetch key
No item "key" in database
b.db> delDB
KVDB> quit
```

## Testing
This utility was tested with test in file `src/test/kotlin/TestBigDataBase.kt`
So, it did 10^5 Sequentially `store` operations,
then 10^5 `fetch` operations,
then 10^5 `delete` operations 
for 3 minutes and 10 seconds
(On HDD)