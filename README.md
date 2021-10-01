# Key-value data base

## Description

This program is utility for work with key-value database

## Database file format

We consider that database is a directory with name ends with `.db`.
Inside this directory we have the file with name `size` were we store database size and the list of file.
Each file name is equal to hash of keys stored in it. 