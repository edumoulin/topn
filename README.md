# topn

Get the greatest N numbers, greatest first from a file.

## Implementation

1. We are reading the file line by line.
2. For every line, we compare the current line value with a list
3. If the value is greater than the minimum of the list we remove the minimal value and insert the new one

The time complexity of the algorithm is `O(l*log(n)^2)`
* l: number of line in the file
* n: number of value to return

In detail, a dichotomic search is used in combination with a [TreeList](https://commons.apache.org/proper/commons-collections/apidocs/org/apache/commons/collections4/list/TreeList.html)

Tree List time performance:
* Inserting: `O(1)`
* Deleting: `O(1)`
* Getting: `O(log(n))`

Dichotomic search time performance:
* Dichotomic search in an array: `O(log(n))`
* Dichotomic search in a Tree list: `O(log(n)^2)`

For every line, in the worse case scenario, we search, insert, delete and get:
* `O(log(n)^2+1+1+log(n)) = O(l*log(n)^2)`

At the end we iterate through the topn list for display, the complexity is `O(n*log(n))` with `n <= l`:
* `O(l*log(n)^2 + n*log(n) ) = O(l*log(n)^2)`

The size complexity of the algorithm is `O(n)`, as we only store the topn list.

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
 -n,--topn <arg>           N: the number of value to return
```

## Execution


The input file supports integer of double values. Unrecognized numbers are ignored.
```
20
19
78
80
3
```

```
$java -jar topn-0.1-jar-with-dependencies.jar -f test2.txt -n 4
80.0
78.0
20.0
19.0
```

## Possible Improvement

We could thread the job. However as we are reading from one file (therefore one disk), disk i/o will quickly be a bottle neck.
