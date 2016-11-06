# topn

Get the greatest N numbers, greatest first from a file.

Input File format
```
1
2
3
```

## Implementation

1. We are reading the file line by line.
2. For every line, we compare the current line value with a list
3. If the value is greater than the minimum of the list we remove the minimal value and insert the new one

The complexithy of the algorithm is O(l*log(n))
* l: number of line in the file
* n: number of value to return

In the details, a dichotomic search is used in combination with a [TreeList](https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/list/TreeList.html)
* Inserting: O(1)
* Deleting: O(1)
* Getting: O(log(n))
* Searching (with the dichotomic search): O(log(n))

## Compilation

```
mvn package
```

## Help

```
$java -jar topn-0.1-jar-with-dependencies.jar -h
usage: topn
Given an arbitrarily large file and a number, N, containing individual
numbers on each line (e.g. 200Gb file), will output the largest N numbers,
highest first.
 -f,--file <arg>           File path
 -h,--help                 print this message
 -ll,--log4j-level <arg>   Log4j level (DEBUG, INFO, WARN or ERROR)
 -n,--topn <arg>           N: the number of value to 
```

## Execution

```
$java -jar topn-0.1-jar-with-dependencies.jar -f test2.txt -n 4
80.0
78.0
20.0
19.0
```

## Possible Improvement

We could thread the job. However as we are reading from one file (therefore one disk), disk i/o will quickly be a bottle neck.
