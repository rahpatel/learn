Analysis of Subset Methods

1. subset1 Method

Here both the sub and super arrays are unsorted.
So for searching the Linear Search Algorithm was used as this does not require any of the two input arrays to be sorted.
Linear search has linear time complexity:
It takes time n if the item is not found or takes n/2, on average, if the item is found

So if the size of super array is n and the size of subset is  m,then
The loop in subset is run m times, for each of the subset element,calling Linear search each time.
The loop in Linear search runs n times.

So the total running time would  be k*(m*n) + c, where k and c are some constants.
We say that subset1 method takes O(m*n) time.


2. subset2 Method

Here the sub array is not sorted, but the super array is sorted.
So for searching an element as to whether it is present in a array Binary Search Algorithm was used.
Binary search has logarithmic (log n) time complexity.

So if the size of super array is n and the size of subset is  m,then
The loop in subset is run m times, for each of the subset element,calling Binary search each time.
The loop in Binary search runs logn times.

So the total running time would  be k*(m*logn) + c, where k and c are some constants.
We say that subset2 method takes O(m*logn) time.

3. subset3 Method

Here both the sub and  super arrays are  sorted.
So for searching an element as to whether it is present in a array Binary Search Algorithm was used.
Binary search has logarithmic (log n) time complexity.

Here we use the below logic.

Whenever we find that a particular element in sub array is at a location x in the super array,
then we would start the search for the next element in the sub array from the  location 
x+1 in the super array ,instead of location 0 of the super array. This would be a
variation of Binary search algorithm , that we dont keep the lower bound 0 always
whenever we start searching for a new element.


So the total running time would  be k*(n) + c, where k and c are some constants.
We say that subset3 method takes O(n) time.

Analysis of findSubset Method

 findSubset Method

In order to find a particular subset whose sum is equal to target, we find out the powerset of the given array.
Since a power set contains all the subsets of a array, we sum the elements in each of the subsets in the powerset and determine whether the sum is equal 
to the target. When sum is equal to the target it is the requires subset whose elements sums to the target value.

Now there are 2^n subsets in powerset of a set of n elements. So In the worst case we have to evaluate 2^n subsets to check their sum.
So the method findsubset takes O(2^n) time.


* * * * * * * * * * * * * * * * * * Analyzing   subset1   Method * * * * * * * * * * * *
* * * * * * * *
Sub Array Size         Superset Array Size      No.of different Arrays      No. of Times  each run    Time Taken
20                            40                           25                       1000                  105
               
40                            80                           25                       1000                  163 
                
80                            160                           25                      1000                  403
                 
160                            320                           25                     1000                  834 
                 
320                            640                           25                     1000                  3255


                  


* * * * * * * * * * * * * * * * * * Analyzing   subset2   Method * * * * * * * * * * * *
* * * * * * * *
Sub Array Size         Superset Array Size      No.of different Arrays      No. of Timeseach run    Time Taken
20                            40                           25                       1000               36
                
100                            200                         25                       1000               160
               
500                            1000                        25                       1000               997
                  
2500                            5000                       25                       1000               6454 
                  
12500                            25000                     25                       1000               40056                  
                  




* * * * * * * * * * * * * * * * * * Analyzing   subset3   Method * * * * * * * * * * * *
* * * * * * * *
Sub Array Size         Superset Array Size      No.of different Arrays      No. of Timeseach run    Time Taken
20                            40                           25                       1000               16
                
100                            200                         25                       1000               99
                  
500                            1000                        25                       1000               663              
                 
2500                            5000                       25                       1000              4152   
                  
12500                            25000                     25                       1000              26599     
                




* * * * * * * * * * * * * * * * * *Analyzing   findSubset   Method * * * * * * * * * * *
* * * * * * * * *
Array Size             No.of times ran        Time Taken
10                      10                       9
15                      10                       43
20                      10                       31
25                      10                       505
30                      10                       378 








* * * * * * * * * * * * * * * * * * Analyzing   subset1   Method * * * * * * * * * * * *
* * * * * * * *
Sub Array Size         Superset Array Size      No.of different Arrays      No. of Times each run    Time Taken
5                            10                           25                       1000                  31 
               
10                            20                           25                       1000                 10
               
20                            40                           25                       1000                 26
               
40                            80                           25                       1000                 161
               
80                            160                           25                       1000                608
               




* * * * * * * * * * * * * * * * * * Analyzing   subset2   Method * * * * * * * * * * * *
* * * * * * * *
Sub Array Size         Superset Array Size      No.of different Arrays      No. of Times   each run    Time Taken
5                            10                           25                       1000                  44
               
10                            20                           25                       1000                  24
               
20                            40                           25                       1000                  51
               
40                            80                           25                       1000                  132
               
80                            160                           25                       1000                 186
               




* * * * * * * * * * * * * * * * * * Analyzing   subset3   Method * * * * * * * * * * * *
* * * * * * * *
Sub Array Size         Superset Array Size      No.of different Arrays      No. of Timeseach run    Time Taken
5                            10                           25                       1000                 5  
               
10                            20                           25                       1000                 5
               
20                            40                           25                       1000                15
               
40                            80                           25                       1000                 32
               
80                            160                           25                       1000                76
               




* * * * * * * * * * * * * * * * * *Analyzing   findSubset   Method * * * * * * * * * * *
* * * * * * * * *
Array Size             No.of times ran        Time Taken
10                      10                       24
15                      10                       26
20                      10                       157
25                      10                       138
30                      10                       865 